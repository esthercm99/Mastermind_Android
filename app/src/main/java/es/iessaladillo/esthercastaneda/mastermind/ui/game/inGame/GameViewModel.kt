package es.iessaladillo.esthercastaneda.mastermind.ui.game.inGame

import android.app.Application
import androidx.lifecycle.ViewModel
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Combination

class GameViewModel(private val application: Application) : ViewModel() {

    var currentChip : Int = -1
    var currentCombination = Combination(-1, -1, -1, -1)

    private val _listCombination : MutableList<Combination> = mutableListOf()
    val listCombination : MutableList<Combination>
        get() = _listCombination

    fun addCombination(combination: Combination){
        _listCombination.add(combination)
    }

}
