package es.iessaladillo.esthercastaneda.mastermind.ui.game.inGame

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Combination
import kotlinx.android.extensions.LayoutContainer

class GameAdapter() : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    private var dataList: List<Combination> = emptyList()

    override fun getItemCount(): Int = dataList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val combination = dataList[position]
        holder.bind(combination)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.round_easy_item, parent, false)
        return ViewHolder(itemView)
    }

    fun submitList(newList: List<Combination>) {
        dataList = newList
        notifyDataSetChanged()
    }


    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val ficha01: ImageView = containerView.findViewById(R.id.ficha01)
        private val ficha02: ImageView = containerView.findViewById(R.id.ficha02)
        private val ficha03: ImageView = containerView.findViewById(R.id.ficha03)
        private val ficha04: ImageView = containerView.findViewById(R.id.ficha04)

        fun bind(combination: Combination) {
            combination.run {
                ficha01.background = ResourcesCompat.getDrawable(containerView.resources, chip01, null)
                ficha02.background = ResourcesCompat.getDrawable(containerView.resources, chip02, null)
                ficha03.background = ResourcesCompat.getDrawable(containerView.resources, chip03, null)
                ficha04.background = ResourcesCompat.getDrawable(containerView.resources, chip04, null)
            }
        }
    }
}
