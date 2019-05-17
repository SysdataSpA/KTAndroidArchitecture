package com.sysdata.kt.ktandroidarchitecture.repository

import com.sysdata.kt.ktandroidarchitecture.DIUtils
import com.sysdata.kt.ktandroidarchitecture.repository.model.UserLogged
import com.sysdata.kt.ktandroidarchitecture.ui.Note
import it.sysdata.ktandroidarchitecturecore.BaseRepository
import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.functional.Either

class AuthRepository:BaseRepository() {

    private object Holder {
        val INSTANCE = AuthRepository()
    }

    companion object {
        val instance: AuthRepository by lazy {
            Holder.INSTANCE
        }
    }

    fun login(email:String, password:String):Either<Failure, UserLogged>{
        return if(email.isEmpty() || password.isEmpty())
            Either.Left(Failure.NetworkConnection())
        else
            Either.Right(UserLogged(email))
    }

/*  TODO: check this code
    private val datas = listOf(Note(1), Note(2), Note(3), Note(4), Note(5))

    fun startSendingNotes(interval: Int = 5000){
        //TODO: track index and start recurring each 5 seconds
        //DIUtils.channel.postData()
//        DIUtils.dataSourceChannel.observeLoadDataRequest()
    }
    */
}