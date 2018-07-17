package com.sysdata.kt.ktandroidarchitecture

import android.app.Application


class MainApplication : Application() {


    companion object {
        lateinit var INSTANCE: MainApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        MainApplicationConfig.instance
    }


}