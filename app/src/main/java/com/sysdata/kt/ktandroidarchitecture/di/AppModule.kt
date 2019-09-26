package com.sysdata.kt.ktandroidarchitecture.di

import com.sysdata.kt.ktandroidarchitecture.repository.AuthRepository
import com.sysdata.kt.ktandroidarchitecture.repository.AuthRepositoryImpl
import com.sysdata.kt.ktandroidarchitecture.repository.GitHubRepo
import com.sysdata.kt.ktandroidarchitecture.repository.GitHubRepoImpl
import com.sysdata.kt.ktandroidarchitecture.usecase.GitHubUseCase
import com.sysdata.kt.ktandroidarchitecture.usecase.LoginUseCase
import com.sysdata.kt.ktandroidarchitecture.viewmodel.GitHubViewModel
import com.sysdata.kt.ktandroidarchitecture.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module{
    // single instance of AuthRepository
    single<AuthRepository>{AuthRepositoryImpl()}
    single<GitHubRepo>{GitHubRepoImpl(get())}

    // simple UseCase factory
    factory { LoginUseCase(get()) }
    factory { GitHubUseCase(get()) }

    // LoginViewModel ViewModel
    viewModel { LoginViewModel(get())}
    viewModel { GitHubViewModel(get()) }


}