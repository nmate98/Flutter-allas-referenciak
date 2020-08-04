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


class SpajzRecyclerviewAdapter : ListAdapter<SDataItem, RecyclerView.ViewHolder>(
    SCallBack()
) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is SDataItem.Item -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list: List<RecViewData>?) {
        adapterScope.launch {
            val items: List<SDataItem> = when (list) {
                null -> listOf(SDataItem.Header)
                else -> listOf(SDataItem.Header) + list.map {
                    SDataItem.Item(
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
            ITEM_VIEW_TYPE_HEADER -> STextViewHolder.from(
                parent
            )
            ITEM_VIEW_TYPE_ITEM -> SViewHolder.from(
                parent
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SViewHolder -> {
                val item = getItem(position)
                holder.nyersanyagText.text = item.id
                holder.mennyisegText.text = item.menny
            }
        }
    }
}

class SViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nyersanyagText: TextView = itemView.findViewById(R.id.nyersanyag_textview)
    val mennyisegText: TextView = itemView.findViewById(R.id.mennyiseg_textView)

    companion object {
        fun from(parent: ViewGroup): SViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.recylerview_list_item, parent, false)
            return SViewHolder(view)
        }
    }
}

class STextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun from(parent: ViewGroup): STextViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.header, parent, false)
            view.findViewById<TextView>(R.id.header_text).text = SDataItem.Header.id
            return STextViewHolder(view)
        }
    }
}

private class SCallBack : DiffUtil.ItemCallback<SDataItem>() {
    override fun areItemsTheSame(oldItem: SDataItem, newItem: SDataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: SDataItem, newItem: SDataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class SDataItem {
    abstract val id: String
    abstract val menny: String

    data class Item(val rcw: RecViewData) : SDataItem() {
        override val id = rcw.id
        override val menny = rcw.menny
    }

    object Header : SDataItem() {
        override val id = "Spájz"
        override val menny = ""
    }
}

