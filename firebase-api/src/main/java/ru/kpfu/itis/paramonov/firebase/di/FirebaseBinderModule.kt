package ru.kpfu.itis.paramonov.firebase.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kpfu.itis.paramonov.firebase.data.repository.UserRepositoryImpl
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository

@Module
@InstallIn(SingletonComponent::class)
interface FirebaseBinderModule {
    @Binds
    fun bindUserRepositoryToImpl(
        impl: UserRepositoryImpl
    ): UserRepository
}