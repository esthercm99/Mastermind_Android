package es.iessaladillo.esthercastaneda.mastermind.ui.game

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.entity.*

class GameViewModel(private val application: Application) : ViewModel() {

    var round: Int = 1
    var modePlayer: Int = 0
    var colorBlindMode: Boolean
    var currentNumberColor: Int = -1
    lateinit var gameSettings: GameSettings
    lateinit var currentCombination: Combination
    var currentColorId : Int = R.drawable.chip_empty
    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }

    // Player 1:
    var player01 : Player
    init {

        when (settings.getInt("difficultGame", -1)) {
            0 -> {
                gameSettings = GameSettings.EASY
                currentCombination = Combination(arrayOf(Chip(), Chip(), Chip(), Chip()))
            }
            1 -> {
                gameSettings = GameSettings.MEDIUM
                currentCombination = Combination(arrayOf(Chip(), Chip(), Chip(), Chip(), Chip()))
            }
            2 -> {
                gameSettings = GameSettings.HARD
                currentCombination = Combination(arrayOf(Chip(), Chip(), Chip(), Chip(), Chip(), Chip()))
            }
        }

        modePlayer = settings.getInt("modeGame", 0)
        colorBlindMode = settings.getBoolean(application.getString(R.string.prefColorBlindMode_key), true)

        val name = settings.getString(application.getString(R.string.prefPlayerName_key), application.getString(R.string.playerName_defaultValue))
        player01 = name?.let { Player(it) }!!
    }

    private val _listCombination01 : MutableLiveData<List<Combination>> = MutableLiveData()
    val listCombination01 : LiveData<List<Combination>>
        get() = _listCombination01
    private val _listCombinationBN01 : MutableLiveData<List<Combination>> = MutableLiveData()
    val listCombinationBN01 : LiveData<List<Combination>>
        get() = _listCombinationBN01

    // Player IA:
    var player02 = IA("IA")
    private val _listCombinationBN02: MutableLiveData<List<Combination>> = MutableLiveData()
    val listCombinationBN02 : LiveData<List<Combination>>
        get() = _listCombinationBN02
    private val _listCombination02 : MutableLiveData<List<Combination>> = MutableLiveData()
    val listCombination02 : LiveData<List<Combination>>
        get() = _listCombination02

    private val hideCombination = player02.createSecretCombination(gameSettings)
    private val hideCombinationIA= player02.createSecretCombination(gameSettings)

    // IA functions:
    fun playRoundIA() = addCombination()
    fun getWinner02() = player02.isWinner()
    private fun addCombination(){
        currentCombination = player02.createCombination(gameSettings)
        currentCombination.let { it ->
            player02.addCombination(it)
            _listCombination02.value = player02.getCombinationList()
        }
        checkCombination(player02, hideCombinationIA)
        _listCombinationBN02.value = player02.getCombinationBNList()
    }

    // Player functions:
    fun getWinner01() = player01.isWinner()
    fun addCombination(player: Player){
        currentCombination.let {
            player.addCombination(it)
            _listCombination01.value = player.getCombinationList()
        }
        checkCombination(player, hideCombination)
        _listCombinationBN01.value = player.getCombinationBNList()
    }

    private fun checkCombination(player: Player, hideCombination: Combination) {
        var totalWhite: Byte = 0
        var totalBlack: Byte = 0

        val whiteMap: MutableMap<Int, Boolean> = HashMap()
        val blackMap: MutableMap<Int, Boolean> = HashMap()

        // Comprueba si hay fichas negras:
        for (i in 0 until gameSettings.numChips) {

            // Si son iguales se guarda la posición en la que es negra para luego comprobarlo con las blancas:
            if (hideCombination.chips[i].color == currentCombination.chips[i].color &&
                hideCombination.chips[i].position == currentCombination.chips[i].position) {
                totalBlack++
                blackMap[hideCombination.chips[i].position-1] = true
            } else {
                blackMap[hideCombination.chips[i].position-1] = false
            }
        }

        // Combrueba si hay fichas blancas:
        for (i in 0 until gameSettings.numChips) {

            for (j in 0 until gameSettings.numChips) {

                if (hideCombination.chips[i].color == currentCombination.chips[j].color &&
                    hideCombination.chips[i].position != currentCombination.chips[j].position) {

                    val black = blackMap[i]
                    val white = whiteMap[i]

                    // Si en la posición de la IA / jugador contrario no hay ninguna ficha en la misma posición
                    // y aun no se ha colocado como ficha blanca se cuenta:
                    if (black == false) {
                        if (white == null || white == false) {
                            totalWhite++
                            whiteMap[i] = true
                        } else {
                            whiteMap[i] = false
                        }
                    } else {
                        whiteMap[i] = false
                    }

                }
            }
        }

        // Se añade la combinación de blancas y negras:
        var index = 0
        val combinationBN: Combination = getEmptyCombination()

        for (i in 0 until totalBlack) {
            combinationBN.chips[index] = Chip(R.drawable.chip_black, index)
            index++
        }

        for (i in 0 until totalWhite) {
            combinationBN.chips[index] = Chip(R.drawable.chip_white, index)
            index++
        }

        player.addCombinationBN(combinationBN)

        if (totalBlack.toInt() == gameSettings.numChips) {
            player.setWinner(true)
        }
    }
    private fun getEmptyCombination(): Combination {
        val combination: Combination

        when (gameSettings) {
            GameSettings.EASY -> combination = Combination(arrayOf(Chip(R.drawable.chip_empty, 0), Chip(R.drawable.chip_empty, 1),
                                                                    Chip(R.drawable.chip_empty, 2), Chip(R.drawable.chip_empty, 3)))

            GameSettings.MEDIUM -> combination = Combination(arrayOf(Chip(R.drawable.chip_empty, 0), Chip(R.drawable.chip_empty, 1), Chip(R.drawable.chip_empty, 2),
                                                                    Chip(R.drawable.chip_empty, 3), Chip(R.drawable.chip_empty, 4)))

            GameSettings.HARD -> combination = Combination(arrayOf(Chip(R.drawable.chip_empty, 0), Chip(R.drawable.chip_empty, 1), Chip(R.drawable.chip_empty, 2),
                                                                    Chip(R.drawable.chip_empty, 3), Chip(R.drawable.chip_empty, 4), Chip(R.drawable.chip_empty, 5)))
        }

        return combination
    }
    fun resetCurrentCombination() {
        currentCombination = getEmptyCombination()
    }
}