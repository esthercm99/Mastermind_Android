package es.iessaladillo.esthercastaneda.mastermind.ui.menu.manual

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.tabs.TabLayoutMediator
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Page
import kotlinx.android.synthetic.main.fragment_manual.*
import kotlinx.android.synthetic.main.main_activity.*

class ManualFragment : Fragment(R.layout.fragment_manual) {

    private lateinit var manualAdapter: ManualFragmentAdapter

    private val navController: NavController by lazy {
        NavHostFragment.findNavController(navHostFragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupAdapter()
        setupViewPager()
        setupTabLayout()
        btnExit.setOnClickListener { navController.navigateUp() }
    }

    private fun setupAdapter() {
        val pages = listOf(
            Page(R.drawable.manual_howttoplay_es, R.drawable.manual_howttoplay_en),
            Page(R.drawable.manual_emptychip_es, R.drawable.manual_emptychip_en),
            Page(R.drawable.manual_whitechip_es, R.drawable.manual_whitechip_en),
            Page(R.drawable.manual_blackchip_es, R.drawable.manual_blackchip_en),
            Page(R.drawable.manual_modegame_es, R.drawable.manual_modegame_en),
            Page(R.drawable.manual_singleplayer_es, R.drawable.manual_singleplayer_en),
            Page(R.drawable.manual_multiplayer_es, R.drawable.manual_multiplayer_en),
            Page(R.drawable.manual_difficulty_es, R.drawable.manual_difficulty_en),
            Page(R.drawable.manual_easy_es, R.drawable.manual_easy_en),
            Page(R.drawable.manual_medium_es, R.drawable.manual_medium_en),
            Page(R.drawable.manual_hard_es, R.drawable.manual_hard_en)
        )
        manualAdapter = ManualFragmentAdapter(pages).also {
            it.onItemClickListener = { navController.navigateUp() }
        }
    }

    private fun setupViewPager() { viewPager.adapter = manualAdapter }
    private fun setupTabLayout() {
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
    }
}
