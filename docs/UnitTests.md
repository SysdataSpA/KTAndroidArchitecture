# Unit Tests

### 1 Intro
The aim of the document is to provide base indications on unit tests.
Unit testing can be done in various ways and with different tools. 
We suggest to test your application with [Koin](https://insert-koin.io/) in order to mock your test objects.

In the following paragraphs, I'll describe an example of a unit test with the ktAndroidArchitecture.

### 2.1 Preliminary actions
Gradle dependencies

```gradle
android.defaultConfig {
     androidTestImplementation 'androidx.test:runner:1.2.0'
     androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
     testImplementation 'org.mockito:mockito-core:2.23.0'
     testImplementation 'android.arch.core:core-testing:1.1.1'
     testImplementation 'com.jraska.livedata:testing-ktx:1.1.0'
     testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.0"
     // Koin for Unit tests
     testImplementation "org.koin:koin-test:$koin_version"
}
```

### 2.2 UseCase test
In order to perform a UseCase test, you have to call the `run()` function of the UseCase
providing the ActionParams inside the method signature.
After that, you can perform all the assertion that are useful to you.
In the following snippet we are testing the LoginUseCase.

```kotlin
class LoginUseCaseTest : KoinTest {

    @Before
    fun before() {
        startKoin {
            printLogger()
            modules(appModule)
        }
    }
    
    @After
    fun after() {
        stopKoin()
    }
    
    @Test
    fun runUseCase() = runBlockingTest {
        val email = "email"
        val password = "test"
        val params = LoginActionParams(email, password)
        val result = useCase.run(params)
        assert(result.isRight)
        var userLogged: UserLogged? = null
        result.map { res -> userLogged = res }
        delay(200)
        assertEquals(UserLogged(email), userLogged)
    }
    
    // further tests here
}
```

### 2.2 ViewModel test
In order to perform a ViewModel test, you have to create a method inside the real ViewModel.

```kotlin
class LoginViewModel(loginUseCase: LoginUseCase) : BaseViewModel() {

    fun login(username: String, password: String) {
        actionLogin.execute(LoginActionParams(username, password), viewModelScope)
    }
    
}
```

Then you can call the "login()" function inside the ViewModel test class.

```kotlin
class LoginViewModelTest : KoinTest {
    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val loginViewModelTest: LoginViewModel by inject()

    @Before
    fun before() {
        startKoin {
            printLogger()
            modules(appModule)
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun `testLogin`() {
        val testLifecycle = TestLifecycle.initialized()
        val email = "email"
        val password = "test"
        loginViewModelTest.login(email, password)
        testLifecycle.resume()
        val result = LiveDataTestUtil.getValue(loginViewModelTest.actionLogin, testLifecycle)
        assertEquals(UIUserLogged(email), result)
    }
    
     // further tests here
}
```

In the previous snippet, you can see the `loginViewModelTest.login(email, password)` used to test the ViewModel.


### 2.3 Repository test
In order to perform a Repository test, you have to inject the real repository through Koin.
Then, inside the `@Test` annotated function, you can call the repository function that you want to test.

```kotlin
class AuthRepositoryTest : KoinTest {

    val repo: AuthRepository by inject()

    @Before
    fun before() {
        startKoin {
            printLogger()
            modules(appModule)
        }
    }
    
    @After
    fun after() {
        stopKoin()
    }
    
    @Test
    fun login() {
        val email = "email"
        val password = "test"
        val result = repo.login(email, password)
        assertEquals(UserLogged(email), result)
    }
    
}
```