package me.geckotravel.oolong.media

import android.content.Context
import android.content.SharedPreferences
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.core.net.toUri
import me.geckotravel.oolong.settingsscreen.view.SettingsFragment

interface RingtonePlayer {
    fun play()
    fun stop()
    fun reloadRingtone()
}

class RingtonePlayerImpl(
        private val context: Context,
        private val sharedPreferences: SharedPreferences
): RingtonePlayer {
    private var ringtoneUri = sharedPreferences.getString(SettingsFragment.KEY_RINGTONE, null)?.toUri()
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

    private var ringtone: Ringtone = RingtoneManager.getRingtone(context, ringtoneUri)

    override fun play() {
        if (sharedPreferences.getBoolean(SettingsFragment.KEY_IS_RINGTONE_ENABLED, true)) {
            ringtone.play()
        }
    }

    override fun stop() = ringtone.stop()

    override fun reloadRingtone() {
        ringtoneUri = sharedPreferences.getString(SettingsFragment.KEY_RINGTONE, null)?.toUri()
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
    }
}