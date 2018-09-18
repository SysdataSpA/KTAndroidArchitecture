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

![alt text](https://github.com/bbrends/KTAndroidArchitecture/blob/patch-1/uimodel.png)

### 1.2 When does it happen?

La trasformazione da oggetto grezzo proveniente dal web service a uiModel avviene nel ViewModel.
Nello specifico, all'interno del flusso di Action, in coda al risultato dello UseCase, all'interno del metodo "buildWithUiModel":

```
val actionSample = Action.Builder<ActionParams,Model,UiModel>()
            .useCase(SampleUseCase::class.java)
            .buildWithUiModel {
                //map model into UiModel
                UiModel(it)
            }

```


### 1.3 DataBinding

Abbiamo quasi concluso il flusso di recupero e visualizzazione dei dati.
Manca solo il passaggio dello uiModel dal ViewModel alla view.

Riprendendo il concetto di Action, all'interno della view occorre settare un observer del
risultato dell'azione:

```
mViewModel?.actionSample?.observe(this, ::onSampleObserver)
```

Tale observer ci permetterà di rimanere in ascolto del risultato della action e di settare lo UiModel
all'interno della view.

DataBinding è il nome di questa operazione:
viene iniettato lo uimodel direttamente all'interno della view.

```
fun onSampleObserver(uiModel: UIModel?) {
        uiModel?.let {
            binding.uiModel = it
            binding.executePendingBindings()
        }
    }
```


