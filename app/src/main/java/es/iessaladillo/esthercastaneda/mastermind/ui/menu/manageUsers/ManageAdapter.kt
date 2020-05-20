package es.iessaladillo.esthercastaneda.mastermind.ui.menu.manageUsers

import es.iessaladillo.esthercastaneda.mastermind.data.bbdd.UserPlayer
import kotlinx.android.extensions.LayoutContainer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.bbdd.DatabaseUser

class ManageAdapter() : RecyclerView.Adapter<ManageAdapter.ViewHolder>() {

    private var dataList: List<UserPlayer> = listOf()
    var onItemClickListener: ((Int) -> Unit)? = null

    override fun getItemCount(): Int = dataList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(dataList[position])
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.user_item, parent, false)
        return ViewHolder(itemView)
    }

    fun submitListUsers(newList: List<UserPlayer>) {
        dataList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val txtUser: TextView = containerView.findViewById(R.id.txtUser)

        init {
            containerView.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }

        fun bind(userPlayer: UserPlayer) {
              txtUser.text = userPlayer.nameUser
        }
    }
}