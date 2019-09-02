package com.sysdata.kt.ktandroidarchitecture

import com.sysdata.kt.ktandroidarchitecture.repository.AuthRepository
import com.sysdata.kt.ktandroidarchitecture.repository.model.UserLogged
import com.sysdata.kt.ktandroidarchitecture.usecase.LoginActionParams
import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.functional.map
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class AuthRepositoryTest {

    lateinit var repo: AuthRepository
    private val latch = CountDownLatch(1)

    @Before
    fun setup() {
        repo = AuthRepository.instance
    }

    @Test
    fun login() {
        val email = "email"
        val password = "test"
        val result = repo.login(email, password)
        assert(result.isRight)
        var userLogged: UserLogged? = null
        result.map { res -> userLogged = res }
        latch.await(200, TimeUnit.MILLISECONDS)
        assertEquals(UserLogged(email), userLogged)
    }

    @Test
    fun runFailUsernameEmptyLogin() {
        val email = ""
        val password = "test"
        var failureTest: Failure? = null

        val result = repo.login(email, password)
        result.either({
            failureTest = it
            it
        }, {})
        latch.await(200, TimeUnit.MILLISECONDS)
        assert(result.isLeft)
        assert(failureTest is Failure.NetworkConnection)

    }


    @Test
    fun runFailPasswordEmptyUseCase() {
        val email = "email"
        val password = ""
        var failureTest: Failure? = null

        val result = repo.login(email, password)
        result.either({
            failureTest = it
            it
        }, {})
        latch.await(200, TimeUnit.MILLISECONDS)
        assert(result.isLeft)
        assert(failureTest is Failure.NetworkConnection)

    }
}