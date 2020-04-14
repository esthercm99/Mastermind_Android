package es.iessaladillo.esthercastaneda.mastermind.ui.game.inGame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Combination
import kotlinx.android.extensions.LayoutContainer

class GameAdapter() : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    private var dataList: List<Combination> = listOf()

    override fun getItemCount(): Int = dataList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
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
        fun bind() {}
    }
}
