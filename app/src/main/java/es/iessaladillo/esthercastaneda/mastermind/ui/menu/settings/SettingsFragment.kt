package es.iessaladillo.esthercastaneda.mastermind.ui.menu.settings

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import es.iessaladillo.esthercastaneda.mastermind.R
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.settings_fragment.*
import java.util.*

class SettingsFragment : Fragment() {

    private val navController: NavController by lazy {
        NavHostFragment.findNavController(navHostFragment)
    }
    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnExit.setOnClickListener {
            refreshSettings()
            navController.navigateUp()
        }
    }

    private fun refreshSettings() {
        val newLocale: Locale
        val config = Configuration()

        if (settings.getString(getString(R.string.prefLanguage_key), getString(R.string.prefLanguage_defaultValue)).equals(getString(R.string.prefLanguage_defaultValue))) {
            newLocale = Locale("es", "ES")
        } else {
            newLocale = Locale("en", "EN")
        }

        Locale.setDefault(newLocale)
        config.locale = newLocale

        this.resources.updateConfiguration(config, this.resources.displayMetrics)
    }
}
