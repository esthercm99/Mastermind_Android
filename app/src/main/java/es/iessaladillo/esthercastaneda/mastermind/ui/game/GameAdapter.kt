package es.iessaladillo.esthercastaneda.mastermind.ui.game

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Combination
import es.iessaladillo.esthercastaneda.mastermind.data.entity.GameSettings
import es.iessaladillo.esthercastaneda.mastermind.data.entity.PlayCombinations
import kotlinx.android.extensions.LayoutContainer
import kotlin.properties.Delegates

class GameAdapter() : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    private lateinit var gameMode: GameSettings
    private var colorBlindMode by Delegates.notNull<Boolean>()

    private var combinationsList: PlayCombinations = PlayCombinations(mutableListOf(), mutableListOf())

    override fun getItemCount(): Int = combinationsList.combinations.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val combination = combinationsList.combinations[position]
        val combinationBN = combinationsList.combinationsBN[position]
        holder.bind(combination, combinationBN)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        lateinit var itemView: View
        val settings: SharedPreferences by lazy {
            PreferenceManager.getDefaultSharedPreferences(parent.context)
        }
        val layoutInflater = LayoutInflater.from(parent.context)

        when(settings.getInt("difficultGame", -1)) {
            0 -> {
                gameMode = GameSettings.EASY
                itemView = layoutInflater.inflate(R.layout.round_easy_item, parent, false)
            }
            1 -> {
                gameMode = GameSettings.MEDIUM
                itemView = layoutInflater.inflate(R.layout.round_normal_item, parent, false)
            }
            2 -> {
                gameMode = GameSettings.HARD
                itemView = layoutInflater.inflate(R.layout.round_hard_item, parent, false)
            }
        }

        colorBlindMode = settings.getBoolean("prefColorBlindMode", true)

        return ViewHolder(itemView)
    }

    fun submitCombinationList(newList: PlayCombinations) {
        combinationsList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val chip01t: TextView = containerView.findViewById(R.id.chip01)
        private val chip02t: TextView = containerView.findViewById(R.id.chip02)
        private val chip03t: TextView = containerView.findViewById(R.id.chip03)
        private val chip04t: TextView = containerView.findViewById(R.id.chip04)
        private lateinit var chip05t: TextView
        private lateinit var chip06t: TextView

        private val chipt01BN: ImageView = containerView.findViewById(R.id.chipBN01)
        private val chipt02BN: ImageView = containerView.findViewById(R.id.chipBN02)
        private val chipt03BN: ImageView = containerView.findViewById(R.id.chipBN03)
        private val chipt04BN: ImageView = containerView.findViewById(R.id.chipBN04)
        private lateinit var chipt05BN: ImageView
        private lateinit var chipt06BN: ImageView

        fun bind(combination: Combination, combinationBN: Combination) {

            combination.run {
                setChip(chip01t, chips[0].color)
                setChip(chip02t, chips[1].color)
                setChip(chip03t, chips[2].color)
                setChip(chip04t, chips[3].color)

                when(gameMode) {
                    GameSettings.MEDIUM -> {
                        chip05t = containerView.findViewById(R.id.chip05)
                        setChip(chip05t, chips[4].color)
                    }
                    GameSettings.HARD -> {
                        chip05t = containerView.findViewById(R.id.chip05)
                        chip06t = containerView.findViewById(R.id.chip06)
                        setChip(chip05t, chips[4].color)
                        setChip(chip06t, chips[5].color)
                    }
                }
            }

            combinationBN.run {
                chipt01BN.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[0].color), null)
                chipt02BN.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[1].color), null)
                chipt03BN.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[2].color), null)
                chipt04BN.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[3].color), null)

                when(gameMode) {
                    GameSettings.MEDIUM -> {
                        chipt05BN = containerView.findViewById(R.id.chipBN05)
                        chipt05BN.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[4].color), null)
                    }
                    GameSettings.HARD -> {
                        chipt05BN = containerView.findViewById(R.id.chipBN05)
                        chipt06BN = containerView.findViewById(R.id.chipBN06)

                        chipt05BN.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[4].color), null)
                        chipt06BN.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[5].color), null)
                    }
                }
            }
        }

        private fun checkEmptyChips(chipColor: Int): Int {
            val color: Int

            if(chipColor != -1) {
                color = chipColor
            } else {
                color = R.drawable.chip_empty
            }

            return color
        }
        private fun setChip(chip: TextView, color: Int) {
            chip.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(color), null)
            if (colorBlindMode) {
                chip.text = checkColorNumber(color)
            }
        }

        private fun checkColorNumber(color: Int): String {
            var number: Int = -1

            when(color) {
                R.drawable.chip_red -> number = 1
                R.drawable.chip_green -> number = 2
                R.drawable.chip_blue -> number = 3
                R.drawable.chip_yellow -> number = 4
                R.drawable.chip_orange -> number = 5
                R.drawable.chip_purple -> number = 6
                R.drawable.chip_brown -> number = 7
                R.drawable.chip_grey -> number = 8
            }

            return number.toString()
        }
    }
}
