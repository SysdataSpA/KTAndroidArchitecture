package com.sysdata.kt.ktandroidarchitecture.viewmodel

import com.sysdata.kt.ktandroidarchitecture.repository.model.UIUserLogged
import com.sysdata.kt.ktandroidarchitecture.repository.model.UserLogged
import com.sysdata.kt.ktandroidarchitecture.ui.Note
import com.sysdata.kt.ktandroidarchitecture.ui.NoteDataSource
import com.sysdata.kt.ktandroidarchitecture.usecase.LoginActionParams
import com.sysdata.kt.ktandroidarchitecture.usecase.LoginUseCase
import it.sysdata.ktandroidarchitecturecore.interactor.*
import it.sysdata.ktandroidarchitecturecore.platform.BaseViewModel

class LoginViewModel: BaseViewModel() {

    val actionLogin = Action.Builder<LoginActionParams,UserLogged, UIUserLogged>()
            .useCase(LoginUseCase::class.java)
            .buildWithUiModel { UIUserLogged(it.username) }

    val channelNotes = DataSourceChannel.Builder<Int, Note>()
            .dataSource(NoteDataSource::class.java)
            .build()

    val channelPostNotes = Channel<Note>()
}