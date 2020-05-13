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
        btnExit.setOnClickListener { navController.navigateUp() }
    }

    private fun setupAdapter() {
        val pages = listOf(
            Page(R.drawable.manual_singleplayer_es, R.drawable.manual_singleplayer_en),
            Page(R.drawable.manual_multiplayer_es, R.drawable.manual_multiplayer_en)
        )
            /*Page(R.drawable.ic_videogame_asset_black_24dp,
                "Fácil:\n" +
                        "\to Nº Fichas: 4\n" +
                        "\to Nº Rondas: 10\n" +
                        "\to ¿Se repite los colores en la combinación secreta?: No\n"),
            Page(R.drawable.ic_videogame_asset_black_24dp, "Medio:\n" +
                        "\to Nº Fichas: 5\n" +
                        "\to Nº Rondas: 15\n" +
                        "\to ¿Se repite los colores en la combinación secreta?: Sí\n"),
            Page(R.drawable.ic_videogame_asset_black_24dp, "Difícil:\n" +
                        "\to Nº Fichas: 6\n" +
                        "\to Nº Rondas: 20\n" +
                        "\to ¿Se repite los colores en la combinación secreta?: Sí"),
            Page(R.drawable.ic_videogame_asset_black_24dp,
                "Un jugador: Tendrás que adivinar la combinación secreta antes de llegar al límite de rondas."),
            Page(R.drawable.ic_videogame_asset_black_24dp,
                "Multijugador: Juegas contra la IA, tendrás que adivinar la combinación secreta antes que la IA o antes de llegar al límite de rondas."),
            Page(R.drawable.manual_chip_empty,
                "Ficha vacía: Hay una ficha que no aparece en la combinación secreta."),
            Page(R.drawable.manual_chip_white,
                "Ficha blanca: Hay una ficha que aparece en la combinación secreta, pero no está en la misma posición a la secreta."),
            Page(R.drawable.manual_chip_black,
                "Ficha negra: Hay una ficha que aparece en la combinación secreta y está en la misma posición.")
        )*/
        manualAdapter = ManualFragmentAdapter(pages).also {
            it.onItemClickListener = { navController.navigateUp() }
        }
    }

    private fun setupViewPager() { viewPager.adapter = manualAdapter }
}
