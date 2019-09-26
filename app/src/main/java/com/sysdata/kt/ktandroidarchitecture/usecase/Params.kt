package com.sysdata.kt.ktandroidarchitecture.usecase

import it.sysdata.ktandroidarchitecturecore.interactor.ActionParams

class None : ActionParams()
data class LoginActionParams(val email: String, val password: String) : ActionParams()

data class GitHubActionParams(val user: String) : ActionParams()