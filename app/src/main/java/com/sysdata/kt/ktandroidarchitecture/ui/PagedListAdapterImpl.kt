package com.sysdata.kt.ktandroidarchitecture.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sysdata.kt.ktandroidarchitecture.R

class PagedListAdapterImpl(val onClick: (Note) -> Unit) : PagedListAdapter<Note, PagedListAdapterImpl.ListItemViewHolder>(Note.DiffCallback) {
    class ListItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        var note: Note? = null
        set(value) {
            if(value != null){
                val tv: TextView = view.findViewById(R.id.itemText)
                tv.text = value.toString()
            }
            field = value
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder
            = ListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false))

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        Log.d(TAG, "Binding view holder at position $position")
        holder.note = getItem(position)
        holder.view.setOnClickListener { onClick(holder.note!!) }
    }

}