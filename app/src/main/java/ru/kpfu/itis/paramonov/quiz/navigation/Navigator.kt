package ru.kpfu.itis.paramonov.quiz.navigation

import androidx.navigation.NavController
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import ru.kpfu.itis.paramonov.navigation.QuestionsRouter
import ru.kpfu.itis.paramonov.quiz.R

class Navigator: AuthenticationRouter, MainMenuRouter, QuestionsRouter {

    private var navController: NavController? = null

    fun attachNavController(navController: NavController, graph: Int) {
        navController.setGraph(graph)
        this.navController = navController
    }

    fun detachNavController(navController: NavController) {
        if (this.navController == navController) {
            this.navController = null
        }
    }

    override fun goToRegister() {
        navController?.navigate(
            R.id.registerFragment
        )
    }

    override fun goToSignIn() {
        navController?.navigate(
            R.id.signInFragment
        )
    }

    override fun goToMainMenu() {
        navController?.navigate(
            R.id.mainMenuFragment
        )
    }

    override fun goToQuestion() {
        navController?.navigate(
            R.id.action_mainMenuFragment_to_questionsFragment
        )
    }

    override fun goToQuestionSettings() {
        navController?.navigate(
            R.id.action_mainMenuFragment_to_questionsSettingsFragment
        )
    }
}