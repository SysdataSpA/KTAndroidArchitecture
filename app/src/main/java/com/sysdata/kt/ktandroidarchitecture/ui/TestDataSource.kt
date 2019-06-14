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

import androidx.paging.ItemKeyedDataSource

/**
 * Fake datasource that returns random data on each call
 *
 * @property datas List<Note>
 */
class TestDataSource: ItemKeyedDataSource<Int, Note>(){

    private val datas = listOf(Note(1), Note(2), Note(3), Note(4), Note(5))

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Note>) {
        val requestedLoadSize = params.requestedLoadSize
        val requestedInitialKey = params.requestedInitialKey ?: 0

        val initItem = datas.firstOrNull { it.index == requestedInitialKey }
        val initIndex = initItem?.let {datas.indexOf(initItem)} ?: 0
        val newData = getSubList(initIndex, initIndex + requestedLoadSize)
        callback.onResult(newData)
    }

    private fun getSubList(initIndex: Int, finalIndex: Int): MutableList<Note> {
        val newData = mutableListOf<Note>()
        for (i in initIndex until finalIndex) {
            newData.add(datas[i % datas.size])
        }
        return newData
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Note>) {
        val key = params.key
        val requestedLoadSize = params.requestedLoadSize

        val initItem = datas.firstOrNull { it.index == key }
        val initIndex = initItem?.let {datas.indexOf(initItem)} ?: 0
        val newData = getSubList(initIndex, initIndex + requestedLoadSize)
        callback.onResult(newData)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Note>) {
        val key = params.key
        val requestedLoadSize = params.requestedLoadSize

        val initItem = datas.firstOrNull { it.index == key }
        var initIndex = initItem?.let {datas.indexOf(initItem)} ?: 0
        var finalIndex = initIndex
        if(initIndex - requestedLoadSize >= 0){
            initIndex -= requestedLoadSize
        } else {
            initIndex = 0
            finalIndex = initIndex + requestedLoadSize
        }
        val newData = getSubList(initIndex, finalIndex)
        callback.onResult(newData)
    }

    override fun getKey(item: Note): Int {
        return item.index
    }

}