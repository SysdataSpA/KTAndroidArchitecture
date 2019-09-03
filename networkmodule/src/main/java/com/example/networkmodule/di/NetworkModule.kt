package com.example.networkmodule.di

import com.example.networkmodule.api.api.GitHubServiceAPI
import org.koin.dsl.module

val networkModule = module {
    single { GitHubServiceAPI.getGitHubService() }
}