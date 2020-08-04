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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1
private val adapterScope = CoroutineScope(Dispatchers.Default)


class HozzavaloRecyclerviewAdapter : ListAdapter<HDataItem, RecyclerView.ViewHolder>(
    HCallBack()
) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is HDataItem.Item -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list: List<RecViewData>?) {
        adapterScope.launch {
            val items: List<HDataItem> = when (list) {
                null -> listOf(HDataItem.Header)
                else -> listOf(HDataItem.Header) + list.map {
                    HDataItem.Item(
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
            ITEM_VIEW_TYPE_HEADER -> HTextViewHolder.from(
                parent
            )
            ITEM_VIEW_TYPE_ITEM -> HViewHolder.from(
                parent
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HViewHolder -> {
                val item = getItem(position)
                holder.nyersanyagText.text = item.id
                holder.mennyisegText.text = item.menny
            }
        }
    }
}

class HViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nyersanyagText: TextView = itemView.findViewById(R.id.nyersanyag_textview)
    val mennyisegText: TextView = itemView.findViewById(R.id.mennyiseg_textView)

    companion object {
        fun from(parent: ViewGroup): HViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.recylerview_list_item, parent, false)
            return HViewHolder(view)
        }
    }
}

class HTextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun from(parent: ViewGroup): HTextViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.header, parent, false)
            view.findViewById<TextView>(R.id.header_text).text = HDataItem.Header.id
            return HTextViewHolder(view)
        }
    }
}

private class HCallBack : DiffUtil.ItemCallback<HDataItem>() {
    override fun areItemsTheSame(oldItem: HDataItem, newItem: HDataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: HDataItem, newItem: HDataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class HDataItem {
    abstract val id: String
    abstract val menny: String

    data class Item(val rcw: RecViewData) : HDataItem() {
        override val id = rcw.id
        override val menny = rcw.menny
    }

    object Header : HDataItem() {
        override val id = "Hozzávalók"
        override val menny = ""
    }
}

