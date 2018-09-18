
# KTAndroidArchitecture - REPOSITORY

### 1.1 What is repository?
A Repository handles the process of saving or retrieving data from a datasource, it is managed by one or more UseCase.
Il repository equivale al concetto di contenitore di dati. 

I dati risposti da una chiamata al repository, possono essere recuperati da qualsiasi sorgente:
* Database Locale
* Web Service Remoti
* Connessione Bluetooth con altri dispositivi
* Recupero dati dai sensori del device
* etc

Qualsiasi sorgente di dati deve essere invocata dal repository.

![alt text](https://github.com/bbrends/KTAndroidArchitecture/blob/patch-1/repository.png)

### 1.2 How it works?

Come mostrato nell'immagine sopra, il flusso di recupero dati tramite repository è piuttosto lineare:
* lo UseCase invoca un metodo del repository che si occupa del recupero dei dati (implementazione diversa a seconda della tipologia di sorgente dati)
* avviene il recupero dei dati (ad esempio al web service tramite retrofit)
* viene creato un oggetto Either che effettua il Wrap dell'oggetto di risposta o dell'oggetto di errore
* l'oggetto Either viene tornato allo UseCase che ha invocato il repository

### 1.3 Either

Come visto nel paragrafo precedente, l'oggetto Either ha la funzione di veicolare un modello di dati in caso di recupero avvenuto con successo, oppure di restituire un oggetto di errore in caso di fallimento.

La modalità di utilizzo è molto semplice:

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

In caso di success, occorre invocare il metodo "Right" con il modello dei dati mappato.
In caso di failure, occorre invocare il metodo "Left" con il modello dell'errore (in questo caso la classe ServerError presente in Failure.kt). 
Se è presente l'esigenza di creare modelli di errori custom, è sufficiente creare una classe che estenda FeatureFailure.

### 1.4 Sample

Di seguito un esempio completo di Repository:

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
