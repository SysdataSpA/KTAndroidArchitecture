package com.sysdata.kt.ktandroidarchitecture.viewmodel

import androidx.lifecycle.ViewModel
import com.example.networkmodule.api.model.Repo
import com.sysdata.kt.ktandroidarchitecture.usecase.GitHubActionParams
import com.sysdata.kt.ktandroidarchitecture.usecase.GitHubUseCase
import it.sysdata.ktandroidarchitecturecore.interactor.Action

class GitHubViewModel(gitHubUseCase: GitHubUseCase) : ViewModel() {
    val gitHubAction = Action.Builder<GitHubActionParams, List<Repo>, List<Repo>>()
            .useCase(gitHubUseCase)
            .buildWithUiModel { it }

}