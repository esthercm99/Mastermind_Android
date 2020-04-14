package es.iessaladillo.esthercastaneda.mastermind.ui.menu.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import es.iessaladillo.esthercastaneda.mastermind.R
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.main_activity.*


class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    private val navController: NavController by lazy {
        NavHostFragment.findNavController(navHostFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupButtons()
    }

    private fun setupButtons() {

        btnPlay.setOnClickListener {
            navController.navigate(R.id.difficultGameSelect)
        }
        btnManual.setOnClickListener {
            navController.navigate(R.id.manualFragment)
        }
    }

}