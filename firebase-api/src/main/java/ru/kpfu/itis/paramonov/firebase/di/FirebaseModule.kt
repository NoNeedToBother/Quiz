package ru.kpfu.itis.paramonov.firebase.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import ru.kpfu.itis.paramonov.firebase.data.repository.UserRepositoryImpl
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository

@Module
class FirebaseModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    fun provideUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository = impl

}