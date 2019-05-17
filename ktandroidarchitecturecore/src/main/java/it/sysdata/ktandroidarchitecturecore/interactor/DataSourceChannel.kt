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
import it.sysdata.ktandroidarchitecturecore.BaseItemKeyedDatasource
import it.sysdata.ktandroidarchitecturecore.BasePositionalDatasource

/**
 * [DataSourceChannel] allow to define [Datasource] and [MutableLiveData] for handle the success, loading and failure case
 *
 *
 */
class DataSourceChannel<Key: Any, Data>: Channel<PagedList<Data>>(){

    private lateinit var dataSource: DataSource<Key, Data>

    /**
     *
     * This method is used to initialize the datasource associated with the channel
     * and the live data for the paged lists associated with the datasource
     *
     * @param datas List<Data>, list of datas used as dataset of the datasource,
     *                          if your datasource doesn't need an initial dataset don't set this parameter and set the pagesize
     * @param pageSize Int, size of the page to load
     * @param initialLoadSizeHint Int, size of the initial page
     * @param placeHolderEnabled Boolean
     */
    fun initDatasource(datas: List<Data>? = null, pageSize: Int = (datas?.size ?: 0) / 4, initialLoadSizeHint: Int = pageSize * 2, placeHolderEnabled: Boolean = false){
        val dataSourceBase = dataSource
        this.liveData = initLiveData(dataSource, pageSize, initialLoadSizeHint, placeHolderEnabled)
        if (datas != null && datas.isNotEmpty()) {
            // TODO: check this
            if (dataSourceBase is BasePositionalDatasource) {
                dataSourceBase.init(datas, pageSize)
            } else if(dataSourceBase is BaseItemKeyedDatasource){
                dataSourceBase.init(datas, pageSize)
            }
        }
    }

    /**
     * this method is used to create a livedata of paged list based on the channel's datasource
     *
     * @param dataSource DataSource<Key, Data>
     * @param pageSize Int
     * @param initialLoadSizeHint Int
     * @param placeHolderEnabled Boolean
     * @return LiveData<PagedList<Data>>
     */
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
     * Use this class for create a instance of [Channel]
     */
    class ChannelBuilder<Key:Any, Data> internal constructor(private val channel: Channel<PagedList<Data>>) {

        fun build(): DataSourceChannel<Key, Data> {
            return channel as DataSourceChannel<Key, Data>
        }
    }


    /**
     * Use this class for create a instance of [DataSourceChannel]
     */
    class Builder<Key:Any, Data> {
        lateinit var channel : DataSourceChannel<Key, Data>

        fun <T> dataSource(dataSourceClass: Class<T>): ChannelBuilder<Key, Data> where T : DataSource<Key, Data> {
            return this.dataSource<T>(dataSourceClass.newInstance())
        }

        fun <T> dataSource(dataSource: DataSource<Key, Data>): ChannelBuilder<Key, Data> where T : DataSource<Key, Data> {
            channel = DataSourceChannel()
            channel.dataSource = dataSource
            return ChannelBuilder(channel)
        }
    }

}


