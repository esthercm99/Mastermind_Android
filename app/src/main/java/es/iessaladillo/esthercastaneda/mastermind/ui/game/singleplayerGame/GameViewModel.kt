package es.iessaladillo.esthercastaneda.mastermind.ui.game.singleplayerGame

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
    var currentChipId : Int = -1
    lateinit var gameSettings: GameSettings

    lateinit var currentCombination: Combination

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

        // Toast.makeText(application.applicationContext, String.format(gameSettings.name), Toast.LENGTH_LONG).show()
    }

    // Jugador 1:
    private val player = Player("Scole")
    private val _listCombinationBN : MutableLiveData<List<Combination>> = MutableLiveData()
    val listCombinationBN : LiveData<List<Combination>>
        get() = _listCombinationBN
    private val _listCombination : MutableLiveData<List<Combination>> = MutableLiveData()
    val listCombination01 : LiveData<List<Combination>>
        get() = _listCombination

    private val combinationIA = IA("IA").createSecretCombination(gameSettings)

    fun getWinner() = player.isWinner()

    fun addCombination(){
        currentCombination.let {
            player.addCombination(it)
            _listCombination.value = player.getCombinationList()
        }
        checkCombination()
    }
    private fun checkCombination() {
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
                blackMap[i] = true
            } else {
                blackMap[i] = false
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
                    if (black == false && (white == false || white == null)) {
                        totalWhite++
                        whiteMap[i] = true
                    }

                }
            }
        }

        var index = 0
        lateinit var combinationBN: Combination

        when (gameSettings) {
            GameSettings.EASY -> {
                combinationBN = Combination(arrayOf(
                    Chip(-1, 0),
                    Chip(-1, 1),
                    Chip(-1, 2),
                    Chip(-1, 3)))
            }
            GameSettings.NORMAL -> {
                combinationBN = Combination(arrayOf(
                    Chip(-1, 0),
                    Chip(-1, 1),
                    Chip(-1, 2),
                    Chip(-1, 3),
                    Chip(-1, 4)))
            }
            GameSettings.HARD -> {
                combinationBN = Combination(arrayOf(
                    Chip(-1, 0),
                    Chip(-1, 1),
                    Chip(-1, 2),
                    Chip(-1, 3),
                    Chip(-1, 4),
                    Chip(-1, 5)))
            }
        }

        for (i in 0 until totalBlack) {
            combinationBN.chips[index] = Chip(R.drawable.chip_black, index)
            index++
        }

        for (i in 0 until totalWhite) {
            combinationBN.chips[index] = Chip(R.drawable.chip_white, index)
            index++
        }

        player.addCombinationBN(combinationBN)
        _listCombinationBN.value = player.getCombinationBNList()

        if (totalBlack.toInt() == gameSettings.numChips) {
            player.setWinner(true)
        }
        // Toast.makeText(application.applicationContext, String.format("Blancas: %d | Negras: %d", totalWhite, totalBlack), Toast.LENGTH_LONG).show()
    }

    fun resetCurrentCombination() {
        when (gameSettings) {
            GameSettings.EASY -> currentCombination = Combination(arrayOf(Chip(), Chip(), Chip(), Chip()))
            GameSettings.NORMAL -> currentCombination = Combination(arrayOf(Chip(), Chip(), Chip(), Chip(), Chip()))
            GameSettings.HARD -> currentCombination = Combination(arrayOf(Chip(), Chip(), Chip(), Chip(), Chip(), Chip()))
        }
    }
}