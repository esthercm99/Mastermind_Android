package es.iessaladillo.esthercastaneda.mastermind.ui.game.singleplayerGame

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Combination
import es.iessaladillo.esthercastaneda.mastermind.data.entity.GameSettings
import kotlinx.android.synthetic.main.game_fragment.*
import kotlinx.android.synthetic.main.main_activity.*


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
        isColorBlindMode()
        setupBtnSelect()
        lblRound.text = String.format("%s %d", getString(R.string.lblround), viewModel.round)
        putColorOnChipSelected()
        selectColorChip()
        setupButtons()
    }

    private fun setupBtnSelect() {
        if (viewModel.gameSettings == GameSettings.NORMAL) {
            chip05.visibility = View.VISIBLE
        } else if (viewModel.gameSettings == GameSettings.HARD) {
            chip05.visibility = View.VISIBLE
            chip06.visibility = View.VISIBLE
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
        viewModel.currentColorId = -1
        viewModel.resetCurrentCombination()
        resetSelected()

        resetChipSelected(chip01)
        resetChipSelected(chip02)
        resetChipSelected(chip03)
        resetChipSelected(chip04)
        resetChipSelected(chip05)
        resetChipSelected(chip06)

        viewModel.round++
        lblRound.text = String.format("%s %d", getString(R.string.lblround), viewModel.round)
    }

    private fun resetChipSelected(btn: Button) {
        btn.background = context?.getDrawable(R.drawable.ficha_vacia)
        btn.text = ""
    }
    private fun putColorOnChipSelected() {
        chip01.setOnClickListener { changeColorSelected(it.id) }
        chip02.setOnClickListener { changeColorSelected(it.id) }
        chip03.setOnClickListener { changeColorSelected(it.id) }
        chip04.setOnClickListener { changeColorSelected(it.id) }
        chip05.setOnClickListener { changeColorSelected(it.id) }
        chip06.setOnClickListener { changeColorSelected(it.id) }
    }
    private fun selectColorChip() {
        redChip.setOnClickListener { resetAndSelectChip(it, R.drawable.chip_red_selected, R.drawable.chip_red,1 ) }
        greenChip.setOnClickListener { resetAndSelectChip(it, R.drawable.chip_green_selected, R.drawable.chip_green, 2) }
        chipBlue.setOnClickListener { resetAndSelectChip(it, R.drawable.chip_blue_selected, R.drawable.chip_blue, 3) }
        chipYellow.setOnClickListener { resetAndSelectChip(it, R.drawable.chip_yellow_selected, R.drawable.chip_yellow, 4) }
        chipOrange.setOnClickListener { resetAndSelectChip(it, R.drawable.chip_orange_selected, R.drawable.chip_orange, 5) }
        chipPurple.setOnClickListener { resetAndSelectChip(it, R.drawable.chip_purple_selected, R.drawable.chip_purple, 6) }
        chipBrown.setOnClickListener { resetAndSelectChip(it, R.drawable.chip_brown_selected, R.drawable.chip_brown, 7) }
        chipGrey.setOnClickListener { resetAndSelectChip(it, R.drawable.chip_grey_selected, R.drawable.chip_grey, 8) }
    }
    private fun resetAndSelectChip(btn: View, colorSelect: Int, actualColor: Int, number: Int) {
        resetSelected()
        btn.background = context?.getDrawable(colorSelect)
        viewModel.currentColorId = actualColor
        viewModel.currentNumberColor = number
    }
    private fun resetSelected() {
        redChip.background = context?.getDrawable(R.drawable.chip_red)
        chipOrange.background = context?.getDrawable(R.drawable.chip_orange)
        chipBlue.background = context?.getDrawable(R.drawable.chip_blue)
        chipYellow.background = context?.getDrawable(R.drawable.chip_yellow)
        greenChip.background = context?.getDrawable(R.drawable.chip_green)
        chipPurple.background = context?.getDrawable(R.drawable.chip_purple)
        chipBrown.background = context?.getDrawable(R.drawable.chip_brown)
        chipGrey.background = context?.getDrawable(R.drawable.chip_grey)
    }
    private fun changeColorSelected(buttonId: Int) {

        val arrayChipsBtn = arrayOf(chip01.id, chip02.id,
                                                chip03.id, chip04.id,
                                                chip05.id, chip06.id)

        if (viewModel.currentColorId != -1) {

            val btn = view?.findViewById<Button>(buttonId)
            btn?.background = context?.getDrawable(viewModel.currentColorId)

            if (viewModel.colorBlindMode) {
                btn?.text = viewModel.currentNumberColor.toString()
            }

            for (i in 0 until arrayChipsBtn.size) {
                if (arrayChipsBtn[i] == buttonId) {
                    viewModel.currentCombination.chips[i].color = viewModel.currentColorId
                    viewModel.currentCombination.chips[i].position = i+1
                }
            }
        } else {
            Toast.makeText(context, getString(R.string.lblSelectColor), Toast.LENGTH_SHORT).show()
        }
    }

    private fun isColorBlindMode() {
        if (viewModel.colorBlindMode) {
            setupColorBlind(redChip, R.drawable.chip_red,1)
            setupColorBlind(greenChip, R.drawable.chip_green, 2)
            setupColorBlind(chipBlue, R.drawable.chip_blue, 3)
            setupColorBlind(chipYellow, R.drawable.chip_yellow, 4)
            setupColorBlind(chipOrange, R.drawable.chip_orange, 5)
            setupColorBlind(chipPurple, R.drawable.chip_purple, 6)
            setupColorBlind(chipBrown, R.drawable.chip_brown, 7)
            setupColorBlind(chipGrey, R.drawable.chip_grey, 8)
        }
    }
    private fun setupColorBlind(btn: Button, color: Int, number: Int) {
        resetSelected()
        btn.background = context?.getDrawable(color)
        btn.text = number.toString()
    }
}
