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
import androidx.recyclerview.widget.*
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Combination
import kotlinx.android.synthetic.main.game_fragment.*
import kotlinx.android.synthetic.main.main_activity.*
import androidx.lifecycle.observe
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Chip

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
        // lblRound.text = String.format("%s - %s", settings.getString("difficultGame", "None"), settings.getString("modeGame", "None"))
        setupRecyclerView()
        observe()
        setupViews()
    }

    private fun setupRecyclerView() {
        lstRounds.run {
            setHasFixedSize(true)
            adapter = gameAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            itemAnimator = DefaultItemAnimator()
        }
        observe()
    }

    private fun observe() {
        viewModel.listCombination.observe(this) {
            updateCombinationList(it)
        }
        viewModel.listCombinationBN.observe(this) {
            updateCombinationBNList(it)
        }

    }

    private fun updateCombinationList(newList: List<Combination>) {
        lstRounds.post {
            gameAdapter.submitCombinationList(newList)
        }
    }
    private fun updateCombinationBNList(newList: List<Combination>) {
        lstRounds.post {
            gameAdapter.submitCombinationBNList(newList)
        }
    }

    private fun setupViews() {
        lblRound.text = String.format("RONDA %d", viewModel.round)
        chipSelectCheck()
        changeColor()
        setupButtons()
    }
    private fun setupButtons() {
        btnExit.setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }
        btnCheck.setOnClickListener {
        if (viewModel.currentCombination.chips[0].color != -1 &&
            viewModel.currentCombination.chips[1].color != -1 &&
            viewModel.currentCombination.chips[2].color != -1 &&
            viewModel.currentCombination.chips[3].color != -1) {
                // Toast.makeText(context, "Se ha añadido combinación", Toast.LENGTH_SHORT).show()
                viewModel.addCombination()
                nextRound()
            } else {
                Toast.makeText(context, "Rellena la combinación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun nextRound() {
        viewModel.currentChipId = -1
        viewModel.currentCombination = Combination(arrayOf(Chip(), Chip(), Chip(), Chip()))

        ficha01Select.background = context?.getDrawable(R.drawable.ficha_vacia)
        ficha02Select.background = context?.getDrawable(R.drawable.ficha_vacia)
        ficha03Select.background = context?.getDrawable(R.drawable.ficha_vacia)
        ficha04Select.background = context?.getDrawable(R.drawable.ficha_vacia)

        viewModel.round++
        lblRound.text = String.format("RONDA %d", viewModel.round)
    }

    private fun chipSelectCheck() {
        ficha01Select.setOnClickListener { viewModel.currentChipId = it.id }
        ficha02Select.setOnClickListener { viewModel.currentChipId = it.id }
        ficha03Select.setOnClickListener { viewModel.currentChipId = it.id }
        ficha04Select.setOnClickListener { viewModel.currentChipId = it.id }
    }
    private fun changeColor() {
        fichaRoja.setOnClickListener { changeColorSelected(R.drawable.chip_red) }
        fichaNaranja.setOnClickListener { changeColorSelected(R.drawable.chip_orange) }
        fichaAzul.setOnClickListener { changeColorSelected(R.drawable.chip_blue) }
        fichaAmarilla.setOnClickListener { changeColorSelected(R.drawable.chip_yellow) }
        fichaVerde.setOnClickListener { changeColorSelected(R.drawable.chip_green) }
        fichaMorada.setOnClickListener { changeColorSelected(R.drawable.chip_purple) }
        fichaMarron.setOnClickListener { changeColorSelected(R.drawable.chip_brown) }
        fichaGris.setOnClickListener { changeColorSelected(R.drawable.chip_grey) }
    }
    private fun changeColorSelected(color: Int) {
        if (viewModel.currentChipId != -1) {

            val btn = view?.findViewById<Button>(viewModel.currentChipId)

            // Se comprueba cual el botón que se ha seleccionado para añadirle el color a la combinación actual:
            if(ficha01Select.id == this.viewModel.currentChipId) {
                viewModel.currentCombination.chips[0].color = color
                viewModel.currentCombination.chips[0].position = 1
            } else if(ficha02Select.id == viewModel.currentChipId) {
                viewModel.currentCombination.chips[1].color = color
                viewModel.currentCombination.chips[1].position = 2
            } else if(ficha03Select.id == viewModel.currentChipId) {
                viewModel.currentCombination.chips[2].color = color
                viewModel.currentCombination.chips[2].position = 3
            } else if(ficha04Select.id == viewModel.currentChipId) {
                viewModel.currentCombination.chips[3].color = color
                viewModel.currentCombination.chips[3].position = 4
            }

            btn?.background = context?.getDrawable(color)

        } else {
            Toast.makeText(context, "Selecciona una ficha", Toast.LENGTH_SHORT).show()
        }
    }
}
