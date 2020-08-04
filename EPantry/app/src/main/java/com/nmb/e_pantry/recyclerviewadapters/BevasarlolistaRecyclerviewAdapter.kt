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


class BevasarlolistaRecyclerviewAdapter : ListAdapter<BDataItem, RecyclerView.ViewHolder>(
    BCallBack()
) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is BDataItem.Item -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list: List<RecViewData>?) {
        adapterScope.launch {
            val items: List<BDataItem> = when (list) {
                null -> listOf(BDataItem.Header)
                else -> listOf(BDataItem.Header) + list.map {
                    BDataItem.Item(
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
            ITEM_VIEW_TYPE_HEADER -> BTextViewHolder.from(
                parent
            )
            ITEM_VIEW_TYPE_ITEM -> BViewHolder.from(
                parent
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BViewHolder -> {
                val item = getItem(position)
                holder.nyersanyagText.text = item.id
                holder.mennyisegText.text = item.menny
            }
        }
    }
}

class BViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nyersanyagText: TextView = itemView.findViewById(R.id.nyersanyag_textview)
    val mennyisegText: TextView = itemView.findViewById(R.id.mennyiseg_textView)

    companion object {
        fun from(parent: ViewGroup): BViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.recylerview_list_item, parent, false)
            return BViewHolder(view)
        }
    }
}

class BTextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun from(parent: ViewGroup): BTextViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.header, parent, false)
            view.findViewById<TextView>(R.id.header_text).text = BDataItem.Header.id
            return BTextViewHolder(view)
        }
    }
}

private class BCallBack : DiffUtil.ItemCallback<BDataItem>() {
    override fun areItemsTheSame(oldItem: BDataItem, newItem: BDataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: BDataItem, newItem: BDataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class BDataItem {
    abstract val id: String
    abstract val menny: String

    data class Item(val rcw: RecViewData) : BDataItem() {
        override val id = rcw.id
        override val menny = rcw.menny
    }

    object Header : BDataItem() {
        override val id = "Bevásárlólista"
        override val menny = ""
    }
}

