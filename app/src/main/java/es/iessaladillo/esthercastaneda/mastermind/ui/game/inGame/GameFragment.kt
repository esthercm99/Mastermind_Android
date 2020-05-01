package es.iessaladillo.esthercastaneda.mastermind.ui.game.inGame

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.multigame_fragment.*
import kotlinx.android.synthetic.main.singlegame_fragment.btnCheck
import kotlinx.android.synthetic.main.singlegame_fragment.btnExit
import kotlinx.android.synthetic.main.singlegame_fragment.chip01
import kotlinx.android.synthetic.main.singlegame_fragment.chip02
import kotlinx.android.synthetic.main.singlegame_fragment.chip03
import kotlinx.android.synthetic.main.singlegame_fragment.chip04
import kotlinx.android.synthetic.main.singlegame_fragment.chip05
import kotlinx.android.synthetic.main.singlegame_fragment.chip06
import kotlinx.android.synthetic.main.singlegame_fragment.chipBlue
import kotlinx.android.synthetic.main.singlegame_fragment.chipBrown
import kotlinx.android.synthetic.main.singlegame_fragment.chipGrey
import kotlinx.android.synthetic.main.singlegame_fragment.chipOrange
import kotlinx.android.synthetic.main.singlegame_fragment.chipPurple
import kotlinx.android.synthetic.main.singlegame_fragment.chipYellow
import kotlinx.android.synthetic.main.singlegame_fragment.greenChip
import kotlinx.android.synthetic.main.singlegame_fragment.lblRound
import kotlinx.android.synthetic.main.singlegame_fragment.lstRounds
import kotlinx.android.synthetic.main.singlegame_fragment.redChip


class GameFragment : Fragment() {

    private val gameAdapter = GameAdapter()
    private val gameAdapterIA = GameAdapter()

    private val viewModel: GameViewModel by viewModels {
        GameViewModelFactory(requireActivity().application)
    }

    private val navController: NavController by lazy {
        NavHostFragment.findNavController(navHostFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(viewModel.modePlayer == 0) {
            return inflater.inflate(R.layout.singlegame_fragment, container, false)
        } else {
            return inflater.inflate(R.layout.multigame_fragment, container, false)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecyclerView()
        setupViews()
    }

    // Player 01:
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
        viewModel.listCombinationBN01.observe(this) {
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

    // Player 02:
    private fun setupRecyclerViewIA() {
        lstRoundsIA.run {
            setHasFixedSize(true)
            adapter = gameAdapterIA
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            itemAnimator = DefaultItemAnimator()
        }
        observeIA()
    }
    private fun observeIA() {
        viewModel.listCombination02.observe(this) {
            updateCombinationListIA(it)
        }
        viewModel.listCombinationBN02.observe(this) {
            updateCombinationBNListIA(it)
        }
    }
    private fun updateCombinationListIA(newList: List<Combination>) {
        lstRoundsIA.post {
            gameAdapterIA.submitCombinationList(newList)
        }
    }
    private fun updateCombinationBNListIA(newList: List<Combination>) {
        lstRoundsIA.post {
            gameAdapterIA.submitCombinationBNList(newList)
        }
    }

    // Setup Views:
    private fun setupViews() {
        isColorBlindMode()
        if (viewModel.modePlayer == 1) {
            setupMultiplayerViews()
        }
        setupBtnSelect()
        lblRound.text = String.format("%s %d", getString(R.string.lblround), viewModel.round)
        putColorOnChipSelected()
        selectColorChip()
        setupButtons()
    }
    private fun setupMultiplayerViews() {
        namePlayer1.text = viewModel.player01.name
        setupRecyclerViewIA()
    }
    private fun roundIA() {
        viewModel.playRoundIA()
        viewModel.resetCurrentCombination()
    }

    private fun checkWinner() {
        if (viewModel.round > viewModel.gameSettings.numRounds) {
            finishGame()
        } else if(viewModel.getWinner01()) {
            finishGame()
        }
    }
    private fun nextRound() {
        observe()
        viewModel.currentColorId = -1
        viewModel.resetCurrentCombination()
        resetSelected()

        resetChipSelected(chip01)
        resetChipSelected(chip02)
        resetChipSelected(chip03)
        resetChipSelected(chip04)
        resetChipSelected(chip05)
        resetChipSelected(chip06)

        if(viewModel.modePlayer == 1) {
            roundIA()
            observeIA()
        }

        viewModel.round++
        lblRound.text = String.format("%s %d", getString(R.string.lblround), viewModel.round)
    }
    private fun finishGame() {
        val settings: SharedPreferences by lazy {
            PreferenceManager.getDefaultSharedPreferences(activity)
        }

        settings.edit {
            putBoolean("isWinner", viewModel.getWinner01())
        }
        navController.navigate(R.id.resultFragment)
    }

    // Setting buttons
    private fun setupBtnSelect() {
        if (viewModel.gameSettings == GameSettings.NORMAL) {
            chip05.visibility = View.VISIBLE
        } else if (viewModel.gameSettings == GameSettings.HARD) {
            chip05.visibility = View.VISIBLE
            chip06.visibility = View.VISIBLE
        }
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
                viewModel.addCombination(viewModel.player01)
                nextRound()
                checkWinner()
            } else {
                Toast.makeText(context, getString(R.string.lblFill), Toast.LENGTH_SHORT).show()
            }
        }
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
