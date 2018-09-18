/**
 * Copyright (C) 2018 Sysdata S.p.a.
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
 * [ActionQueue] allow to define a queue of [UseCase]
 * they will be launched one after the other and the Model of previous [UseCase]
 * will be mapping for Params of next [UseCase]
 */
class ActionQueue<Params : ActionParams, UiModel : Any, LastModel : Any> private constructor() {
    // LiveData for UiModel
    private val liveData = MutableLiveData<UiModel>()

    // LiveData for loading status (true if action is loading)
    private val loadingLiveData = MutableLiveData<Boolean>()

    // LiveData for fail status
    private val failureLiveData = MutableLiveData<Failure>()

    //Last use case
    private lateinit var lastUseCase: ActionQueueItem<*, *, *>

    //Check if call the last use case
    private var isLastAction = false

    private var index = 1

    //Queue of use case
    private val useCaseQueue = mutableListOf<ActionQueueItem<*, *, *>>()

    //Mapping function for mapping last model of last use case to UiModel
    private var mappingFunction: (LastModel) -> UiModel? = { null }

    /**
     * Execute the first action
     *
     * @param params for first use case
     */
    fun execute(params: Params) {
        val item = useCaseQueue.first()
        loadingLiveData.postValue(true)
        if (useCaseQueue.size == 1) //check if the first use case is the only use case (use normal Action for this case)
            isLastAction = true
        item.executeWithoutMapping(::handleFailure, ::handleUseCase, params)


    }


    @Suppress("UNCHECKED_CAST")
    private fun <T> handleUseCase(model: T) {

        // If last action set the user action for the result use case
        //  otherwise call the action with this function as function result

        when {
            isLastAction -> lastUseCase.execute(::handleFailure, ::handleSuccess, model as Any)
            model != null -> {
                isLastAction = index + 1 == useCaseQueue.size
                val item = useCaseQueue[index]
                item.execute(::handleFailure, ::handleUseCase, model)
            }
            else -> handleFailure(Failure.NullInQueue(index))
        }
        index++
    }


    private fun handleFailure(fail: Failure) {
        loadingLiveData.postValue(false)
        failureLiveData.postValue(fail)
    }


