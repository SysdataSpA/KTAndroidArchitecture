# DI - Use with Dagger
A Kotlin android architecture with Google Architecture Components

## 1; How to use it?

### 1.1 Import dependency

#### 1.1.1 in **app level `build.gradle`** add these dependencies that will be needed to add Dagger and thus Dependency injection.
```gradle
       // dagger2 android
       kapt "com.google.dagger:dagger-android-processor:$dagger_version"
       implementation "com.google.dagger:dagger-android:$dagger_version"
       implementation "com.google.dagger:dagger-android-support:$dagger_version"
       kapt "com.google.dagger:dagger-compiler:$dagger_version"
```
### 1.2 Basic implementation steps
#### 1.2.1 Define a ViewModelFactory that will injext ViewModel providers as soon as they will be needed and will add them to a map so they are as singleton instances. You will have to add all the ViewModels you create to this map and to the constructor.
```kotlin
@Singleton
class ViewModelFactory @Inject constructor(application: Application
// ... add other view model providers here
, sampleViewModelProvider: Provider<SampleViewModel>) : ViewModelProvider.Factory {
    
    private val mMapProvider = HashMap<Class<out ViewModel>, Provider<out ViewModel>>()
    private val defaultFactory: ViewModelProvider.AndroidViewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)

    init {
        mMapProvider[SampleViewModel::class.java] = sampleViewModelProvider
        // ... add other view model providers to the map here
    }
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (mMapProvider.containsKey(modelClass)) {
            mMapProvider[modelClass]!!.get() as T
        } else {
            defaultFactory.create(modelClass)
        }
    }
}
```
#### 1.2.2 Define an application component that will provide the created ViewModelFactory.
```kotlin
@Singleton
@Component(modules = [AndroidSupportInjectionModule::class])
interface ApplicationComponent : AndroidInjector<DaggerApplication> {
    fun getViewModelFactory(): ViewModelFactory
}
```
#### 1.2.3 Define a ViewModel. By using Dagger, you can create the instance of the view model through the @Inject annotation. Moreover, usecases will be injected instead of being instantiated through reflection.

```kotlin
class SampleViewModel @Inject constructor(sampleUsecase: SampleUsecase
// you can add all possible usecases here as parameters of the ViewModel
) : ViewModel() {
     // actions can use instances of the injected UseCases instead of the class
     val sampleAction = Action.Builder<None, None, None>()
            .useCase(sampleUsecase)
            .buildWithUiModel { None }
    
}
```

#### 1.2.4 UseCases are written in a similar way: the only difference is that we use the @Inject annotation here and we define our repositories as constructor parameters instead of creating them.
```kotlin
class SampleUsecase @Inject constructor(
// optional repo that you can add as constrtor parameters
private val sampleRepo: SampleRepo) : UseCase<None, None>() {
    override suspend fun run(params: None): Either<Failure, None> {
        // optionally you can perform async operations here.
        return Either.Right(None)
    }
}
```
```kotlin
@Singleton
class AuthenticationRepo @Inject constructor() {
    // define here your repository methods (DataBase calls, wev)
}
```
#### 1.2.5 You are now ready to use your view model inside your Activity/Fragment by injecting the ViewModelFactory and lazy initializing it.
```kotlin
@Inject
lateinit var factory: ViewModelFactory
private val viewModel: SampleViewModel by lazy {
    ViewModelProviders.of(this, factory).get(SampleViewModel::class.java)
}
```