package es.iessaladillo.esthercastaneda.mastermind.data.entity

enum class GameSettings(val numChips: Int, val numRounds: Int) {
    EASY(4, 10), NORMAL(5, 15), HARD(8,20)
}