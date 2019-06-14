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
package com.sysdata.kt.ktandroidarchitecture.usecase

import com.sysdata.kt.ktandroidarchitecture.repository.AuthRepository
import com.sysdata.kt.ktandroidarchitecture.repository.model.UserLogged
import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.functional.Either
import it.sysdata.ktandroidarchitecturecore.interactor.UseCase

/**
 * Fake login use case that calls a mock repository
 */
class LoginUseCase: UseCase<UserLogged, LoginActionParams>() {
    override suspend fun run(params: LoginActionParams): Either<Failure, UserLogged> {
        return AuthRepository.instance.login(params.email, params.password)
    }
}