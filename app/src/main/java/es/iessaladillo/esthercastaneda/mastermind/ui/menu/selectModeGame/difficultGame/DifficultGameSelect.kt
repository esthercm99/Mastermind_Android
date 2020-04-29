package es.iessaladillo.esthercastaneda.mastermind.ui.menu.selectModeGame.difficultGame

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import es.iessaladillo.esthercastaneda.mastermind.R
import kotlinx.android.synthetic.main.difficult_game_select_fragment.*
import kotlinx.android.synthetic.main.main_activity.*


class DifficultGameSelect : Fragment(R.layout.difficult_game_select_fragment) {

    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    private val navController: NavController by lazy {
        NavHostFragment.findNavController(navHostFragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupButtons()
    }

    private fun setupButtons() {
        btn_back.setOnClickListener {
            navController.navigateUp()
        }
        btnEasy.setOnClickListener {
            navigateToModeSelect(0)
        }
        btnNormal.setOnClickListener {
            navigateToModeSelect(1)
        }
        btnHard.setOnClickListener {
            navigateToModeSelect(2)
        }
    }

    private fun navigateToModeSelect(difficult: Int) {
        settings.edit {
            putInt("difficultGame", difficult)
        }
        navController.navigate(R.id.modeGame)
    }

}
