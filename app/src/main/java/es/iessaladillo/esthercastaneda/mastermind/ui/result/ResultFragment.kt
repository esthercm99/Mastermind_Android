package es.iessaladillo.esthercastaneda.mastermind.ui.result

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import es.iessaladillo.esthercastaneda.mastermind.R
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.result_fragment.*

class ResultFragment : Fragment(R.layout.result_fragment) {

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

    /*
        Configura las vistas, diciendo si el jugador ha ganado o perdido.
    */
    private fun setupViews() {

        val player = settings.getString(getString(R.string.prefPlayerName_key), getString(R.string.playerName_defaultValue))

        if (settings.getBoolean("isWinner", false)) {
            txtResult.text = String.format(getString(R.string.youWin), player)
        } else {
            txtResult.text = String.format(getString(R.string.youLose), player)
            background.visibility = View.INVISIBLE
        }

        btnClose.setOnClickListener {
            navController.navigateUp()
        }
    }
}