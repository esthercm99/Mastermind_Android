package es.iessaladillo.esthercastaneda.mastermind.ui.game.inGame

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Combination
import es.iessaladillo.esthercastaneda.mastermind.data.entity.GameSettings
import kotlinx.android.extensions.LayoutContainer

class GameAdapter() : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    private lateinit var gameMode: GameSettings
    private var combinationList: List<Combination> = emptyList()
    private var combinationListBN: List<Combination> = emptyList()

    override fun getItemCount(): Int = combinationList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val combination = combinationList[position]
        val combinationBN = combinationListBN[position]
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
                gameMode = GameSettings.NORMAL
                itemView = layoutInflater.inflate(R.layout.round_normal_item, parent, false)
            }
            2 -> {
                gameMode = GameSettings.HARD
                itemView = layoutInflater.inflate(R.layout.round_hard_item, parent, false)
            }
        }
        return ViewHolder(itemView)
    }

    fun submitCombinationList(newList: List<Combination>) {
        combinationList = newList
        notifyDataSetChanged()
    }

    fun submitCombinationBNList(newList: List<Combination>) {
        combinationListBN = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val ficha01: ImageView = containerView.findViewById(R.id.ficha01)
        private val ficha02: ImageView = containerView.findViewById(R.id.ficha02)
        private val ficha03: ImageView = containerView.findViewById(R.id.ficha03)
        private val ficha04: ImageView = containerView.findViewById(R.id.ficha04)
        private lateinit var ficha05: ImageView
        private lateinit var ficha06: ImageView

        private val ficha01BN: ImageView = containerView.findViewById(R.id.fichaByN01)
        private val ficha02BN: ImageView = containerView.findViewById(R.id.fichaByN02)
        private val ficha03BN: ImageView = containerView.findViewById(R.id.fichaByN03)
        private val ficha04BN: ImageView = containerView.findViewById(R.id.fichaByN04)
        private lateinit var ficha05BN: ImageView
        private lateinit var ficha06BN: ImageView

        fun bind(combination: Combination, combinationBN: Combination) {

            combination.run {
                ficha01.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[0].color), null)
                ficha02.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[1].color), null)
                ficha03.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[2].color), null)
                ficha04.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[3].color), null)

                when(gameMode) {
                    GameSettings.NORMAL -> {
                        ficha05 = containerView.findViewById(R.id.ficha05)
                        ficha05.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[4].color), null)
                    }
                    GameSettings.HARD -> {
                        ficha05 = containerView.findViewById(R.id.ficha05)
                        ficha06 = containerView.findViewById(R.id.ficha06)
                        ficha05.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[4].color), null)
                        ficha06.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[5].color), null)
                    }
                }
            }

            combinationBN.run {
                ficha01BN.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[0].color), null)
                ficha02BN.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[1].color), null)
                ficha03BN.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[2].color), null)
                ficha04BN.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[3].color), null)

                when(gameMode) {
                    GameSettings.NORMAL -> {
                        ficha05BN = containerView.findViewById(R.id.fichaByN05)
                        ficha05BN.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[4].color), null)
                    }
                    GameSettings.HARD -> {
                        ficha05BN = containerView.findViewById(R.id.fichaByN05)
                        ficha06BN = containerView.findViewById(R.id.fichaByN06)

                        ficha05BN.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[4].color), null)
                        ficha06BN.background = ResourcesCompat.getDrawable(containerView.resources, checkEmptyChips(chips[5].color), null)
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

    }
}
