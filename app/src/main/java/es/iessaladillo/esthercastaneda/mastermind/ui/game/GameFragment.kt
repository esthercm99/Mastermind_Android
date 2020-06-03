package es.iessaladillo.esthercastaneda.mastermind.ui.game

import android.app.AlertDialog
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
import es.iessaladillo.esthercastaneda.mastermind.data.bbdd.DatabaseUser
import es.iessaladillo.esthercastaneda.mastermind.data.bbdd.Game
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Combination
import es.iessaladillo.esthercastaneda.mastermind.data.entity.GameSettings
import es.iessaladillo.esthercastaneda.mastermind.data.entity.PlayCombinations
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Player
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.multigame_fragment.*
import kotlinx.android.synthetic.main.singlegame_fragment.btnCheck
import kotlinx.android.synthetic.main.singlegame_fragment.btnExit
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
import kotlin.concurrent.thread


class GameFragment : Fragment() {

    private val gameAdapter = GameAdapter()
    private val gameAdapterIA = GameAdapter()

    private val viewModel: GameViewModel by viewModels {
        GameViewModelFactory(requireActivity().application)
    }
    private val navController: NavController by lazy {
        NavHostFragment.findNavController(navHostFragment)
    }
    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
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
        observe()
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
        viewModel.listPlayer01.observe(viewLifecycleOwner) {
            updateCombinationList(it)
        }
    }
    private fun updateCombinationList(newList: PlayCombinations) {
        lstRounds.post {
            gameAdapter.submitCombinationList(newList)
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
        viewModel.listPlayer02.observe(viewLifecycleOwner) {
            updateCombinationListIA(it)
        }
    }
    private fun updateCombinationListIA(newList: PlayCombinations) {
        lstRoundsIA.post {
            gameAdapterIA.submitCombinationList(newList)
        }
    }

    // Setup Views:
    private fun setupViews() {
        isColorBlindMode()
        viewModel.resetCurrentCombination()
        if (viewModel.modePlayer == 1) {
            setupMultiplayerViews()
            observeIA()
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
        checkWinner(viewModel.player02)
    }

    private fun checkWinner(player: Player) {
        if (viewModel.round > viewModel.gameSettings.numRounds) {
            finishGame()
        } else if(player.isWinner()) {
            finishGame()
            Toast.makeText(context, String.format(getString(R.string.youWin), player.name), Toast.LENGTH_SHORT).show()
        }
    }
    private fun nextRound() {
        viewModel.currentColorId = R.drawable.chip_empty
        viewModel.resetCurrentCombination()
        resetSelected()

        resetChipSelected(chip01select)
        resetChipSelected(chip02select)
        resetChipSelected(chip03select)
        resetChipSelected(chip04select)
        resetChipSelected(chip05select)
        resetChipSelected(chip06select)

        if(viewModel.modePlayer == 1) {
            roundIA()
            observeIA()
        }

        viewModel.round++
        lblRound.text = String.format("%s %d", getString(R.string.lblround), viewModel.round)
    }
    private fun finishGame() {
        settings.edit {
            putBoolean("isWinner", viewModel.getWinner01())
        }

        thread {
            val idUser = settings.getLong(getString(R.string.key_currentIdUser), -1L)
            val difficult = viewModel.gameSettings.name.toLowerCase()
            val modeGame: String

            if (viewModel.modePlayer == 0) {
                modeGame = "s"
            } else {
                modeGame = "m"
            }

            if (viewModel.getWinner01()) {
                DatabaseUser.getInstance(requireContext()).gameDao.insertGame(Game(0, idUser, viewModel.round, difficult, modeGame, "w"))
            } else {
                DatabaseUser.getInstance(requireContext()).gameDao.insertGame(Game(0, idUser, viewModel.round, difficult, modeGame, "l"))
            }
        }.join()

        navController.navigate(R.id.action_resultFragment_to_homeFragment)
    }

    // Setting buttons
    private fun setupBtnSelect() {
        if (viewModel.gameSettings == GameSettings.MEDIUM) {
            chip05select.visibility = View.VISIBLE
        } else if (viewModel.gameSettings == GameSettings.HARD) {
            chip05select.visibility = View.VISIBLE
            chip06select.visibility = View.VISIBLE
        }
    }
    private fun setupButtons() {

        btnExit.setOnClickListener {
            AlertDialog.Builder(context).setTitle(getString(R.string.quitGame))
                .setMessage(getString(R.string.msgQuitGame))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.txtYes)) { _, _ -> navController.navigateUp() }
                .setNegativeButton(getString(R.string.txtNo)){ _, _ -> }
                .show()
        }

        btnCheck.setOnClickListener {
            var emptyChip = false

            for (element in viewModel.currentCombination.chips) {
                if (element.color == R.drawable.chip_empty) {
                    emptyChip = true
                }
            }

            if(!emptyChip) {
                viewModel.addCombination(viewModel.player01)
                nextRound()
                checkWinner(viewModel.player01)
            } else {
                Toast.makeText(context, getString(R.string.lblFill), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun resetChipSelected(btn: Button) {
        btn.background = context?.getDrawable(R.drawable.chip_empty)
        btn.text = ""
    }
    private fun putColorOnChipSelected() {
        chip01select.setOnClickListener { changeColorSelected(it.id) }
        chip02select.setOnClickListener { changeColorSelected(it.id) }
        chip03select.setOnClickListener { changeColorSelected(it.id) }
        chip04select.setOnClickListener { changeColorSelected(it.id) }
        chip05select.setOnClickListener { changeColorSelected(it.id) }
        chip06select.setOnClickListener { changeColorSelected(it.id) }
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

        val arrayChipsBtn = arrayOf(chip01select.id, chip02select.id,
                                                chip03select.id, chip04select.id,
                                                chip05select.id, chip06select.id)

        if (viewModel.currentColorId != R.drawable.chip_empty) {

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
