package es.iessaladillo.esthercastaneda.mastermind.ui.game.inGame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Combination
import kotlinx.android.extensions.LayoutContainer

class GameAdapter() : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    private var combinationList: List<Combination> = emptyList()
    private var combinationListBN: List<Combination> = emptyList()

    override fun getItemCount(): Int = combinationList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val combination = combinationList[position]
        val combinationBN = combinationListBN[position]
        holder.bind(combination, combinationBN)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.round_easy_item, parent, false)
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

        private val ficha01BN: ImageView = containerView.findViewById(R.id.fichaByN01)
        private val ficha02BN: ImageView = containerView.findViewById(R.id.fichaByN02)
        private val ficha03BN: ImageView = containerView.findViewById(R.id.fichaByN03)
        private val ficha04BN: ImageView = containerView.findViewById(R.id.fichaByN04)

        private val arrayBN: Array<ImageView> = arrayOf(ficha01BN, ficha02BN, ficha03BN, ficha04BN)

        fun bind(combination: Combination, combinationBN: Combination) {
            combination.run {
                ficha01.background = ResourcesCompat.getDrawable(containerView.resources, chips[0].color, null)
                ficha02.background = ResourcesCompat.getDrawable(containerView.resources, chips[1].color, null)
                ficha03.background = ResourcesCompat.getDrawable(containerView.resources, chips[2].color, null)
                ficha04.background = ResourcesCompat.getDrawable(containerView.resources, chips[3].color, null)
            }

            /* combinationBN.run {
                for(i in 0 until chips.size) {
                    // Toast.makeText(containerView.context, chips[i].color.toString(), Toast.LENGTH_SHORT).show()
                    arrayBN[i].background = ResourcesCompat.getDrawable(containerView.resources, chips[i].color, null)
                    // Toast.makeText(containerView.context, "Check", Toast.LENGTH_SHORT).show()
                }
            }*/
        }
    }
}
