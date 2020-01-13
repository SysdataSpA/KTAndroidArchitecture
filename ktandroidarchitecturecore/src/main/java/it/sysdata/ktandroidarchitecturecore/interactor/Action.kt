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
package it.sysdata.ktandroidarchitecturecore.interactor

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.functional.Either
import it.sysdata.ktandroidarchitecturecore.platform.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope

/**
 * [Action] allow to define [UseCase] and [MutableLiveData] for handle the success, loading and failure case
 *
 * of Use Case and it allow to set a function for mapping Model into UiModel
 *
 */
class Action<Params : ActionParams, Model : Any, UiModel : Any> private constructor() {

    private val liveData = MutableLiveData<UiModel>()
    private val loadingLiveData = MutableLiveData<Boolean>()

    private val failureLiveData = SingleLiveEvent<Failure>()
    private lateinit var uc: UseCase<Model, Params>
    private var mappingFunction: (Model) -> UiModel? = { null }

    private var lastParams: Params? = null

    /**
     * Execute the action
     *
     * @param params for use case
     */
    fun execute(params: Params, scope: CoroutineScope = GlobalScope) {
        lastParams = params
        loadingLiveData.value = true
        uc.execute({ it.either(::handleFailure, ::handleSuccess) }, params, scope)
    }

    /**
     * Execute the action in safe mode. This means tha whenever an exception is thrown, the architecture will return a [Failure.InternalError] failure.
     *
     * @param params for use case
     */
    fun safeExecute(params: Params, scope: CoroutineScope = GlobalScope) {
        lastParams = params
        loadingLiveData.value = true
        uc.execute({ it.either(::handleFailure, ::handleSuccess) }, params, scope,true)
    }
    /**
     * Retry the action performed last type
     *
     * @param params for use case
     */
    fun retry() {
        if (lastParams == null) return
        lastParams?.let {
            execute(it)
        }
    }

    /**
     * Define the function that will use for handle the result
     *
     * @param owner for [liveData]
     * @param body  the function that will use for handle the result
     */
    fun observe(owner: LifecycleOwner, body: (UiModel) -> Unit) {
        liveData.observe(owner, Observer(body))

    }

    /**
     * Define the function that will use for handle the result without model
     *
     * @param owner for [liveData]
     * @param body  the function that will use for handle the result
     */
    fun observeWithoutModel(owner: LifecycleOwner, body: () -> Unit) {
        liveData.observe(owner, Observer { body.invoke() })

    }

    /**
     * Define the function that will use for handle the failure
     *
     * @param owner for [failureLiveData]
     * @param body  the function that will use for handle the failure
     */
    fun observeFailure(owner: LifecycleOwner, body: (Failure) -> Unit) {
        failureLiveData.observe(owner, Observer(body))

    }

    /**
     * Define the function that will use for handle the failure without type of Failure
     *
     * @param owner for [failureLiveData]
     * @param body  the function that will use for handle the failure
     */
    fun observeFailureWithoutType(owner: LifecycleOwner, body: () -> Unit) {
        failureLiveData.observe(owner, Observer { body.invoke() })

    }

    /**
     * Define the function that will use for handle the loading
     *
     * @param owner for [loadingLiveData]
     * @param body  the function that will use for handle the loading
     */
    fun observeLoadingStatus(owner: LifecycleOwner, body: (Boolean) -> Unit) {
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

        /**
         * Set use case for [Action]
         * @param useCase the instance of useCase
         *
         * @return [ActionBuilderMappingUiModel] instance
         */
        fun <T> useCase(useCase: T): ActionBuilderMappingUiModel<Params, Model, UiModel> where T : UseCase<Model, Params> {
            action.uc = useCase

            return ActionBuilderMappingUiModel(action)
        }
    }

}


