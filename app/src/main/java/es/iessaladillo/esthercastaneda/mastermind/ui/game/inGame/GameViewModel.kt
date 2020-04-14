package es.iessaladillo.esthercastaneda.mastermind.ui.game.inGame

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Combination

class GameViewModel(private val application: Application) : ViewModel() {

    var currentChipId : Int = -1
    var currentCombination = Combination(-1, -1, -1, -1)

    private var combinationList: MutableList<Combination> = mutableListOf()
    private val _listCombination : MutableLiveData<List<Combination>> = MutableLiveData()
    val listCombination : LiveData<List<Combination>>
        get() = _listCombination


    fun addCombination(){
        combinationList.add(currentCombination)
        _listCombination.value = combinationList
    }

}
