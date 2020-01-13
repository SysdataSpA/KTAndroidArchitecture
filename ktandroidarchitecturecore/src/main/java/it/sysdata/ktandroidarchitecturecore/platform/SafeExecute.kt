package it.sysdata.ktandroidarchitecturecore.platform

import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.functional.Either
import kotlinx.coroutines.Deferred

/**
 * This class is used as a default implementation for the safe execute behaviour.
 * Whenever an exception is thrown, this will be wrapped inside a [Failure.InternalError] failure.
 * If you want to define a custom behaviour for all the exception, you can define a custom class that inherits from [SafeExecuteInterface]
 */
class SafeExecute : SafeExecuteInterface {


    override suspend fun <Type> safeExecute(job: Deferred<Either<Failure, Type>>): Either<Failure, Type> {
        return try {
            job.await()

        } catch (e: Exception) {
            e.printStackTrace()
            Either.Left(Failure.InternalError(e.message))
        }
    }
}