package it.sysdata.ktandroidarchitecturecore.interactor

import it.sysdata.ktandroidarchitecturecore.exception.Failure
import it.sysdata.ktandroidarchitecturecore.functional.Either
import kotlinx.coroutines.experimental.*
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertFailsWith

class UseCaseTest {

    @Test
    fun testRun():Unit {
        runBlocking {
            val result = DivisionUseCase().run(DivActionParams(12,3))
            result.either(::handleFailure, ::handleSuccess)
        }
    }

    @Test
    fun testExecute(){
        runBlocking {
            DivisionUseCase().execute({ it.either(::handleFailure, ::handleSuccess) }, DivActionParams(12, 3))
            delay(1000,TimeUnit.MILLISECONDS)
        }
    }

    @Test
    fun testExecuteFailure() {
        assertFailsWith<ArithmeticException> {
            runBlocking {
                DivisionUseCase().execute({ it.either(::handleFailure, ::handleSuccess) }, DivActionParams(12, 0))
                delay(1000, TimeUnit.MILLISECONDS)
            }
        }
    }

    fun handleFailure(fail:Failure){
        Assert.assertThat(fail, CoreMatchers.instanceOf(DivisionByZero::class.java))
    }

    fun handleSuccess(res: Int){
        Assert.assertEquals(res, 4)
    }

    class DivisionUseCase: UseCase<Int, DivActionParams>() {
        override suspend fun run(params: DivActionParams): Either<Failure, Int> {
            val res = params.firstNumber / params.secondNumber
            return Either.Right(res)
        }

    }

    class DivisionByZero:Failure.FeatureFailure()

    data class DivActionParams(val firstNumber: Int, val secondNumber: Int) : ActionParams()
}