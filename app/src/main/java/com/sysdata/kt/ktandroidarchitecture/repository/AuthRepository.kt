package com.sysdata.kt.ktandroidarchitecture.repository

import com.sysdata.kt.ktandroidarchitecture.repository.model.UserLogged
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
}