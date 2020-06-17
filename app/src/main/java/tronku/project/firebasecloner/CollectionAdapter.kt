package tronku.project.firebasecloner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.collection_item_layout.view.*
import tronku.project.firebasecloner.db.CollectionModel

class CollectionAdapter(private val collectionClickListener: CollectionClickListener)
    : RecyclerView.Adapter<CollectionAdapter.Viewholder>() {

    private var collections = listOf<CollectionModel>()

    inner class Viewholder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(collectionModel: CollectionModel) {
            itemView.collectionName.text = collectionModel.collectionName
            itemView.deleteButton.setOnClickListener {
                collectionClickListener.onClicked(collectionModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.collection_item_layout, parent, false)
        return Viewholder(view)
    }

    override fun getItemCount() = collections.size

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(collections[position])
    }

    fun updateList(list: List<CollectionModel>) {
        collections = list
        notifyDataSetChanged()
    }

    interface CollectionClickListener {
        fun onClicked(collectionModel: CollectionModel)
    }

}