    @Suppress("UNCHECKED_CAST")
    private fun <T> handleSuccess(model: T) {

        loadingLiveData.postValue(false)
        liveData.postValue(mappingFunction(model as LastModel))
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

    class ActionQueueItem<Params : ActionParams, out Model : Any, OldModel : Any> internal constructor(private val useCase: UseCase<Model, Params>, private val handleMapping: ((OldModel) -> Params)? = null) {


        @Suppress("UNCHECKED_CAST")
        fun execute(handleFailure: (Failure) -> Unit, handleSuccess: (Any) -> Unit, model: Any) {
            useCase.execute({ it.either(handleFailure, handleSuccess) }, handleMapping!!(model as OldModel))

        }

        @Suppress("UNCHECKED_CAST")
        fun executeWithoutMapping(handleFailure: (Failure) -> Unit, handleSuccess: (Any) -> Unit, params: Any) {
            useCase.execute({ it.either(handleFailure, handleSuccess) }, params as Params)

        }
    }


    class ActionQueueBuilderMappingUiModel<Params : ActionParams, UiModel : Any, Model : Any> internal constructor(private val actionQueue: ActionQueue<Params, UiModel, Model>) {
        /**
         * Set map function for [ActionQueue]
         * @param handleResult function for mapping Model to UiModel
         *
         * @return [Builder] instance
         */
        fun buildWithUiModel(handleResult: (Model) -> UiModel): ActionQueue<Params, UiModel, Model> {
            actionQueue.mappingFunction = handleResult
            return actionQueue

        }

    }


    class ActionQueueBuilderUseCase<Params : ActionParams, UiModel : Any, OldModel : Any> internal constructor(private val actionQueue: ActionQueue<Params, UiModel, Any>) {
        /**
         * Add a use case that will execute after previous use case
         *
         *
         * @param useCaseClass java class of use case
         * @param mapping function that mapping previous model use case result with params for this
         * use case
         * @return [Builder] instance
         */


        fun <Model : Any, ParamsUseCase : ActionParams, T : UseCase<Model, ParamsUseCase>> addUseCase(useCaseClass: Class<T>, mapping: (OldModel) -> ParamsUseCase): ActionQueueBuilderUseCase<Params, UiModel, Model> {
            actionQueue.useCaseQueue.add(ActionQueueItem(useCaseClass.newInstance(), mapping))

            return ActionQueueBuilderUseCase(actionQueue)

        }

        /**
         * Add a use case that will execute after previous use case
         *
         *
         * @param run function for use case
         * @param mapping function that mapping previous model use case result with params for this
         * use case
         *
         * @return [Builder] instance

         */
        fun <Model : Any, ParamsUseCase : ActionParams> addUseCase(run: (ParamsUseCase) -> Either<Failure, Model>, mapping: (OldModel) -> ParamsUseCase): ActionQueueBuilderUseCase<Params, UiModel, Model> {
            val useCase = object : UseCase<Model, ParamsUseCase>() {
                override suspend fun run(params: ParamsUseCase): Either<Failure, Model> {
                    return run.invoke(params)
                }
            }
            actionQueue.useCaseQueue.add(ActionQueueItem(useCase, mapping))
            return ActionQueueBuilderUseCase(actionQueue)


        }

        /**
         * Set the last use case that will execute with the function define in [mappingFunction]
         *
         *
         * @param useCaseClass java class of use case
         * @param mapping function that mapping previous model use case result with params for this
         * use case
         *
         * @return [Builder] instance

         */


        fun <ParamsUseCase : ActionParams, Model : Any, T : UseCase<Model, ParamsUseCase>> setLastUseCase(useCaseClass: Class<T>, mapping: (OldModel) -> ParamsUseCase): ActionQueueBuilderMappingUiModel<Params, UiModel, Model> {
            val finalActionQueue = ActionQueue<Params, UiModel, Model>()

            finalActionQueue.lastUseCase = ActionQueueItem(useCaseClass.newInstance(), mapping)
            finalActionQueue.useCaseQueue.addAll(actionQueue.useCaseQueue)

            return ActionQueueBuilderMappingUiModel(finalActionQueue)
        }

        /**
         * Set the last use case that will execute with the function define in [mappingFunction]
         *
         *
         * @param run function for use case
         * @param mapping function that mapping previous model use case result with params for this
         * use case
         *
         * @return [ActionQueueBuilder] instance

         */
        fun <Model : Any, ParamsUseCase : ActionParams> setLastUseCase(run: (ParamsUseCase) -> Either<Failure, Model>, mapping: (OldModel) -> ParamsUseCase): ActionQueueBuilderMappingUiModel<Params, UiModel, Model> {
            val useCase = object : UseCase<Model, ParamsUseCase>() {
                override suspend fun run(params: ParamsUseCase): Either<Failure, Model> {
                    return run.invoke(params)
                }
            }
            val finalActionQueue = ActionQueue<Params, UiModel, Model>()

            finalActionQueue.lastUseCase = ActionQueueItem(useCase, mapping)
            finalActionQueue.useCaseQueue.addAll(actionQueue.useCaseQueue)

            return ActionQueueBuilderMappingUiModel(finalActionQueue)


        }


    }


    /**
     * Use this class for create a instance of [ActionQueue]
     */
    class Builder<Params : ActionParams, UiModel : Any> {
        private val actionQueue = ActionQueue<Params, UiModel, Any>()


        /**
         * Set the first use case that will execute
         *
         *
         * @param useCaseClass java class of use case
         * use case
         * @return [Builder] instance
         */


        fun <Model : Any, T : UseCase<Model, Params>> setFirstUseCase(useCaseClass: Class<T>): ActionQueueBuilderUseCase<Params, UiModel, Model> {
            actionQueue.useCaseQueue.add(0, ActionQueueItem<Params, Model, Model>(useCaseClass.newInstance()))

            return ActionQueueBuilderUseCase(actionQueue)
        }

        /**
         * Set the first use case that will execute
         *
         *
         * @param run function for use case
         * use case
         * @return [Builder] instance
         */
        fun <Model : Any> setFirstUseCase(run: (Params) -> Either<Failure, Model>): ActionQueueBuilderUseCase<Params, UiModel, Model> {
            val useCase = object : UseCase<Model, Params>() {
                override suspend fun run(params: Params): Either<Failure, Model> {
                    return run.invoke(params)
                }
            }
            actionQueue.useCaseQueue.add(0, ActionQueueItem<Params, Model, Model>(useCase))
            return ActionQueueBuilderUseCase(actionQueue)


        }


    }
}