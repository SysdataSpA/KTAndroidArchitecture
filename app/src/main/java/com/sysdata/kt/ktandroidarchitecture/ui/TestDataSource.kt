package com.sysdata.kt.ktandroidarchitecture.ui

import androidx.paging.ItemKeyedDataSource

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