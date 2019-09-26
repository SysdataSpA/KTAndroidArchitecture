package com.sysdata.kt.ktandroidarchitecture

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sysdata.kt.ktandroidarchitecture.di.appModule
import com.sysdata.kt.ktandroidarchitecture.repository.model.UserLogged
import com.sysdata.kt.ktandroidarchitecture.usecase.LoginActionParams
import com.sysdata.kt.ktandroidarchitecture.usecase.LoginUseCase
import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.functional.map
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@ExperimentalCoroutinesApi
class LoginUseCaseTest : KoinTest {


    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val useCase: LoginUseCase by inject()
    @Before
    fun before() {
        startKoin {
            printLogger()
            modules(appModule)
        }
    }
    @After
    fun after() {
        stopKoin()
    }
    @Test
    fun runUseCase() = runBlockingTest {
        val email = "email"
        val password = "test"
        val params = LoginActionParams(email, password)
        val result = useCase.run(params)
        assert(result.isRight)
        var userLogged: UserLogged? = null
        result.map { res -> userLogged = res }
        delay(200)
        assertEquals(UserLogged(email), userLogged)
    }

    @Test
    fun runFailUsernameEmptyUseCase() = runBlockingTest {
        val email = ""
        val password = "test"
        var failureTest: Failure? = null

        val params = LoginActionParams(email, password)
        val result = useCase.run(params)
        result.either({
            failureTest = it
            it
        }, {})
        delay(200)
        assert(result.isLeft)
        assert(failureTest is Failure.NetworkConnection)

    }


    @Test
    fun runFailPasswordEmptyUseCase() = runBlockingTest {
        val email = "email"
        val password = ""
        var failureTest: Failure? = null

        val params = LoginActionParams(email, password)
        val result = useCase.run(params)
        result.either({
            failureTest = it
            it
        }, {})
        delay(200)
        assert(result.isLeft)
        assert(failureTest is Failure.NetworkConnection)

    }
}