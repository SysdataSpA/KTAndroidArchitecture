package com.sysdata.kt.ktandroidarchitecture

import android.app.Application
import com.example.networkmodule.di.networkModule
import com.sysdata.kt.ktandroidarchitecture.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class MainApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MainApplication)
            modules(listOf(appModule, networkModule))
        }
    }


}