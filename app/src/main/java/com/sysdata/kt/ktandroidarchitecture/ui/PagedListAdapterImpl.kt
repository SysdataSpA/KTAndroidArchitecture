/**
 * Copyright (C) 2019 Sysdata S.p.a.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sysdata.kt.ktandroidarchitecture.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sysdata.kt.ktandroidarchitecture.R

/**
 * Dummy implementation of [PagedListAdapter]
 *
 * @property onClick Function1<Note, Unit>
 * @constructor
 */
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