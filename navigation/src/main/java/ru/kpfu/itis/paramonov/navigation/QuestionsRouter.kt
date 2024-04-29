package ru.kpfu.itis.paramonov.navigation

interface QuestionsRouter {

    fun goToQuestions()

    fun goToTraining()

    fun goToQuestionSettings()

    fun goToQuestionResults(vararg args: Pair<String, Any>)
}