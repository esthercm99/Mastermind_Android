package es.iessaladillo.esthercastaneda.mastermind.data.entity

open class Player (val name: String) {

    private var isWinner: Boolean = false
    private var combinationList: MutableList<Combination> = mutableListOf()
    private var combinationBNList: MutableList<Combination> = mutableListOf()

    fun isWinner() = isWinner
    fun getCombinationList() = combinationList
    fun getCombinationBNList() = combinationBNList

    fun setWinner(isWinner: Boolean) { this.isWinner = isWinner }
    fun addCombination(combination: Combination) = combinationList.add(combination)
    fun addCombinationBN(combination: Combination) = combinationBNList.add(combination)

}