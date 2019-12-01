package `fun`.gladkikh.app.fastpallet8.ui
import `fun`.gladkikh.app.fastpallet8.R
import `fun`.gladkikh.app.fastpallet8.repository.SettingsRepository
import `fun`.gladkikh.app.fastpallet8.ui.activity.MainActivity
import android.os.Bundle
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.koin.android.ext.android.inject

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    private val settingRepositoryUpdate: SettingsRepository by inject()

    override fun onPreferenceChange(p0: Preference?, p1: Any?): Boolean {
        Toast.makeText(
            activity,
            "Будь внимателен!",
            Toast.LENGTH_SHORT
        ).show()

        return true
    }

    override fun onCreatePreferences(saveInstantState: Bundle?, rootkey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootkey)
        findPreference<EditTextPreference>("preference_host")!!.onPreferenceChangeListener = this

    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).refreshSettingApp()
    }

}