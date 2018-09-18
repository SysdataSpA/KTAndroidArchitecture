
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
* a partire dallo UseCase, viene invocato un metodo del repository che si occupa del recupero dei dati (implementazione diversa a seconda della tipologia di sorgente dati)
* viene effettuato la chiamata (ad esempio al web service tramite retrofit)
* viene creato un oggetto Either che effettua il Wrap dell'oggetto di risposta
* l'oggetto Either viene tornato allo UseCase che ha invocato il repository
