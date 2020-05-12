package es.iessaladillo.esthercastaneda.mastermind.ui.menu.profile

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import es.iessaladillo.esthercastaneda.mastermind.R
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val navController: NavController by lazy {
        NavHostFragment.findNavController(navHostFragment)
    }

    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        btnHome.setOnClickListener { navController.navigateUp() }

        nameUser.text = String.format(getString(R.string.namePlayer, settings.getString(getString(R.string.prefPlayerName_key), getString(R.string.playerName_defaultValue))))
        lblNumGames.text = String.format(getString(R.string.n_games), 0)
        lblNumGamesWin.text = String.format(getString(R.string.n_games_win), 0)
        lblNumGamesLose.text = String.format(getString(R.string.n_games_lose), 0)
    }

}
