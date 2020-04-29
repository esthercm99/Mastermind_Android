package es.iessaladillo.esthercastaneda.mastermind.data.entity

enum class GameSettings(val numChips: Int, val numRounds: Int) {
    EASY(4, 10), NORMAL(5, 10), HARD(6,15)
}