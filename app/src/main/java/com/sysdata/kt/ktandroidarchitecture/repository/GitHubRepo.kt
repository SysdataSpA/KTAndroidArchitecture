package com.sysdata.kt.ktandroidarchitecture.repository

import com.example.networkmodule.api.api.GitHubService
import com.example.networkmodule.api.model.Repo

interface GitHubRepo {
    suspend fun getRepositoryByUser(user: String): List<Repo>

}

class GitHubRepoImpl(private val api: GitHubService) : GitHubRepo {

    override suspend fun getRepositoryByUser(user: String): List<Repo> {
        return api.listRepos(user)
    }
}