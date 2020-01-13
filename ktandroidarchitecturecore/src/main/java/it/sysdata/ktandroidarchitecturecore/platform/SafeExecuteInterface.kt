package it.sysdata.ktandroidarchitecturecore.platform

import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.functional.Either
import kotlinx.coroutines.Deferred

/**
 * This interface is used for defining an implementation when the Usecase throws an exception.
 * By default, the ktAndroid architecture will wrap the safe execute behaviour with the [SafeExecute] implementation.
 */
interface SafeExecuteInterface {

     suspend fun  <Type>safeExecute(job: Deferred<Either<Failure, Type>>): Either<Failure, Type> }