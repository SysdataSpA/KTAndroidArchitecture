package com.sysdata.kt.ktandroidarchitecture.viewmodel

import com.sysdata.kt.ktandroidarchitecture.repository.model.UserLogged
import com.sysdata.kt.ktandroidarchitecture.usecase.LoginActionParams
import com.sysdata.kt.ktandroidarchitecture.usecase.LoginUseCase
import it.sysdata.ktandroidarchitecturecore.interactor.Action
import it.sysdata.ktandroidarchitecturecore.platform.BaseViewModel

class LoginViewModel: BaseViewModel() {

    val actionLogin = Action.Builder<LoginActionParams,UserLogged,UserLogged>()
            .useCase(LoginUseCase::class.java)
            .buildWithUiModel { it }
}