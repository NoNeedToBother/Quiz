package ru.kpfu.itis.paramonov.firebase.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.kpfu.itis.paramonov.firebase.data.repository.UserRepositoryImpl
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import ru.kpfu.itis.paramonov.quiz.di.scopes.FeatureScope

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