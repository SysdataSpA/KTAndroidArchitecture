/**
 * Copyright (C) 2019 Sysdata S.p.a.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sysdata.kt.ktandroidarchitecture.repository

import com.sysdata.kt.ktandroidarchitecture.repository.model.UserLogged
import it.sysdata.ktandroidarchitecturecore.BaseRepository
import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.functional.Either

/**
 * Mock implementation of a repository
 */
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