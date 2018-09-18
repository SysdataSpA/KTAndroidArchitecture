
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

