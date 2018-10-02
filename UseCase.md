# KTAndroidArchitecture - USECASE

### 1.1 What is UseCase?
A UseCase is basically a wrapper for business logic operations, such as backend calls, data elaboration and so on. 

In this specific case a usecase only elaborates the datas provided by a repository and return the elaborated datas.
In the execution flow of a usecase we can find this steps:

* The action execute the usecase;
* The usecase calls the repository to get datas;
* The retrieved data get elaborate, by handling error or success results ie.
* The elaborated datas returns to the usecase's calling action.


![alt text](https://github.com/SysdataSpA/KTAndroidArchitecture/blob/usecase_documentation/usecase.png)

A usecase have to extend the UseCase abstract class and implement the method **run** where all  the logic is placed, the caller action calls the **execute** method which calls the **run** method

### 1.2 Sample

Below a complete example of UseCase:


```
class LoginUseCase: UseCase<UserLogged, LoginActionParams>() {
    override suspend fun run(params: LoginActionParams): Either<Failure, UserLogged> {
        return AuthRepository.instance.login(params.email, params.password).map { do something }
    }
}
```
