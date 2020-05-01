package es.iessaladillo.esthercastaneda.mastermind.ui.menu.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import es.iessaladillo.esthercastaneda.mastermind.R

class SettingsListFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}