package com.sysdata.kt.ktandroidarchitecture

import com.example.networkmodule.di.networkModule
import com.sysdata.kt.ktandroidarchitecture.di.appModule
import org.junit.Test
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class CheckModulesTest : KoinTest {

    @Test
    fun checkAllModules() {
        koinApplication { modules(listOf(appModule, networkModule) )}.checkModules()
    }

}