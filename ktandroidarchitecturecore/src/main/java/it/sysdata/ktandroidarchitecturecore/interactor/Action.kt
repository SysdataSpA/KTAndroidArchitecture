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
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.functional.Either

/**
 * [Action] allow to define [UseCase] and [MutableLiveData] for handle the success, loading and failure case
 *
 * of Use Case and it allow to set a function for mapping Model into UiModel
 *
 */
class Action<Params : ActionParams, Model : Any, UiModel : Any> private constructor() {

    private val liveData = MutableLiveData<UiModel>()
    private val loadingLiveData = MutableLiveData<Boolean>()

    private val failureLiveData = MutableLiveData<Failure>()
    private lateinit var uc: UseCase<Model, Params>
    private var mappingFunction: (Model) -> UiModel? = { null }

    /**
     * Execute the action
     *
     * @param params for use case
     */
    fun execute(params: Params) {
        loadingLiveData.postValue(true)
        uc.execute({ it.either(::handleFailure, ::handleSuccess) }, params)

    }

    /**
     * Define the function that will use for handle the result
     *
     * @param owner for [liveData]
     * @param body  the function that will use for handle the result
     */
    fun observe(owner: LifecycleOwner, body: (UiModel?) -> Unit) {
        liveData.observe(owner, Observer(body))

    }

    /**
     * Define the function that will use for handle the failure
     *
     * @param owner for [failureLiveData]
     * @param body  the function that will use for handle the failure
     */
    fun observeFailure(owner: LifecycleOwner, body: (Failure?) -> Unit) {
        failureLiveData.observe(owner, Observer(body))

    }

    /**
     * Define the function that will use for handle the loading
     *
     * @param owner for [loadingLiveData]
     * @param body  the function that will use for handle the loading
     */
    fun observeLoadingStatus(owner: LifecycleOwner, body: (Boolean?) -> Unit) {
        loadingLiveData.observe(owner, Observer(body))

    }

    /**
     * Post loading live date to false
     *
     * Post the Fail into [MutableLiveData]
     * @param fail
     */
    private fun handleFailure(fail: Failure) {
        loadingLiveData.postValue(false)
        failureLiveData.postValue(fail)
    }

    /**
     * Post loading live date to false
     *
     * Post the Uimodel with @mappingFunction into [MutableLiveData]
     * @param model
     */
    private fun handleSuccess(model: Model) {
        loadingLiveData.postValue(false)
        liveData.postValue(mappingFunction(model))
    }


    class ActionBuilderMappingUiModel<Params : ActionParams, Model : Any, UiModel : Any> internal constructor(private val action: Action<Params, Model, UiModel>) {
        /**
         * Set map function for [Action]
         * @param handleResult function for mapping Model to UiModel
         *
         * @return [Builder] instance
         */
        fun buildWithUiModel(handleResult: (Model) -> UiModel): Action<Params, Model, UiModel> {
            action.mappingFunction = handleResult
            return action

        }


    }


    /**
     * Use this class for create a instance of [Action]
     */
    class Builder<Params : ActionParams, Model : Any, UiModel : Any> {
        private val action = Action<Params, Model, UiModel>()

        /**
         * Set use case for [Action]
         * @param useCaseClass Java class of use case
         *
         * @return [ActionBuilderMappingUiModel] instance
         */

        fun <T> useCase(useCaseClass: Class<T>): ActionBuilderMappingUiModel<Params, Model, UiModel> where T : UseCase<Model, Params> {
            action.uc = useCaseClass.newInstance()

            return ActionBuilderMappingUiModel(action)
        }

        /**
         * Set use case for [Action]
         * @param run function for Action
         *
         * @return [ActionBuilderMappingUiModel] instance
         */
        fun useCase(run: (Params) -> Either<Failure, Model>): ActionBuilderMappingUiModel<Params, Model, UiModel> {
            action.uc = object : UseCase<Model, Params>() {
                override suspend fun run(params: Params): Either<Failure, Model> {
                    return run.invoke(params)
                }
            }
            return ActionBuilderMappingUiModel(action)
        }


    }

}


