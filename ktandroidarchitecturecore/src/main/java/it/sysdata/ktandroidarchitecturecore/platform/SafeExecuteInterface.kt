package it.sysdata.ktandroidarchitecturecore.platform

import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.functional.Either
import kotlinx.coroutines.Deferred

interface SafeExecuteInterface {

     suspend fun  <Type>safeExecute(job: Deferred<Either<Failure, Type>>): Either<Failure, Type> }