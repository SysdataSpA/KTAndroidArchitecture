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

import androidx.recyclerview.widget.DiffUtil
import java.util.*

/**
 * Data object used into the paged lisi adapter, we implement a [diff callback][DiffUtil.ItemCallback] because is need by the adapter
 *
 * @property index Int
 * @property noteId String
 * @property title String
 * @property content String
 * @constructor
 */
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