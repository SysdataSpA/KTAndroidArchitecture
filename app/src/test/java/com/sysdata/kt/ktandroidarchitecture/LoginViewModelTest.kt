package com.sysdata.kt.ktandroidarchitecture

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.TestLifecycle
import com.sysdata.kt.ktandroidarchitecture.di.appModule
import com.sysdata.kt.ktandroidarchitecture.repository.model.UIUserLogged
import com.sysdata.kt.ktandroidarchitecture.viewmodel.LoginViewModel
import it.sysdata.ktandroidarchitecturecore.exception.Failure
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject


class LoginViewModelTest : KoinTest {
    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val loginViewModelTest: LoginViewModel by inject()

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
    fun `testLogin`() {
        val testLifecycle = TestLifecycle.initialized()

        val email = "email"
        val password = "test"

        loginViewModelTest.login(email, password)

        testLifecycle.resume()


        val result = LiveDataTestUtil.getValue(loginViewModelTest.actionLogin, testLifecycle)

        assertEquals(UIUserLogged(email), result)


    }

    @Test
    fun `testLoginWithNoValue`() {
        val testLifecycle = TestLifecycle.initialized()

        val email = ""
        val password = ""

        loginViewModelTest.login(email, password)

        testLifecycle.resume()


        val result = LiveDataTestUtil.getFail(loginViewModelTest.actionLogin, testLifecycle)
        assertTrue(result is Failure.NetworkConnection)


    }
}
