package es.iessaladillo.esthercastaneda.mastermind.ui.menu.settings

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import es.iessaladillo.esthercastaneda.mastermind.R
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.settings_fragment.*
import java.util.*

class SettingsFragment : Fragment(R.layout.settings_fragment) {

    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    private val navController: NavController by lazy {
        NavHostFragment.findNavController(navHostFragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnExit.setOnClickListener { navController.navigateUp() }
    }

    private fun refreshView() {
        requireFragmentManager()
            .beginTransaction()
            .detach(this)
            .attach(this)
            .commitAllowingStateLoss()
    }

}
