package es.iessaladillo.esthercastaneda.mastermind.ui.game.inGame

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Combination
import kotlinx.android.synthetic.main.game_fragment.*
import kotlinx.android.synthetic.main.main_activity.*


class GameFragment : Fragment(R.layout.game_fragment) {

    private val gameAdapter = GameAdapter()

    private val viewModel: GameViewModel by viewModels {
        GameViewModelFactory(requireActivity().application)
    }

    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    private val navController: NavController by lazy {
        NavHostFragment.findNavController(navHostFragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lblRound.text = String.format("%s - %s", settings.getString("difficultGame", "None"), settings.getString("modeGame", "None"))
        setupRecyclerView()
        observe()
        setupViews()
    }

    private fun setupRecyclerView() {
        lstRounds.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 1)
            adapter = gameAdapter
        }
    }

    private fun observe() {
        updateList(viewModel.listCombination)
    }
    private fun updateList(newList: List<Combination>) {
        lstRounds.post {
            gameAdapter.submitList(newList)
        }
    }

    private fun setupViews() {
        chipSelectCheck()
        changeColor()
        setupButtons()
    }
    private fun setupButtons() {
        btnExit.setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }
        btnCheck.setOnClickListener {
            if (ficha01Select.background != context?.getDrawable(R.drawable.ficha_vacia) &&
                ficha02Select.background != context?.getDrawable(R.drawable.ficha_vacia) &&
                ficha03Select.background != context?.getDrawable(R.drawable.ficha_vacia) &&
                ficha04Select.background != context?.getDrawable(R.drawable.ficha_vacia)) {
                Toast.makeText(context, "Se ha añadido combinación", Toast.LENGTH_SHORT).show()
                viewModel.addCombination(Combination(ficha01Select.background.alpha, ficha02Select.background.alpha, ficha03Select.background.alpha, ficha04Select.background.alpha))
            } else {
                Toast.makeText(context, "Rellene una combinación entera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun chipSelectCheck() {
        ficha01Select.setOnClickListener { viewModel.currentChip = it.id }
        ficha02Select.setOnClickListener { viewModel.currentChip = it.id }
        ficha03Select.setOnClickListener { viewModel.currentChip = it.id }
        ficha04Select.setOnClickListener { viewModel.currentChip = it.id }
    }
    private fun changeColor() {
        fichaRoja.setOnClickListener { changeColorSelected(R.drawable.ficha_roja) }
        fichaNaranja.setOnClickListener { changeColorSelected(R.drawable.ficha_naranja) }
        fichaAzul.setOnClickListener { changeColorSelected(R.drawable.ficha_azul) }
        fichaAmarilla.setOnClickListener { changeColorSelected(R.drawable.ficha_amarillo) }
        fichaVerde.setOnClickListener { changeColorSelected(R.drawable.ficha_verde) }
        fichaMorada.setOnClickListener { changeColorSelected(R.drawable.ficha_morado) }
        fichaMarron.setOnClickListener { changeColorSelected(R.drawable.ficha_marron) }
        fichaGris.setOnClickListener { changeColorSelected(R.drawable.ficha_gris) }
    }
    private fun changeColorSelected(color: Int) {
        if (viewModel.currentChip != -1) {
            view?.findViewById<Button>(viewModel.currentChip)?.background = context?.getDrawable(color)
        } else {
            Toast.makeText(context, "Selecciona una ficha", Toast.LENGTH_SHORT).show()
        }
    }
}
