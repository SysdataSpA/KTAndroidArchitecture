package com.sysdata.kt.ktandroidarchitecture

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.TestLifecycle
import com.sysdata.kt.ktandroidarchitecture.repository.model.UIUserLogged
import com.sysdata.kt.ktandroidarchitecture.viewmodel.LoginViewModel
import it.sysdata.ktandroidarchitecturecore.exception.Failure
import junit.framework.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations


class LoginViewModelTest {
    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testLogin() {
        val testLifecycle = TestLifecycle.initialized()

        val loginViewModelTest = LoginViewModel()
        val email = "email"
        val password = "test"

        loginViewModelTest.login(email, password)

        testLifecycle.resume()


        val result = LiveDataTestUtil.getValue(loginViewModelTest.actionLogin, testLifecycle)

        assertEquals(UIUserLogged(email), result)


    }

    @Test
    fun testLoginWithNoValue() {
        val testLifecycle = TestLifecycle.initialized()

        val loginViewModelTest = LoginViewModel()
        val email = ""
        val password = ""

        loginViewModelTest.login(email, password)

        testLifecycle.resume()


        val result = LiveDataTestUtil.getFail(loginViewModelTest.actionLogin, testLifecycle)
        assertTrue(result is Failure.NetworkConnection)


    }
}
