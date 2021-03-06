package es.iessaladillo.esthercastaneda.mastermind.data.entity

enum class GameSettings(val numChips: Int, val numRounds: Int, val repeatColor: Boolean) {
    EASY(4, 10, false),
    MEDIUM(5, 15, true),
    HARD(6,20, true)
}