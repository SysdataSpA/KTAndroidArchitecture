package com.sysdata.kt.ktandroidarchitecture

import com.sysdata.kt.ktandroidarchitecture.di.appModule
import com.sysdata.kt.ktandroidarchitecture.repository.AuthRepository
import com.sysdata.kt.ktandroidarchitecture.repository.model.UserLogged
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

class AuthRepositoryTest : KoinTest {

    val repo: AuthRepository by inject()


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
    fun login() {
        val email = "email"
        val password = "test"
        val result = repo.login(email, password)
        assertEquals(UserLogged(email), result)
    }

    @Test
    fun runFailUsernameEmptyLogin() {
        val email = ""
        val password = "test"
        try {
            val result = repo.login(email, password)

        } catch (e: Exception) {

            assert(e is RuntimeException)
        }

    }


    @Test
    fun `runFailPasswordEmptyLogin`() {
        val email = "email"
        val password = ""
        try {
            val result = repo.login(email, password)

        } catch (e: Exception) {

            assert(e is RuntimeException)
        }

    }
}


