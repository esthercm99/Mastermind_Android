package es.iessaladillo.esthercastaneda.mastermind.data.entity

open class Player (val name: String) {

    private var isWinner: Boolean = false
    private var combinationsList: PlayCombinations = PlayCombinations(mutableListOf(), mutableListOf())

    fun isWinner() = isWinner
    fun getCombinationsList() = combinationsList

    fun addCombinationsList(combination: Combination, combinationBN: Combination) {
        combinationsList.combinations.add(combination)
        combinationsList.combinationsBN.add(combinationBN)
    }
    fun setWinner(isWinner: Boolean) { this.isWinner = isWinner }

}