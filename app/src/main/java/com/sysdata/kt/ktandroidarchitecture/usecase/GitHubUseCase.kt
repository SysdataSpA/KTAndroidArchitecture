package com.sysdata.kt.ktandroidarchitecture.usecase

import com.example.networkmodule.api.model.Repo
import com.sysdata.kt.ktandroidarchitecture.repository.GitHubRepo
import com.sysdata.kt.ktandroidarchitecture.repository.model.UserLogged
import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.functional.Either
import it.sysdata.ktandroidarchitecturecore.interactor.UseCase

class GitHubUseCase(private val repo: GitHubRepo) : UseCase< List<Repo>, GitHubActionParams>() {
    override suspend fun run(params: GitHubActionParams): Either<Failure, List<Repo>> {
        return try {
            Either.Right(repo.getRepositoryByUser(params.user))
        } catch (e: Exception) {
            Either.Left(Failure.NetworkConnection())
        }
    }
}