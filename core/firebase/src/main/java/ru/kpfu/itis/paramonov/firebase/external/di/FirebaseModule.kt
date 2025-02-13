package ru.kpfu.itis.paramonov.firebase.external.di

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import ru.kpfu.itis.paramonov.firebase.external.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.firebase.external.repository.FriendRepository
import ru.kpfu.itis.paramonov.firebase.external.repository.ResultRepository
import ru.kpfu.itis.paramonov.firebase.external.repository.UserRepository
import ru.kpfu.itis.paramonov.firebase.internal.handler.RegistrationExceptionHandler
import ru.kpfu.itis.paramonov.firebase.internal.handler.SignInExceptionHandler
import ru.kpfu.itis.paramonov.firebase.internal.repository.AuthenticationRepositoryImpl
import ru.kpfu.itis.paramonov.firebase.internal.repository.FriendRepositoryImpl
import ru.kpfu.itis.paramonov.firebase.internal.repository.ResultRepositoryImpl
import ru.kpfu.itis.paramonov.firebase.internal.repository.UserRepositoryImpl

val firebaseModule = DI {
    bind<RegistrationExceptionHandler>() with provider { RegistrationExceptionHandler(resourceManager = instance()) }
    bind<SignInExceptionHandler>() with provider { SignInExceptionHandler(resourceManager = instance()) }

    bind<UserRepository>() with provider {
        UserRepositoryImpl(
            auth = Firebase.auth,
            database = Firebase.firestore,
            storage = Firebase.storage,
            dispatcher = instance(),
            resourceManager = instance(),
            passwordValidator = instance(),
            usernameValidator = instance()
        )
    }

    bind<ResultRepository>() with provider {
        ResultRepositoryImpl(
            database = Firebase.firestore,
            dispatcher = instance(),
            resourceManager = instance(),
            userRepository = instance(),
            dateTimeParser = instance()
        )
    }

    bind<AuthenticationRepository>() with provider {
        AuthenticationRepositoryImpl(
            auth = Firebase.auth,
            dispatcher = instance(),
            registerExceptionHandler = instance(),
            signInExceptionHandler = instance(),
            resourceManager = instance(),
            userRepository = instance(),
            dateTimeParser = instance(),
            passwordValidator = instance(),
            usernameValidator = instance()
        )
    }

    bind<FriendRepository>() with provider {
        FriendRepositoryImpl(
            database = Firebase.firestore,
            dispatcher = instance(),
            resourceManager = instance(),
            userRepository = instance()
        )
    }
}
