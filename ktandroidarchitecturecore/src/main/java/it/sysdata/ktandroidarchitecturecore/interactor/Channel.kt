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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * [Channel] allow to observe a [MutableLiveData] used to post datas time by time
 *
 */
open class Channel<Data>{

    var liveData: LiveData<Data> = MutableLiveData()

    /**
     * Define the function that will use for handle the data posted by the channel
     *
     * @param owner for [liveData]
     * @param body  the function that will use for handle the data posted by the channel
     */
    fun observe(owner: LifecycleOwner, body: (Data?) -> Unit) {
        liveData.observe(owner, Observer(body))
    }

    /**
     * Use this method to post datas through channel
     *
     * @param data Data to be posted on the livedata
     */
    fun postData(data: Data){
        val postLiveData = liveData
        if (postLiveData is MutableLiveData) {
            postLiveData.postValue(data)
        }
    }

}


