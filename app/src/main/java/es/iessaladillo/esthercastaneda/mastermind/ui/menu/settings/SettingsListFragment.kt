package es.iessaladillo.esthercastaneda.mastermind.ui.menu.settings

import android.content.res.Configuration
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import es.iessaladillo.esthercastaneda.mastermind.R
import java.util.*

class SettingsListFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        setupLanguage()
    }

    private fun setupLanguage() {
        val list = findPreference<ListPreference>(getString(R.string.prefLanguage_key)) as ListPreference

        list.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                val index = list.findIndexOfValue(newValue.toString())

                if (index != -1) {
                    val newLocale: Locale
                    val config = Configuration()

                    if (newValue.equals(getString(R.string.prefLanguage_defaultValue))) {
                        newLocale = Locale("es", "ES")
                    } else {
                        newLocale = Locale("en", "EN")
                    }

                    Locale.setDefault(newLocale)
                    config.locale = newLocale

                    this.resources.updateConfiguration(config, this.resources.displayMetrics)
                }
                true
            }
    }
}