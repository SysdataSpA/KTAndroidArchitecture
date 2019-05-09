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

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.PagedList

/**
 * [Channel] allow to define [Datasource] and [MutableLiveData] for handle the success, loading and failure case
 *
 * of Use Case and it allow to set a function for mapping Model into UiModel
 *
 */
abstract class Channel<Data>{

    protected var liveData: LiveData<PagedList<Data>>? = null

    protected lateinit var dataSource: DataSource<Int, Data>

    /**
     * Define the function that will use for handle the result
     *
     * @param owner for [liveData]
     * @param body  the function that will use for handle the result
     */
    fun observe(owner: LifecycleOwner, body: (PagedList<Data>?) -> Unit) {
        liveData?.observe(owner, Observer(body))
    }

    abstract fun initDatasource(datas: List<Data>)

    abstract fun postInitial()

    abstract fun postNext()

    class ChannelBuilder<Data> internal constructor(private val channel: Channel<Data>) {
        /**
         * Set map function for [Channel]
         *
         * @return [Builder] instance
         */
        fun buildWithUiModel(): Channel<Data> {
            return channel

        }
    }


    /**
     * Use this class for create a instance of [Channel]
     */
    class Builder<Data> {
        lateinit var channel : Channel<Data>

        fun<T> channel(channelClass: Class<T>) : Builder<Data> where T : Channel<Data>{
            val channel = channelClass.newInstance()
            this.channel = channel
            return this
        }

        /**
         * Set use case for [Channel]
         * @param dataSourceClass Java class of use case
         *
         * @return [ChannelBuilder] instance
         */

        fun <T> dataSource(dataSourceClass: Class<T>): ChannelBuilder<Data> where T : DataSource<Int, Data> {
            channel.dataSource = dataSourceClass.newInstance()
            return ChannelBuilder(channel)
        }


    }

}


