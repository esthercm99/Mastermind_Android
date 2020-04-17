package es.iessaladillo.esthercastaneda.mastermind.data.entity

import es.iessaladillo.esthercastaneda.mastermind.R

class IA {
    fun createSecretCombination(): Combination {
        return Combination(arrayOf(Chip(R.drawable.chip_red, 1), Chip(R.drawable.chip_yellow, 2),
                           Chip(R.drawable.chip_yellow, 3), Chip(R.drawable.chip_green, 4)))
    }
}