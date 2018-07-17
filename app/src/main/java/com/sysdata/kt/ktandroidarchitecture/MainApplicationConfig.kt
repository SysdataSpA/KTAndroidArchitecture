package com.sysdata.kt.ktandroidarchitecture

class MainApplicationConfig private constructor() {

    private object Holder {
        val INSTANCE = MainApplicationConfig()
    }

    companion object {
        val instance: MainApplicationConfig by lazy {
            Holder.INSTANCE
        }
    }
}