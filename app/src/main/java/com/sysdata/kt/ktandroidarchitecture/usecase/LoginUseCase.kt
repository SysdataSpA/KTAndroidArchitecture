package com.sysdata.kt.ktandroidarchitecture.usecase

import com.sysdata.kt.ktandroidarchitecture.repository.AuthRepository
import com.sysdata.kt.ktandroidarchitecture.repository.AuthRepositoryImpl
import com.sysdata.kt.ktandroidarchitecture.repository.model.UserLogged
import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.functional.Either
import it.sysdata.ktandroidarchitecturecore.interactor.UseCase

class LoginUseCase(private val repo:AuthRepository) : UseCase<UserLogged, LoginActionParams>() {
    override suspend fun run(params: LoginActionParams): Either<Failure, UserLogged> {
        return try {
            Either.Right(repo.login(params.email, params.password))
        } catch (e: Exception) {
            Either.Left(Failure.NetworkConnection())
        }
    }
}