# KTAndroidArchitecture - UIMODEL

### 1.1 What is UiModel?
A UIModel is a object which contains all UI-related datas of a view, a fragment or an activity

Per capire il concetto di UiModel, facciamo un passo indietro:
immaginiamo un normale flusso di recupero e visualizzazione dei dati.
I componenti di questo flusso saranno quindi un server che contiene i dati ed il client che li visualizza.
Verosimilmente, il server potrebbe servire altri client oltre al nostro che avranno esigenze di visualizzazione/logica diverse;
ne consegue che i dati tornati potrebbero essere sporchi, ci potrebbero essere informazioni superflue oppure per visualizzare un dato dobbiamo compiere
un'operazione di merge di vari parametri.
Questo ci porta ad intendere i dati che ci arrivano dal web service come grezzi: "RAW".

Nella nostra interfaccia grafica, invece, occorrono dati ben precisi e ben mappati.
Nasce quindi la necessità di creare un modello dei dati che serva unicamente l'interfaccia grafica,
scartando tutto quello che non serve e effettuando tutte le elaborazioni dei dati necessari prima che
questi arrivino alla view.

![alt text](https://github.com/bbrends/KTAndroidArchitecture/blob/patch-1/uiModel.png)

### 1.2 When does it happen?



### 1.3 Bind
