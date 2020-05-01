package es.iessaladillo.esthercastaneda.mastermind.data.entity

import java.util.*
import es.iessaladillo.esthercastaneda.mastermind.R

class IA(name: String) : Player(name) {

    fun createSecretCombination(gameSettings: GameSettings): Combination {
        return createCombination(gameSettings)
    }

    private fun createCombination(gameSettings: GameSettings): Combination {

        val combination: Combination

        when(gameSettings) {

            GameSettings.EASY -> combination = Combination(arrayOf(   Chip(R.drawable.chip_empty, 1),
                Chip(R.drawable.chip_empty, 2),
                Chip(R.drawable.chip_empty, 3),
                Chip(R.drawable.chip_empty, 4)))

            GameSettings.NORMAL -> combination = Combination(arrayOf(   Chip(R.drawable.chip_empty, 1),
                Chip(R.drawable.chip_empty, 2),
                Chip(R.drawable.chip_empty, 3),
                Chip(R.drawable.chip_empty, 4),
                Chip(R.drawable.chip_empty, 5)))

            GameSettings.HARD -> combination = Combination(arrayOf(  Chip(R.drawable.chip_empty, 1),
                Chip(R.drawable.chip_empty, 2),
                Chip(R.drawable.chip_empty, 3),
                Chip(R.drawable.chip_empty, 4),
                Chip(R.drawable.chip_empty, 5),
                Chip(R.drawable.chip_empty, 6)))
        }

        for (i in 0 until combination.chips.size) {

            combination.chips[i].color = chooseColor()

            if(!gameSettings.repeatColor) {
                do {
                    var exit = true
                    for (j in 0 until combination.chips.size) {
                        if (combination.chips[i].color == combination.chips[j].color && i!=j) {
                            combination.chips[i].color = chooseColor()
                            exit = false
                        }
                    }
                } while(!exit)
            }

        }

        return combination
    }

    private fun chooseColor(): Int {
        val random = Random()
        val numRnd = random.nextInt(16)
        var color: Int = -1
        val listRepeatColors = arrayOf(0, 7, 4, 1, 1, 0, 3, 2, 3, 5, 2, 6, 7, 6, 4, 5 )

        when (listRepeatColors[numRnd]) {
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