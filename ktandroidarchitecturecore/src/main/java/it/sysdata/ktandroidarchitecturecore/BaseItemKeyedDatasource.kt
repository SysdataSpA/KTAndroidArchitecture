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
package it.sysdata.ktandroidarchitecturecore

import androidx.paging.*

/**
 * A simple implementation of [ItemKeyedDataSource]
 *
 * @param Key:Any, the key to get the item from the datasource
 * @param Data : Any, the data returned on each call
 * @property defaultLoadSize Int?, the default size of a page
 * @property datas List<Data>, the initial dataset
 */
open class BaseItemKeyedDatasource<Key:Any, Data : Any>: ItemKeyedDataSource<Key, Data>(){

    private var defaultLoadSize: Int? = null
    protected lateinit var datas: List<Data>

    /**
     * This method if called to load all the data after the position of the item with the given key
     *
     * @param params LoadParams<Key>
     * @param callback LoadCallback<Data>
     */
    override fun loadAfter(params: LoadParams<Key>, callback: LoadCallback<Data>) {

        val requestedLoadSize = params.requestedLoadSize
        val key = params.key

        val item = getItemForKey(key)
        var startPosition = datas.indexOf(item)
        startPosition = if(startPosition < datas.size) startPosition else 0

        val subList = getSubList(startPosition, requestedLoadSize)

        callback.onResult(subList)
    }

    /**
     * This method if called to load all the data before the position of the item with the given key
     *
     * @param params LoadParams<Key>
     * @param callback LoadCallback<Data>
     */
    override fun loadBefore(params: LoadParams<Key>, callback: LoadCallback<Data>) {
        val requestedLoadSize = params.requestedLoadSize
        val key = params.key

        val item = getItemForKey(key)
        var startPosition = datas.indexOf(item) - requestedLoadSize
        startPosition = if(startPosition > 0) startPosition else 0

        val subList = getSubList(startPosition, requestedLoadSize)

        callback.onResult(subList)
    }

    /**
     * This method returns the key of the given item
     *
     * @param item Data
     * @return Key
     */
    override fun getKey(item: Data): Key {
        return item.hashCode() as Key
    }

    /**
     * This method is used to load the initial datas
     * @param params LoadInitialParams<Key>
     * @param callback LoadInitialCallback<Data>
     */
    override fun loadInitial(params: LoadInitialParams<Key>, callback: LoadInitialCallback<Data>) {
        val placeholdersEnabled = params.placeholdersEnabled
        val requestedInitialKey = params.requestedInitialKey
        var requestedLoadSize = params.requestedLoadSize

        val item = requestedInitialKey?.let { getItemForKey(it) }
        var startPosition = item?.let {datas.indexOf(item)} ?: 0
        startPosition = if(startPosition < datas.size) startPosition else 0

        defaultLoadSize?.let {
            if(requestedLoadSize > it){
                requestedLoadSize = it
            }
        }
        val subList = getSubList(startPosition, requestedLoadSize)

        if (placeholdersEnabled) {
            callback.onResult(subList, startPosition, datas.size)
        } else {
            callback.onResult(subList)
        }
    }

    /**
     * This method is used to retrieve the item based on the key,
     * Override this method to use another key
     *
     * @param key Key
     * @return Data
     */
    protected open fun getItemForKey(key: Key):Data{
        return datas.first { it.hashCode() == key.hashCode() }
    }

    /**
     * returns a sublist of the initial dataset
     *
     * @param startPosition Int
     * @param defaultLoadSize Int
     * @return List<Data>
     */
    private fun getSubList(startPosition: Int, defaultLoadSize: Int): List<Data> {
        if(startPosition >= datas.size){
            return datas.subList(datas.size - 1, datas.size - 1)
        }
        val endIndex = if (startPosition + defaultLoadSize <= datas.size) startPosition + defaultLoadSize else datas.size - 1
        return datas.subList(startPosition, endIndex)
    }

    fun init(datas: List<Data>, pageSize: Int) {
        this.datas = datas
        this.defaultLoadSize = pageSize
    }
}