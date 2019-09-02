package com.sysdata.kt.ktandroidarchitecture

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sysdata.kt.ktandroidarchitecture.repository.model.UserLogged
import com.sysdata.kt.ktandroidarchitecture.usecase.LoginActionParams
import com.sysdata.kt.ktandroidarchitecture.usecase.LoginUseCase
import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.functional.map
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginUseCaseTest {

    // Set the main coroutines dispatcher for unit testing
    @get:Rule
    var coroutinesRule = MainCoroutineRule()
    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var useCase: LoginUseCase
    @Before
    fun setup() {
        useCase = LoginUseCase()
    }

    @Test
    fun runUseCase() = runBlocking {
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
    fun runFailUsernameEmptyUseCase() = runBlocking {
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
    fun runFailPasswordEmptyUseCase() = runBlocking {
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