package it.sysdata.ktandroidarchitecturecore.platform

import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.functional.Either
import kotlinx.coroutines.Deferred

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