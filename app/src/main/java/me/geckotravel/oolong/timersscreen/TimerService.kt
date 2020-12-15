package me.geckotravel.oolong.timersscreen

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import me.geckotravel.oolong.MainActivity
import me.geckotravel.oolong.R
import me.geckotravel.oolong.media.RingtonePlayer
import me.geckotravel.oolong.models.Duration
import me.geckotravel.oolong.models.TeaRecipe
import me.geckotravel.oolong.models.TeaTimer
import org.koin.android.ext.android.inject

class TimerService : Service() {

    private val binder = LocalBinder()
    private val ringtonePlayer: RingtonePlayer by inject()

    // channel ID is needed for sdk > 26
    private val channelIdTimerRunning: String by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    "tea_timers_running",
                    getString(R.string.notification_channel_running_timers),
                    NotificationManager.IMPORTANCE_LOW
            )
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(channel)
            "tea_timers_running"
        } else {
            ""
        }
    }

    private val channelIdTimerDone: String by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    "tea_timers_done",
                    getString(R.string.notification_channel_timer_done),
                    NotificationManager.IMPORTANCE_HIGH
            )
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(channel)
            "tea_timers_done"
        } else {
            ""
        }
    }

    private inner class RunningTeaTimer(
            val recipe: TeaRecipe,
            val notificationId: Int,
    ) : CountDownTimer(recipe.brewingDuration.milliseconds, 1000) {

        private val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var remainingTime = recipe.brewingDuration
        var isFinished = false

        override fun onTick(millisUntilFinished: Long) {
            notificationManager.notify(
                    runningTimersNotificationId,
                    createRunningTimerNotification()
            )
            updateRunningTimersObservable()
            remainingTime = Duration.fromMilliseconds(millisUntilFinished)
        }

        override fun onFinish() {
            isFinished = true

            if (timers.values.all { it.isFinished }) {
                stopForeground(true)
                //stopSelf() // when is the service actually killed ?
            }

            notificationManager.notify(
                    notificationId,
                    createDoneNotification(recipe.name)
            )

            ringtonePlayer.play()

            updateRunningTimersObservable()
        }

        fun dismiss() {
            cancel()
            timers.remove(notificationId)

            if (timers.isEmpty()) {
                stopForeground(true)
                stopSelf()
            }

            notificationManager.cancel(notificationId)

            ringtonePlayer.stop()
        }
    }

    private val timers: MutableMap<Int, RunningTeaTimer> = mutableMapOf()
    private val runningTimersNotificationId = 2
    private var nextDoneNotificationId = 424242

    // allow the app to observe timers
    val runningTimers: MutableLiveData<List<TeaTimer>> = MutableLiveData()

    private fun updateRunningTimersObservable() {
        runningTimers.postValue(timers.values.map {
            TeaTimer(
                    it.notificationId,
                    it.recipe,
                    it.remainingTime,
                    it.isFinished
            )
        })
    }

    fun startTimer(recipe: TeaRecipe) {
        with(RunningTeaTimer(recipe, nextDoneNotificationId)) {
            timers[nextDoneNotificationId] = this
            start()
        }
        nextDoneNotificationId += 1

        updateRunningTimersObservable()

        startForeground(runningTimersNotificationId, createRunningTimerNotification())
    }

    fun dismissTimer(timer: TeaTimer) {
        timers[timer.id]?.dismiss()
    }

    fun stopRingtone() = ringtonePlayer.stop()

    private fun createRunningTimerNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        return if (timers.values.filterNot { it.isFinished }.size == 1) {
            val runningTimer = timers.values.first { !it.isFinished }
            NotificationCompat.Builder(this, channelIdTimerRunning)
                    .setContentTitle(getString(R.string.notification_running_timer_title, runningTimer.recipe.name))
                    .setContentText(getString(R.string.notification_running_timer_text, runningTimer.remainingTime.toString()))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentIntent(pendingIntent)
                    .build()
        } else {
            NotificationCompat.Builder(this, channelIdTimerRunning)
                    .setContentTitle(getString(R.string.notification_mutliple_running_timers_title))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentIntent(pendingIntent)
                    .setStyle(NotificationCompat.InboxStyle().also {
                        timers.values.filterNot { it.isFinished }.forEach { runningTimer ->
                            it.addLine(
                                    getString(
                                            R.string.notification_mutliple_running_timers_text,
                                            runningTimer.recipe.name,
                                            runningTimer.remainingTime.toString()
                                    )
                            )
                        }
                    })
                    .build()
        }

    }

    private fun createDoneNotification(
            teaName: String
    ): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        return NotificationCompat.Builder(this, channelIdTimerDone)
                .setContentTitle(getString(R.string.notification_tea_is_done_title, teaName))
                .setContentText(getString(R.string.notification_tea_is_done_text))
                .setOngoing(false)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .build()
    }

    inner class LocalBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(p0: Intent?): IBinder = binder
}