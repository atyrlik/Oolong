package me.geckotravel.oolong

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import me.geckotravel.oolong.media.RingtonePlayer
import me.geckotravel.oolong.settingsscreen.view.SettingsFragment
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val sharedPreferences: SharedPreferences by inject()
    private val ringtonePlayer: RingtonePlayer by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!.findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SettingsFragment.RESULT_CODE_SET_RINGTONE -> {
                if (resultCode != Activity.RESULT_OK || data == null) return
                val uri = data.getParcelableExtra<Parcelable>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                with (sharedPreferences.edit()) {
                    putString(SettingsFragment.KEY_RINGTONE, uri.toString())
                    commit()
                }
                ringtonePlayer.reloadRingtone()
            }
        }
    }
}