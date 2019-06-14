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

import androidx.paging.PositionalDataSource

/**
 * A simple implementation of [PositionalDataSource]
 *
 * @param Data : the type of data given by the DataSource
 * @property defaultLoadSize Int?, default size of a page
 * @property datas List<Data>, initial dataset
 * @property currentPosition Int, position of the first item we want
 */
open class BasePositionalDatasource<Data : Any>: PositionalDataSource<Data>(){

    private var defaultLoadSize: Int? = null
    private lateinit var datas: List<Data>
    private var currentPosition: Int = 0

    /**
     * This method returns the datas in a given range of positions
     *
     * @param params LoadRangeParams
     * @param callback LoadRangeCallback<Data>
     */
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Data>) {
        val loadSize = params.loadSize
        val startPosition = params.startPosition

        val subList = if(currentPosition > 0 && defaultLoadSize != null){
            getSubList(currentPosition, defaultLoadSize as Int)
        } else{
            getSubList(startPosition, loadSize)
        }

        callback.onResult(subList)
    }

    /**
     * This method loads the initial datas
     *
     * @param params LoadInitialParams
     * @param callback LoadInitialCallback<Data>
     */
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Data>) {
        var startPosition = params.requestedStartPosition
        var pageSize = params.pageSize
        val placeholdersEnabled = params.placeholdersEnabled
        val requestedLoadSize = params.requestedLoadSize

        startPosition = if(startPosition < datas.size) startPosition else 0

        defaultLoadSize?.let {
            if(pageSize > it){
                pageSize = it
            }
        }
        val subList = getSubList(startPosition, requestedLoadSize)
        if(placeholdersEnabled){
            callback.onResult(subList, startPosition, datas.size)
        } else {
            callback.onResult(subList, startPosition)
        }
    }

    /**
     * returns a sublist of the dataset
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
        currentPosition = endIndex
        return datas.subList(startPosition, endIndex)
    }

    fun init(datas: List<Data>, pageSize: Int) {
        this.datas = datas
        this.defaultLoadSize = pageSize
    }
}