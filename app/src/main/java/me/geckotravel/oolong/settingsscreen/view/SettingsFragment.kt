package me.geckotravel.oolong.settingsscreen.view

import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import me.geckotravel.oolong.R

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        fun newInstance() = SettingsFragment()

        const val KEY_TEMPERATURE_UNIT = "temperature_unit"
        const val KEY_IS_RINGTONE_ENABLED = "is_ringtone_enabled"
        const val KEY_RINGTONE = "ringtone"
        const val RESULT_CODE_SET_RINGTONE = 1000
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<Preference>(KEY_RINGTONE)?.setOnPreferenceClickListener {
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            activity?.startActivityForResult(intent, RESULT_CODE_SET_RINGTONE)
            true
        }
    }

}