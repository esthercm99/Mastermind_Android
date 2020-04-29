package es.iessaladillo.esthercastaneda.mastermind.data.entity

import java.util.*
import es.iessaladillo.esthercastaneda.mastermind.R

class IA(name: String) : Player(name) {

    fun createSecretCombination(gameSettings: GameSettings): Combination {

        val combination: Combination

        when(gameSettings) {

            GameSettings.EASY -> combination = Combination(arrayOf(   Chip(R.drawable.chip_orange, 1),
                Chip(R.drawable.chip_orange, 2),
                Chip(R.drawable.chip_orange, 3),
                Chip(R.drawable.chip_orange, 4)))

            GameSettings.NORMAL -> combination = Combination(arrayOf(   Chip(R.drawable.chip_orange, 1),
                Chip(R.drawable.chip_orange, 2),
                Chip(R.drawable.chip_orange, 3),
                Chip(R.drawable.chip_orange, 4),
                Chip(R.drawable.chip_orange, 5)))

            GameSettings.HARD -> combination = Combination(arrayOf(   Chip(R.drawable.chip_orange, 1),
                Chip(R.drawable.chip_orange, 2),
                Chip(R.drawable.chip_orange, 3),
                Chip(R.drawable.chip_orange, 4),
                Chip(R.drawable.chip_orange, 5),
                Chip(R.drawable.chip_orange, 6)))
        }

        return combination
        /*
        return Combination(arrayOf( Chip(chooseColor(), 1),
                                    Chip(chooseColor(), 2),
                                    Chip(chooseColor(), 3),
                                    Chip(chooseColor(), 4)))

         */
    }

    private fun chooseColor(): Int {
        val random = Random()
        val numRnd = random.nextInt(8)
        var color: Int = -1

        when (numRnd) {
            0 -> color = R.drawable.chip_red
            1 -> color = R.drawable.chip_yellow
            2 -> color = R.drawable.chip_green
            3 -> color = R.drawable.chip_blue
            4 -> color = R.drawable.chip_brown
            5 -> color = R.drawable.chip_grey
            6 -> color = R.drawable.chip_purple
            7 -> color = R.drawable.chip_orange
        }

        return color
    }
}