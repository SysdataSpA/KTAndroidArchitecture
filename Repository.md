
# KTAndroidArchitecture - REPOSITORY

### 1.1 What is repository?
A Repository handles the process of saving or retrieving data from a datasource, it is managed by one or more UseCase.
The repository is a sort of data storage. 

The datas retrieved by a repository's call, can come from each one of this sources:
* Local Databases
* Remote Web Services
* Bluetooth connection with other devices
* Data from device sensors
* etc

Each data source has to be called by the repository.

![alt text](https://github.com/bbrends/KTAndroidArchitecture/blob/patch-1/repository.png)

### 1.2 How it works?

As shown in the image above, the flow of retreiving data through repository is pretty linear:
* the UseCase invoke a repository's method which handles the data retrieving (The implementation changes with the data source)
* the repository's method retrieve the datas (i.e. calling a web service through retrofit)
* Is created an instance of Either object which wrap the response or the error object
* the Either object goes to the UseCase which has called the repository

### 1.3 Either

As seen in the previous paragraph, the Either object has the function to convey a data model in case of successfully data retreiving, or return an error object in case of failure.

Use the object is very simple:

```
suspend inline fun fakeCall(param: String? = null): Either<Failure, SignInResponse> {

        val bodyRequest = RequestModel().param(param)

        return try {

            val response = adapt(ApiClient.INSTANCE.getSampleApi().getService(bodyRequest)).await()

            Either.Right(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Either.Left(Failure.ServerError(e.message))
        }
    }
```

When success, you need to invoke the method "Right" with the mapped data model.
When failure, you need to invoke the method "Left" with the model of the error(in the example above the class ServerError in Failure.kt). 
If you need a custom error model, you have to extend the class FeatureFailure.

### 1.4 Sample

Below a complete example of Repository:

```
class SampleRepo : BaseRepository() {

    /**
     * Used to recall instance only whe it need
     */
    private object Holder {
        val INSTANCE = SampleRepo()
    }

    /**
     * Used to make singleton pattern
     */
    companion object {
        val instance: SampleRepo by lazy {
            Holder.INSTANCE
        }
    }

    /**
     * perform a retriveDataCall request
     *
     * @param param
     *
     * @return Either
     */
    suspend inline fun retriveDataCall(param: String? = null): Either<Failure, SignInResponse> {

        val bodyRequest = RequestModel().param(param)

        return try {

            val response = adapt(ApiClient.INSTANCE.getSampleApi().getService(bodyRequest)).await()

            Either.Right(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Either.Left(Failure.ServerError(e.message))
        }
    }
}
```
