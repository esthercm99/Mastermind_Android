package es.iessaladillo.esthercastaneda.mastermind.ui.menu.selectModeGame.modeGame

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.core.content.edit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import es.iessaladillo.esthercastaneda.mastermind.R
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.mode_game_fragment.*


class ModeGame : Fragment(R.layout.mode_game_fragment) {

    private lateinit var viewModel: ModeGameViewModel

    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    private val navController: NavController by lazy {
        NavHostFragment.findNavController(navHostFragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ModeGameViewModel::class.java)
        setupViews()
    }

    private fun setupViews() {
        setupButtons()
    }

    private fun setupButtons() {
        btn_back.setOnClickListener {
            navController.navigateUp()
        }
        btnSingle.setOnClickListener {
            navigateToModeSelect(0)
        }
        btnMulti.setOnClickListener {
            navigateToModeSelect(1)
        }
    }

    private fun navigateToModeSelect(modeGame: Int) {
        settings.edit {
            putInt("modeGame", modeGame)
        }
        navController.navigate(R.id.difficultGameSelect)
    }

}
