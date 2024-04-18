package ru.kpfu.itis.paramonov.quiz.di.main

import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import ru.kpfu.itis.paramonov.quiz.di.dependencies.ComponentDependencies

interface MainFirebaseDependencies: ComponentDependencies {

    fun userRepository(): UserRepository

}