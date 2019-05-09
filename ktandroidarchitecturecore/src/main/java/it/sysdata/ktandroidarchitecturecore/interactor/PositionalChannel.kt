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

import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource

/**
 * [PositionalChannel] allow to define [Datasource] and [MutableLiveData] for handle the success, loading and failure case
 *
 * of Use Case and it allow to set a function for mapping Model into UiModel
 *
 */
open class PositionalChannel<Data> : Channel<Data>() {

    override fun initDatasource(datas: List<Data>){
        val pageSize = datas.size / 4
        (dataSource as BasePositionalDatasource).initDataSource(datas, pageSize)
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(false)
                .build()
        val dataSourceFactory = object : DataSource.Factory<Int, Data>() {
            override fun create(): DataSource<Int, Data> {
                return dataSource
            }
        }
        liveData = LivePagedListBuilder<Int, Data>(dataSourceFactory, config).build()
    }

    override fun postInitial(){
        (dataSource as BasePositionalDatasource).loadInitial(
                PositionalDataSource.LoadInitialParams(
                        0,
                        10,
                        10,
                        false
                ),
                object: PositionalDataSource.LoadInitialCallback<Data>(){
                    override fun onResult(data: MutableList<Data>, position: Int, totalCount: Int) {
//                        liveData.postValue(data)
                    }

                    override fun onResult(data: MutableList<Data>, position: Int) {
//                        liveData.postValue(data)
                    }

                }
        )
    }

    override fun postNext(){
        (dataSource as BasePositionalDatasource).loadRange(
                PositionalDataSource.LoadRangeParams(
                       0,
                        0
                ),
                object: PositionalDataSource.LoadRangeCallback<Data>(){
                    override fun onResult(data: MutableList<Data>) {
//                        liveData.postValue(data)
                    }

                }
        )
    }

}


