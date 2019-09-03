package com.sysdata.kt.ktandroidarchitecture

import com.example.networkmodule.api.api.GitHubService
import com.example.networkmodule.di.networkModule
import com.sysdata.kt.ktandroidarchitecture.di.appModule
import com.sysdata.kt.ktandroidarchitecture.repository.GitHubRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given

@ExperimentalCoroutinesApi
class GitHubRepositoryTest : KoinTest {
    private val repositoryTest: GitHubRepo by inject()


    @Before
    fun before() {
        startKoin {


            modules(listOf(appModule, networkModule))
        }
        declareMock<GitHubService> {
            runBlocking {
                given(this@declareMock.listRepos("SysdataSpA")).willReturn(listOf())
            }
        }


    }
    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun testGitListRepoByUser(){
        runBlockingTest {
            val list = repositoryTest.getRepositoryByUser("SysdataSpA")
            assert(list.isEmpty())
        }
    }

}