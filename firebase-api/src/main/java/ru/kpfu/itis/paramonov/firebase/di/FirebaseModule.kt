package ru.kpfu.itis.paramonov.firebase.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.firebase.data.handler.RegistrationExceptionHandler
import ru.kpfu.itis.paramonov.firebase.data.handler.SignInExceptionHandler
import ru.kpfu.itis.paramonov.firebase.data.repository.ResultRepositoryImpl
import ru.kpfu.itis.paramonov.firebase.data.repository.UserRepositoryImpl
import ru.kpfu.itis.paramonov.firebase.domain.repository.ResultRepository
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository

@Module
class FirebaseModule {

    @Provides
    fun registrationExceptionHandler(resourceManager: ResourceManager): RegistrationExceptionHandler {
        return RegistrationExceptionHandler(resourceManager)
    }

    @Provides
    fun signInExceptionHandler(resourceManager: ResourceManager): SignInExceptionHandler {
        return SignInExceptionHandler(resourceManager)
    }

    @Provides
    fun userRepositoryImpl(
        firebaseAuth: FirebaseAuth,
        dispatcher: CoroutineDispatcher,
        registerExceptionHandler: RegistrationExceptionHandler,
        signInExceptionHandler: SignInExceptionHandler,
        resManager: ResourceManager
    ): UserRepositoryImpl {
        return UserRepositoryImpl(firebaseAuth, Firebase.firestore, dispatcher, registerExceptionHandler,
            signInExceptionHandler, resManager)
    }

    @Provides
    fun userRepository(impl: UserRepositoryImpl): UserRepository = impl

    @Provides
    fun firebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun resultRepositoryImpl(
        dispatcher: CoroutineDispatcher,
        resourceManager: ResourceManager
    ): ResultRepositoryImpl {
        return ResultRepositoryImpl(Firebase.firestore, dispatcher, resourceManager)
    }

    @Provides
    fun resultRepository(
        impl: ResultRepositoryImpl
    ): ResultRepository = impl
}