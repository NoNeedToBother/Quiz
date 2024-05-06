package ru.kpfu.itis.paramonov.firebase.internal.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.utils.DateTimeParser
import ru.kpfu.itis.paramonov.firebase.internal.data.handler.RegistrationExceptionHandler
import ru.kpfu.itis.paramonov.firebase.internal.data.handler.SignInExceptionHandler
import ru.kpfu.itis.paramonov.firebase.internal.data.repository.AuthenticationRepositoryImpl
import ru.kpfu.itis.paramonov.firebase.internal.data.repository.FriendRepositoryImpl
import ru.kpfu.itis.paramonov.firebase.internal.data.repository.ResultRepositoryImpl
import ru.kpfu.itis.paramonov.firebase.internal.data.repository.UserRepositoryImpl
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.FriendRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.ResultRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository

@Module
internal class FirebaseModule {

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
        resourceManager: ResourceManager
    ): UserRepositoryImpl {
        return UserRepositoryImpl(firebaseAuth, Firebase.firestore, Firebase.storage, dispatcher, resourceManager)
    }

    @Provides
    fun userRepository(impl: UserRepositoryImpl): UserRepository = impl

    @Provides
    fun firebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun resultRepositoryImpl(
        dispatcher: CoroutineDispatcher,
        resourceManager: ResourceManager,
        userRepository: UserRepository
    ): ResultRepositoryImpl {
        return ResultRepositoryImpl(Firebase.firestore, dispatcher, resourceManager, userRepository)
    }

    @Provides
    fun resultRepository(
        impl: ResultRepositoryImpl
    ): ResultRepository = impl

    @Provides
    fun authenticationRepositoryImpl(
        firebaseAuth: FirebaseAuth,
        dispatcher: CoroutineDispatcher,
        registerExceptionHandler: RegistrationExceptionHandler,
        signInExceptionHandler: SignInExceptionHandler,
        resourceManager: ResourceManager,
        userRepository: UserRepository,
        dateTimeParser: DateTimeParser
    ): AuthenticationRepositoryImpl {
        return AuthenticationRepositoryImpl(firebaseAuth, dispatcher, registerExceptionHandler,
            signInExceptionHandler, resourceManager, userRepository, dateTimeParser)
    }

    @Provides
    fun authenticationRepository(
        impl: AuthenticationRepositoryImpl
    ): AuthenticationRepository = impl

    @Provides
    fun friendRepository(
        impl: FriendRepositoryImpl
    ): FriendRepository = impl

    @Provides
    fun friendRepositoryImpl(
        dispatcher: CoroutineDispatcher,
        resourceManager: ResourceManager,
        userRepository: UserRepository
    ): FriendRepositoryImpl {
        return FriendRepositoryImpl(
            Firebase.firestore, dispatcher, resourceManager, userRepository
        )
    }
}