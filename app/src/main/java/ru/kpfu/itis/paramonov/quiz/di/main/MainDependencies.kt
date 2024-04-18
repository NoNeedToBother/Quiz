package ru.kpfu.itis.paramonov.quiz.di.main

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.quiz.di.dependencies.ComponentDependencies
import ru.kpfu.itis.paramonov.quiz.navigation.Navigator

interface MainDependencies : ComponentDependencies {

    fun navigator(): Navigator

    fun dispatcher(): CoroutineDispatcher
}