package es.iessaladillo.esthercastaneda.mastermind.ui.game.inGame

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Chip
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Combination
import es.iessaladillo.esthercastaneda.mastermind.data.entity.GameSettings
import es.iessaladillo.esthercastaneda.mastermind.data.entity.IA

class GameViewModel(private val application: Application) : ViewModel() {

    var currentChipId : Int = -1
    var round: Int = 1
    var currentCombination = Combination(arrayOf(Chip(), Chip(), Chip(), Chip()))
    private val combinationIA = IA().createSecretCombination()
    private var gameSettings = GameSettings.EASY

    private var combinationBNList: MutableList<Combination> = mutableListOf()
    private val _listCombinationBN : MutableLiveData<List<Combination>> = MutableLiveData()
    val listCombinationBN : LiveData<List<Combination>>
        get() = _listCombination


    private var combinationList: MutableList<Combination> = mutableListOf()
    private val _listCombination : MutableLiveData<List<Combination>> = MutableLiveData()
    val listCombination : LiveData<List<Combination>>
        get() = _listCombination


    fun addCombination(){
        currentCombination.let {
            combinationList.add(it)
            _listCombination.value = combinationList
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

                    if (black == false && (white == false || white == null)) {
                        totalWhite++
                        whiteMap[i] = true
                    }

                }
            }
        }

        Toast.makeText(application.applicationContext, String.format("Blancas: %d | Negras: %d", totalWhite, totalBlack), Toast.LENGTH_LONG).show()
    }

}
