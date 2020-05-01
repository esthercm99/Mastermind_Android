package es.iessaladillo.esthercastaneda.mastermind.ui.menu.manual

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import es.iessaladillo.esthercastaneda.mastermind.R
import kotlinx.android.synthetic.main.fragment_manual.*
import kotlinx.android.synthetic.main.main_activity.*

class ManualFragment : Fragment(R.layout.fragment_manual) {

    private val navController: NavController by lazy {
        NavHostFragment.findNavController(navHostFragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnExit.setOnClickListener {
            navController.navigateUp()
        }
    }
}
