package com.sysdata.kt.ktandroidarchitecture.ui

import androidx.recyclerview.widget.DiffUtil
import java.util.*

data class Note(val index:Int, val noteId: String = UUID.randomUUID().toString()) {
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean
                    = oldItem.noteId == newItem.noteId

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean
                    = oldItem.noteId == newItem.noteId && oldItem.title == newItem.title && oldItem.content == newItem.content
        }
    }
    var title: String = ""
    var content: String = ""
}