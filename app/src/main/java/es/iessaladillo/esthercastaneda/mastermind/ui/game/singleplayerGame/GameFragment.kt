package es.iessaladillo.esthercastaneda.mastermind.ui.game.singleplayerGame

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.*
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Combination
import kotlinx.android.synthetic.main.game_fragment.*
import kotlinx.android.synthetic.main.main_activity.*
import androidx.lifecycle.observe
import androidx.preference.PreferenceManager
import es.iessaladillo.esthercastaneda.mastermind.data.entity.GameSettings

class GameFragment : Fragment(R.layout.game_fragment) {

    private val gameAdapter = GameAdapter()

    private val viewModel: GameViewModel by viewModels {
        GameViewModelFactory(requireActivity().application)
    }

    private val navController: NavController by lazy {
        NavHostFragment.findNavController(navHostFragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
        viewModel.listCombination01.observe(this) {
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
        setupBtnSelect()
        lblRound.text = String.format("%s %d", getString(R.string.lblround), viewModel.round)
        chipSelectCheck()
        changeColor()
        setupButtons()
    }

    private fun setupBtnSelect() {
        if (viewModel.gameSettings == GameSettings.NORMAL) {
            ficha05Select.visibility = View.VISIBLE
        } else if (viewModel.gameSettings == GameSettings.HARD) {
            ficha05Select.visibility = View.VISIBLE
            ficha06Select.visibility = View.VISIBLE
        }
    }
    private fun checkWinner() {
        if (viewModel.round > viewModel.gameSettings.numRounds) {
            finishGame()
        } else if(viewModel.getWinner()) {
            finishGame()
        }
    }
    private fun finishGame() {
        val settings: SharedPreferences by lazy {
            PreferenceManager.getDefaultSharedPreferences(activity)
        }

        settings.edit {
            putBoolean("isWinner", viewModel.getWinner())
        }
        navController.navigate(R.id.resultFragment)
    }
    private fun setupButtons() {

        btnExit.setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }

        btnCheck.setOnClickListener {
            var emptyChip = false

            for (i in 0 until viewModel.currentCombination.chips.size) {
                if (viewModel.currentCombination.chips[i].color == -1) {
                    emptyChip = true
                }
            }

            if(!emptyChip) {
                viewModel.addCombination()
                nextRound()
                checkWinner()
            } else {
                Toast.makeText(context, getString(R.string.lblFill), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun nextRound() {
        viewModel.currentChipId = -1
        viewModel.resetCurrentCombination()

        ficha01Select.background = context?.getDrawable(R.drawable.ficha_vacia)
        ficha02Select.background = context?.getDrawable(R.drawable.ficha_vacia)
        ficha03Select.background = context?.getDrawable(R.drawable.ficha_vacia)
        ficha04Select.background = context?.getDrawable(R.drawable.ficha_vacia)
        ficha05Select.background = context?.getDrawable(R.drawable.ficha_vacia)
        ficha06Select.background = context?.getDrawable(R.drawable.ficha_vacia)

        viewModel.round++
        lblRound.text = String.format("%s %d", getString(R.string.lblround), viewModel.round)
    }
    private fun chipSelectCheck() {
        ficha01Select.setOnClickListener { viewModel.currentChipId = it.id }
        ficha02Select.setOnClickListener { viewModel.currentChipId = it.id }
        ficha03Select.setOnClickListener { viewModel.currentChipId = it.id }
        ficha04Select.setOnClickListener { viewModel.currentChipId = it.id }
        ficha05Select.setOnClickListener { viewModel.currentChipId = it.id }
        ficha06Select.setOnClickListener { viewModel.currentChipId = it.id }
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
            if (ficha01Select.id == this.viewModel.currentChipId) {
                viewModel.currentCombination.chips[0].color = color
                viewModel.currentCombination.chips[0].position = 1

            } else if (ficha02Select.id == viewModel.currentChipId) {
                viewModel.currentCombination.chips[1].color = color
                viewModel.currentCombination.chips[1].position = 2

            } else if (ficha03Select.id == viewModel.currentChipId) {
                viewModel.currentCombination.chips[2].color = color
                viewModel.currentCombination.chips[2].position = 3

            } else if (ficha04Select.id == viewModel.currentChipId) {
                viewModel.currentCombination.chips[3].color = color
                viewModel.currentCombination.chips[3].position = 4

            } else if (ficha05Select.id == viewModel.currentChipId) {
                viewModel.currentCombination.chips[4].color = color
                viewModel.currentCombination.chips[4].position = 5

            } else if (ficha06Select.id == viewModel.currentChipId) {
                viewModel.currentCombination.chips[5].color = color
                viewModel.currentCombination.chips[5].position = 6
            }

            btn?.background = context?.getDrawable(color)

        } else {
            Toast.makeText(context, getString(R.string.lblSelectChip), Toast.LENGTH_SHORT).show()
        }
    }
}