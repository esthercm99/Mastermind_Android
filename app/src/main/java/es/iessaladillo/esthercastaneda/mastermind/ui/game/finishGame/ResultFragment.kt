package es.iessaladillo.esthercastaneda.mastermind.ui.game.finishGame

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import es.iessaladillo.esthercastaneda.mastermind.R
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.result_fragment.*

class ResultFragment : Fragment(R.layout.result_fragment) {

    private val navController: NavController by lazy {
        NavHostFragment.findNavController(navHostFragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnCerrar.setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }
    }
}