package es.iessaladillo.esthercastaneda.mastermind.ui.menu.selectModeGame

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.core.content.edit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import assets.blink
import es.iessaladillo.esthercastaneda.mastermind.R
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.mode_game_fragment.*


class ModeGame : Fragment(R.layout.mode_game_fragment) {

    private var indexList = 0
    private var indexDifficultList = 0
    private lateinit var listOptions: List<TextView>
    private lateinit var listDifficult: List<TextView>

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
        listOptions = listOf(btnSingle, btnMulti)
        listDifficult = listOf(btnEasy, btnNormal, btnHard)
        setupButtons()
    }

    private fun setupButtons() {

        btnPlay.blink()

        btn_back.setOnClickListener {
            navController.navigateUp()
        }

        btn_left01.setOnClickListener {
            listOptions[indexList].visibility = View.INVISIBLE

            indexList--

            if (indexList < 0) {
                indexList = listOptions.size-1
            }

            listOptions[indexList].visibility = View.VISIBLE
        }

        btn_right01.setOnClickListener {
            listOptions[indexList].visibility = View.INVISIBLE

            indexList++

            if (indexList >= listOptions.size) {
                indexList = 0
            }

            listOptions[indexList].visibility = View.VISIBLE
        }

        btn_left02.setOnClickListener {
            listDifficult[indexDifficultList].visibility = View.INVISIBLE

            indexDifficultList--

            if (indexDifficultList < 0) {
                indexDifficultList = listDifficult.size-1
            }

            listDifficult[indexDifficultList].visibility = View.VISIBLE
        }

        btn_right02.setOnClickListener {
            listDifficult[indexDifficultList].visibility = View.INVISIBLE

            indexDifficultList++

            if (indexDifficultList >= listDifficult.size) {
                indexDifficultList = 0
            }

            listDifficult[indexDifficultList].visibility = View.VISIBLE
        }

        btnPlay.setOnClickListener {
            navigateToGame()
        }
    }

    private fun navigateToGame() {
        settings.edit {
            putInt("modeGame", indexList)
            putInt("difficultGame", indexDifficultList)
        }
        navController.navigate(R.id.action_modeGame_to_gameFragment)
    }

}
