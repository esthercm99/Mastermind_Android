package es.iessaladillo.esthercastaneda.mastermind.ui.menu.manual

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.entity.Page
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.manual_pages_fragment.view.*

class ManualFragmentAdapter (val pageList: List<Page>) : RecyclerView.Adapter<ManualFragmentAdapter.ViewHolder>() {

    private var currentPosition : Int =  0
    var onItemClickListener: ((Int) -> Unit)? = null

    override fun getItemCount(): Int = pageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentPosition = position
        holder.bind(pageList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.manual_pages_fragment, parent, false)

        return ViewHolder(itemView)
    }


    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {  containerView.btnFinish.setOnClickListener { onItemClickListener?.invoke(adapterPosition) } }

        private val settings: SharedPreferences by lazy {
            PreferenceManager.getDefaultSharedPreferences(containerView.context)
        }

        fun bind(page: Page) {
            page.run {

                val language = settings.getString(containerView.context.getString(R.string.prefLanguage_key), containerView.context.getString(R.string.prefLanguage_defaultValue))

                if(language.equals(containerView.context.getString(R.string.prefLanguage_defaultValue))) {
                    containerView.icon.background = containerView.context.getDrawable(page.imgES)
                } else {
                    containerView.icon.background = containerView.context.getDrawable(page.imgEN)
                }

                if (adapterPosition == pageList.size - 1) {
                    containerView.btnFinish.visibility = View.VISIBLE
                } else {
                    containerView.btnFinish.visibility = View.INVISIBLE
                }

            }
        }

    }
}