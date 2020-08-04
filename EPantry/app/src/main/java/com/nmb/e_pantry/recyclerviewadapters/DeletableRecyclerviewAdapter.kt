package com.nmb.e_pantry.recyclerviewadapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nmb.e_pantry.R
import com.nmb.e_pantry.RecViewData
import com.nmb.e_pantry.databinding.DeleteableRecylerviewListItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1
private val adapterScope = CoroutineScope(Dispatchers.Default)

class DeletableRecyclerviewAdapter(val clickListener: DeleteClickListener) :
    ListAdapter<DDataItem, RecyclerView.ViewHolder>(
        DCallBack()
    ) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DDataItem.Item -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list: List<RecViewData>?) {
        adapterScope.launch {
            val items: List<DDataItem> = when (list) {
                null -> listOf(DDataItem.Header)
                else -> listOf(DDataItem.Header) + list.map {
                    DDataItem.Item(
                        it
                    )
                }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> DTextViewHolder.from(
                parent
            )
            ITEM_VIEW_TYPE_ITEM -> DViewHolder.from(
                parent
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DViewHolder -> {
                val item = getItem(position)
                val itemAsRCW = RecViewData(item.id, item.menny)
                holder.bind(itemAsRCW, clickListener)
            }
        }
    }

}

private class DCallBack : DiffUtil.ItemCallback<DDataItem>() {
    override fun areItemsTheSame(oldItem: DDataItem, newItem: DDataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DDataItem, newItem: DDataItem): Boolean {
        return oldItem == newItem
    }
}

class DViewHolder(val binding: DeleteableRecylerviewListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RecViewData, clickListener: DeleteClickListener) {
        binding.recViewItem = item
        binding.nyersanyagTextview.text = item.id
        binding.mennyisegTextView.text = item.menny
        binding.itemClickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): DViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                DeleteableRecylerviewListItemBinding.inflate(layoutInflater, parent, false)
            return DViewHolder(binding)
        }
    }
}

class DTextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun from(parent: ViewGroup): BTextViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.header, parent, false)
            view.findViewById<TextView>(R.id.header_text).text = DDataItem.Header.id
            return BTextViewHolder(view)
        }
    }
}

class DeleteClickListener(val clickListener: (id: String) -> Unit) {
    fun onClick(data: RecViewData) = clickListener(data.id)
}

sealed class DDataItem {
    abstract val id: String
    abstract val menny: String

    data class Item(val rcw: RecViewData) : DDataItem() {
        override val id = rcw.id
        override val menny = rcw.menny
    }

    object Header : DDataItem() {
        override val id = "Bevásárlólista"
        override val menny = ""
    }
}