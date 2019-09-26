package com.sysdata.kt.ktandroidarchitecture.repository

import com.sysdata.kt.ktandroidarchitecture.repository.model.UserLogged
import it.sysdata.ktandroidarchitecturecore.BaseRepository


interface AuthRepository {
    fun login(email: String, password: String): UserLogged

}

class AuthRepositoryImpl : BaseRepository(),AuthRepository {



  override fun login(email: String, password: String): UserLogged {
        if (email.isEmpty() || password.isEmpty())
            throw RuntimeException()

        return UserLogged(email)
    }
}