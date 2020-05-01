package es.iessaladillo.esthercastaneda.mastermind.ui.game.inGame

import android.app.Application
import android.content.SharedPreferences
import android.widget.Toast
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
    private lateinit var hideCombinationIA: Combination

    // Player 1:
    var player01 : Player
    // Player 2:
    var player02 = IA("IA")

    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }

    init {

        when (settings.getInt("difficultGame", -1)) {
            0 -> {
                gameSettings = GameSettings.EASY
                currentCombination = Combination(arrayOf(Chip(), Chip(), Chip(), Chip()))
            }
            1 -> {
                gameSettings = GameSettings.NORMAL
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
        // Toast.makeText(application.applicationContext, String.format(gameSettings.name), Toast.LENGTH_LONG).show()
    }

    // Player 1:
    private val _listCombinationBN01 : MutableLiveData<List<Combination>> = MutableLiveData()
    val listCombinationBN01 : LiveData<List<Combination>>
        get() = _listCombinationBN01
    private val _listCombination01 : MutableLiveData<List<Combination>> = MutableLiveData()
    val listCombination01 : LiveData<List<Combination>>
        get() = _listCombination01

    // Player IA:
    private var combinationIA = player02.createSecretCombination(gameSettings)
    private val _listCombinationBN02: MutableLiveData<List<Combination>> = MutableLiveData()
    val listCombinationBN02 : LiveData<List<Combination>>
        get() = _listCombinationBN02
    private val _listCombination02 : MutableLiveData<List<Combination>> = MutableLiveData()
    val listCombination02 : LiveData<List<Combination>>
        get() = _listCombination02


    // IA functions:
    fun getWinner02() = player02.isWinner()
    fun playRoundIA() {
        hideCombinationIA = player02.createSecretCombination(gameSettings)
        addCombination()
    }
    private fun addCombination(){
        currentCombination = hideCombinationIA
        currentCombination.let { it ->
            player02.addCombination(it)
            _listCombination02.value = player02.getCombinationList()
        }
        checkCombination(player02)
        _listCombinationBN02.value = player02.getCombinationBNList()
    }

    // Player functions:
    fun getWinner01() = player01.isWinner()
    fun addCombination(player: Player){
        currentCombination.let {
            player.addCombination(it)
            _listCombination01.value = player.getCombinationList()
        }
        checkCombination(player)
        _listCombinationBN01.value = player.getCombinationBNList()
    }

    private fun checkCombination(player: Player) {
        var totalWhite: Byte = 0
        var totalBlack: Byte = 0

        val whiteMap: MutableMap<Int, Boolean> = HashMap()
        val blackMap: MutableMap<Int, Boolean> = HashMap()

        // Comprueba si hay fichas negras:
        for (i in 0 until gameSettings.numChips) {

            // Si son iguales se guarda la posición en la que es negra para luego comprobarlo con las blancas:
            if (combinationIA.chips[i].color == currentCombination.chips[i].color &&
                combinationIA.chips[i].position == currentCombination.chips[i].position) {
                totalBlack++
                blackMap[combinationIA.chips[i].position-1] = true
            } else {
                blackMap[combinationIA.chips[i].position-1] = false
            }
        }

        // Combrueba si hay fichas blancas:
        for (i in 0 until gameSettings.numChips) {

            for (j in 0 until gameSettings.numChips) {

                if (combinationIA.chips[i].color == currentCombination.chips[j].color &&
                    combinationIA.chips[i].position != currentCombination.chips[j].position) {

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

        //totalWhite = (totalWhite - totalBlack).toByte()

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
/*
        if(totalBlack + totalWhite < gameSettings.numChips) {
            for (i in index until gameSettings.numChips) {
                combinationBN.chips[index] = Chip(, index)
                index++
            }
        }
*/
        player.addCombinationBN(combinationBN)

        if (totalBlack.toInt() == gameSettings.numChips) {
            player.setWinner(true)
        }

        // Toast.makeText(application.applicationContext, String.format("Blancas: %d | Negras: %d", totalWhite, totalBlack), Toast.LENGTH_LONG).show()
    }
    private fun getEmptyCombination(): Combination {
        val combination: Combination

        when (gameSettings) {
            GameSettings.EASY -> combination = Combination(arrayOf(Chip(R.drawable.chip_empty, 0), Chip(R.drawable.chip_empty, 1),
                                                                    Chip(R.drawable.chip_empty, 2), Chip(R.drawable.chip_empty, 3)))

            GameSettings.NORMAL -> combination = Combination(arrayOf(Chip(R.drawable.chip_empty, 0), Chip(R.drawable.chip_empty, 1), Chip(R.drawable.chip_empty, 2),
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