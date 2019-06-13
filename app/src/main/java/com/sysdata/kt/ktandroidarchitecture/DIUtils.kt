package com.sysdata.kt.ktandroidarchitecture

import com.sysdata.kt.ktandroidarchitecture.ui.Note
import it.sysdata.ktandroidarchitecturecore.interactor.Channel

class DIUtils {

    private object Holder {
        val INSTANCE = Channel<Note>()
    }

    companion object {
        val channel: Channel<Note> by lazy {
            Holder.INSTANCE
        }
    }
}