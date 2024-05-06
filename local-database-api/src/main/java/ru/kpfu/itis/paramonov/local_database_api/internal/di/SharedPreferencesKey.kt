package ru.kpfu.itis.paramonov.local_database_api.internal.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class SharedPreferencesKey