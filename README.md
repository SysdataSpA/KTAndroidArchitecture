# KTAndroidArchitecture
A Kotlin android architecture with Google Architecture Components
## 1. A Brief Introduciton
The app is a sample project that shows how to implement the KTAndroidArchitecture into your Android app.

* [1.1 What is KTAndroidArchitecture?](README.md#L22)
* [2. How to use it?](README.md#L35)
   * [2.1 Import dependency](#import-dependency)
   * [2.2 Create a Repository](#create-a-repository)
   * [2.3 Create a UseCase](#create-a-usecase)
   * [2.4 Create a ViewModel](#create-a-viewmodel)
   * [2.5 Call the action](#call-the-action)
* [3. Main components](#main-components)








### 1.1 What is KTAndroidArchitecture?
It is a layer-based architecture that allows a real disentangle of the UI components from the business logic. 

![alt text](https://cdn-images-1.medium.com/max/800/1*I9WPcnpGNuI4CjxxrkP0-g.png "Simple Architecture Diagram")

The main components are:

* UI
* UIModel
* ViewModels with Livedata
* UseCase
* Repository

## 2. How to use it?

### 2.1 Import dependency

#### 2.1.1 in **Project level `build.gradle`** add this repository
```gradle
   maven { url  'https://dl.bintray.com/sysdata/maven' }
```
#### 2.1.2 in your **App level `build.gradle`** add this dependecy
```gradle
    implementation 'it.sysdata.mobile:ktandroidarchitecturecore:1.0.0'
```

### 2.2 Create a Repository
A repository needs just to extend **BaseRepository** in this way 
```kotlin
  class AuthRepository:BaseRepository() 
```

### 2.3 Create a UseCase
A usecase has to extend **UseCase<Out,In>** and implement a method run
```kotlin
class LoginUseCase: UseCase<UserLogged, LoginActionParams>() {
    override suspend fun run(params: LoginActionParams): Either<Failure, UserLogged> {
        do something
        return result
    }
}
```
The run function defined inside the UseCase can return a **Failure object** or a **Model object**.

The input params are defined in a Param object which is a data class defined like this
```kotlin
data class LoginActionParams(val email: String, val password: String) : ActionParams()
```

### 2.4 Create a ViewModel for your Activity/Fragment

A ViewModel needs to extend an abstract class BaseViewModel 
```kotlin
class LoginViewModel: BaseViewModel()
```

#### 2.4.1 Define an Action inside the ViewModel

An **Action** can be created by using a Builder like this
```kotlin
val actionLogin = Action.Builder<ActionParams,Model,UiModel>()
            .useCase(LoginUseCase::class.java)
            .buildWithUiModel { UiModel(it) }
```

![alt text](https://github.com/SysdataSpA/KTAndroidArchitecture/blob/develop/ActionFlowDiagram.png)

The flow have these steps:
1. the execution of an Action performed by the method execute(...) of Action class
2. the first logical step is the post of an object inside an internal livedata called LoadingLiveData indicating that loading has started; the UI can observe this LiveData using the method observeLoadingStatus(...)
3. the next step is the execution of a usecase which use repositories to retrieve some datas
4. the result of repositories' call returned to the usecase
5. the post of an object inside an internal livedata called LoadingLivedata indicating that loading has finished; the UI can observe this LiveData using the method observeLoadingStatus(...)
6. the post of the usecase result in two internal livedatas based on the success or the failure; the UI can observe these two LiveDatas by using observe(...) and observeFailure(...)

### 2.5 Call the Action from the Activity/Fragment

![alt text](https://github.com/SysdataSpA/KTAndroidArchitecture/blob/develop/UI_to_VM.png)

An action has several methods like:
- ``` action?.observe(...) ```, this method observe the success of the operation defined inside the usecase;
- ``` action?.observeFailure(...) ```, this method observe the failure of the operation; 
- ``` action?.observeLoadingStatus(...) ```, this method observe the loading state of the operation; 
- ``` action?.execute(...) ```, this method call the run function inside the usecase and execute the operation;

To call an action you have to write thisù
```kotlin
        viewModel?.action?.observe(this, ::onActionSuccess)
        viewModel?.action?.observeFailure(this, ::onActionFailed)
        viewModel?.action?.execute(Params)
```

## 3 KTAndroidArchitecture main components

### 3.1 UI

The UI layer of the architecture comprises activities, fragments and views. 

### 3.2 UIModel

A **UIModel** is a object which contains all UI-related datas of a view, a fragment or an activity

[Read More](UiModel.md)

### 3.3 ViewModels with Livedata

Each activity or fragment could have a **ViewModel** which is a object designed to store and manage UI-related data in a lifecycle conscious way by defining some **Actions** to call one or more **UseCases**

### 3.4 UseCase
A **UseCase** is a wrapper for a small business logic operation. A **UseCase** can use one or more **Repository** to get or write the requested data, then it returns the response event.

[Read More](UseCase.md)

### 3.5 Repository
A **Repository** handles the process of saving or retrieving data from a datasource, it is managed by one or more **UseCase**.

[Read More](Repository.md)

### 3.6 Action
An **Action** handles the process of calling a **UseCase** and map the response, generally an action use only one **UseCase** but is possible to define an **ActionQueue** to call multiple **UseCases** sequentially.
Into an **ActionQueue** each **UseCase**, except the first, take the result of the previous as parameters and give the output to the next.

**Action**

![alt text](https://github.com/SysdataSpA/KTAndroidArchitecture/blob/develop/actionSingleUseCase.png "Action")

```kotlin
val actionLogin = Action.Builder<ActionParams,Model,UiModel>()
            .useCase(LoginUseCase::class.java)
            .buildWithUiModel { UiModel(it) }
```

**ActionQueue**

![alt text](https://github.com/SysdataSpA/KTAndroidArchitecture/blob/develop/actionQueue.png "ActionQueue")

```kotlin
val actionQueue = ActionQueue.Builder<LoginActionParams, UserLogged>()
        .setFirstUseCase(FirstUseCase::class.java)
        .addUseCase(...)
        .setLastUseCase(...)
```

# License

      Copyright (C) 2017 Sysdata S.p.A.

      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.
 
