# KTAndroidArchitecture
A kotlint android architecture with Google Architecture Components
## 1. A Brief Introduciton
The app is a sample project that shows how to implement the KTAndroidArchitecture into your Android app.

### 1.1 What is KTAndroidArchitecture?
It is a layer-based architecture that allows a real disentangle of the UI components from the business logic. 

![alt text](https://cdn-images-1.medium.com/max/800/1*I9WPcnpGNuI4CjxxrkP0-g.png "Simple Architecture Diagram")

The main components are:

* UI
* UIModel
* ViewModels with Livedata
* UseCase
* Repository

### 1.2 KTAndroidArchitecture main components

#### 1.2.1 UI

The UI layer of the architecture comprises activities, fragments and views. 

#### 1.2.2 UIModel

A **UIModel** is a object which contains all UI-related datas of a view, a fragment or an activity

#### 1.2.3 ViewModels with Livedata

Each activity or fragment could have a **ViewModel** which is a object designed to store and manage UI-related data in a lifecycle conscious way by defining some **Actions** to call one or more **UseCases**

#### 1.2.4 UseCase
A **UseCase** is a wrapper for a small business logic operation. A **UseCase** can use one or more **Repository** to get or write the requested data, then it returns the response event.

#### 1.2.5 Repository
A **Repository** handles the process of saving or retrieving data from a datasource, it is managed by one or more **UseCase**.

#### 1.2.6 Action
An **Action** handles the process of calling a **UseCase** and map the response, generally an action use only one **UseCase** but is possible to define an ActionQueue to call multiple **UseCases** sequentially.

## 2. How to use it?

### 2.1 Import dependency

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
            .buildWithUiModel { it }
```

### 2.5 Call the Action from the Activity/Fragment
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

# Licence

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
 
