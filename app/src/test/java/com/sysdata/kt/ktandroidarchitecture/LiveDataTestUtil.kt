package com.sysdata.kt.ktandroidarchitecture

import com.jraska.livedata.TestLifecycle
import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.interactor.Action
import it.sysdata.ktandroidarchitecturecore.interactor.ActionParams
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Safely handles observables from LiveData for testing.
 */
object LiveDataTestUtil {

    /**
     * Gets the value of a LiveData safely.
     */
    @Throws(InterruptedException::class)
    fun <T : Any, Model : Any, Params : ActionParams> getValue(action: Action<Params, Model, T>, lifecycle: TestLifecycle): T? {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer: (T) -> Unit = { o ->
            data = o
            latch.countDown()

        }

        action.observe(lifecycle, observer)

        latch.await(2, TimeUnit.SECONDS)

        return data
    }

    @Throws(InterruptedException::class)
    fun < Model : Any, Params : ActionParams,UiModel:Any> getFail(action: Action<Params, Model, UiModel>, lifecycle: TestLifecycle): Failure? {
        var data: Failure? = null
        val latch = CountDownLatch(1)
        val observer: (T:Failure) -> Unit = { o ->
            data = o
            latch.countDown()

        }

        action.observeFailure(lifecycle, observer)

        latch.await(2, TimeUnit.SECONDS)

        return data
    }

}