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
import com.nmb.e_pantry.databinding.WarningRecListItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1
private val adapterScope = CoroutineScope(Dispatchers.Default)

class WarningRecyclerviewAdapter(val clickListener: WarningClickListener) :
    ListAdapter<WDataItem, RecyclerView.ViewHolder>(
        WCallBack()
    ) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is WDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is WDataItem.Item -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list: List<RecViewData>?) {
        adapterScope.launch {
            val items: List<WDataItem> = when (list) {
                null -> listOf(WDataItem.Header)
                else -> listOf(WDataItem.Header) + list.map {
                    WDataItem.Item(
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
            ITEM_VIEW_TYPE_HEADER -> WTextViewHolder.from(
                parent
            )
            ITEM_VIEW_TYPE_ITEM -> WViewHolder.from(
                parent
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WViewHolder -> {
                val item = getItem(position)
                val itemAsRCW = RecViewData(item.id, item.menny)
                holder.bind(itemAsRCW, clickListener)
            }
        }
    }
}

private class WCallBack : DiffUtil.ItemCallback<WDataItem>() {
    override fun areItemsTheSame(oldItem: WDataItem, newItem: WDataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: WDataItem, newItem: WDataItem): Boolean {
        return oldItem == newItem
    }
}

class WViewHolder(val binding: WarningRecListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RecViewData, clickListener: WarningClickListener) {
        binding.recViewItem = item
        binding.nyersanyagTextview.text = item.id
        binding.mennyisegTextView.text = item.menny
        binding.itemClickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): WViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = WarningRecListItemBinding.inflate(layoutInflater, parent, false)
            return WViewHolder(binding)
        }

    }
}

class WTextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun from(parent: ViewGroup): WTextViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.header, parent, false)
            view.findViewById<TextView>(R.id.header_text).text = WDataItem.Header.id
            return WTextViewHolder(view)
        }
    }
}

class WarningClickListener(val clickListener: (id: String) -> Unit) {
    fun onClick(data: RecViewData) = clickListener(data.id)
}

sealed class WDataItem {
    abstract val id: String
    abstract val menny: String

    data class Item(val rcw: RecViewData) : WDataItem() {
        override val id = rcw.id
        override val menny = rcw.menny
    }

    object Header : WDataItem() {
        override val id = "Figyelmeztetések"
        override val menny = ""
    }
}
