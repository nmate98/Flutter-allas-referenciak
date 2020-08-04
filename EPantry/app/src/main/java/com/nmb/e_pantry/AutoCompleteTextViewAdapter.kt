package com.nmb.e_pantry

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import java.util.*

class AutoCompleteTextViewAdapter(
    context: Context,
    private val resourceId: Int,
    private val allItem: List<String>
) :
    ArrayAdapter<String>(context, resourceId, allItem), Filterable {
    private var items = allItem

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): String? {
        return items[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(
            resourceId,
            parent,
            false
        ) as TextView
        view.text = items[position]
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val str : String = constraint.toString().toLowerCase(Locale.getDefault())
                val results = FilterResults()
                items = allItem
                results.values = if (str == "" || str == "null" || str.isEmpty()) {
                    items
                } else {
                    items.filter {
                        it.toLowerCase(Locale.getDefault()).contains(str)
                    }
                }
                return results

            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                items = results!!.values as List<String>
                notifyDataSetChanged()
            }
        }
    }
}