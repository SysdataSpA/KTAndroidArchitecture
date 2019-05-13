/**
 * Copyright (C) 2018 Sysdata Digital, S.r.l.
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
package it.sysdata.ktandroidarchitecturecore.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

/**
 * [DataSourceChannel] allow to define [Datasource] and [MutableLiveData] for handle the success, loading and failure case
 *
 * of Use Case and it allow to set a function for mapping Model into UiModel
 *
 */
class DataSourceChannel<Key: Any, Data>: Channel<PagedList<Data>>(){

    private lateinit var dataSource: DataSource<Key, Data>

    fun initDatasource(datas: List<Data>, pageSize: Int = datas.size / 4, initialLoadSizeHint: Int = pageSize * 2, placeHolderEnabled: Boolean = false){
        val dataSourceBase = dataSource
        this.liveData = initLiveData(dataSource, pageSize, initialLoadSizeHint, placeHolderEnabled)
        // TODO: check this
        if (dataSourceBase is BasePositionalDatasource) {
            dataSourceBase.init(datas, pageSize)
        } else if(dataSourceBase is BaseItemKeyedDatasource){
            dataSourceBase.init(datas, pageSize)
        }
        this.liveData = initLiveData(dataSource, pageSize, initialLoadSizeHint, placeHolderEnabled)
    }

    private fun initLiveData(dataSource: DataSource<Key, Data>, pageSize: Int, initialLoadSizeHint: Int, placeHolderEnabled: Boolean): LiveData<PagedList<Data>> {
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(initialLoadSizeHint)
                .setEnablePlaceholders(placeHolderEnabled)
                .build()
        val dataSourceFactory = object : DataSource.Factory<Key, Data>() {
            override fun create(): DataSource<Key, Data> {
                return dataSource
            }
        }
        return LivePagedListBuilder<Key, Data>(dataSourceFactory, config).build()
    }


    /**
     * Use this class for create a instance of [DataSourceChannel]
     */
    class Builder<Key:Any, Data> {
        lateinit var channel : DataSourceChannel<Key, Data>

        fun <T> dataSource(dataSourceClass: Class<T>): ChannelBuilder<PagedList<Data>> where T : DataSource<Key, Data> {
            return this.dataSource<T>(dataSourceClass.newInstance())
        }

        fun <T> dataSource(dataSource: DataSource<Key, Data>): ChannelBuilder<PagedList<Data>> where T : DataSource<Key, Data> {
            channel = DataSourceChannel()
            channel.dataSource = dataSource
            return ChannelBuilder(channel)
        }
    }

}


