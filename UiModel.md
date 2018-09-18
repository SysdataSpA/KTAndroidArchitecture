# KTAndroidArchitecture - UIMODEL

### 1.1 What is UiModel?
A UIModel is a object which contains all UI-related datas of a view, a fragment or an activity

To fully understand the concept of UiModel, let's do a step back:
think about a normal flow of retrieving and showing datas.

The components of this flow will be a server, which contains the datas, and a client, which shows them.
Likely, the server will be accessed by clients different from ours;
so the datas returned by the server could be dirty, with unnecessary information or parameters which needs to be elaborated after the retrieving.

This takes us to treat the datas from the web service as "RAW" datas.

Our graphic interface, instead, need specific and well mapped datas.

There is the need to create a data model only for the graphic interface, ignoring all unnecessary datas and elaborating others before passing them to the view.

![alt text](https://github.com/bbrends/KTAndroidArchitecture/blob/patch-1/uimodel.png)

### 1.2 When does it happen?

The trasformation from raw web service's object to uiModel is done into the ViewModel.
Specifically, into the Action flow, after the usecase, inside the "buildWithUiModel" method:

```
val actionSample = Action.Builder<ActionParams,Model,UiModel>()
            .useCase(SampleUseCase::class.java)
            .buildWithUiModel {
                //map model into UiModel
                UiModel(it)
            }

```


### 1.3 DataBinding

We have almost ended the flow of retrieving and showing datas.
There is only the transition of the uiModel from the ViewModel to the view.

Coming back to the idea of Action, inside the view you need to set an observer of the action's result:

```
mViewModel?.actionSample?.observe(this, ::onSampleObserver)
```

This observer make possible to listen the action's result and set the UiModel inside the view.

This operation is called DataBinding:
a uimodel is injected directly into the view.

```
fun onSampleObserver(uiModel: UIModel?) {
        uiModel?.let {
            binding.uiModel = it
            binding.executePendingBindings()
        }
    }
```


