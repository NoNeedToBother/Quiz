package ru.kpfu.itis.paramonov.quiz.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import ru.kpfu.itis.paramonov.navigation.QuestionsRouter
import ru.kpfu.itis.paramonov.navigation.UserRouter
import ru.kpfu.itis.paramonov.quiz.R

class Navigator: AuthenticationRouter, MainMenuRouter, QuestionsRouter, UserRouter {

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

    fun addOnDestinationChangedListener(listener: (NavDestination) -> Unit) {
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            listener.invoke(destination)
        }
    }

    override fun goToRegister() {
        navController?.apply {
            popBackStack(R.id.registerFragment, false)
            navigate(R.id.registerFragment)
        }
    }

    override fun goToSignIn() {
        navController?.apply {
            popBackStack(R.id.signInFragment, false)
            navigate(R.id.signInFragment)
        }
    }

    override fun goToMainMenu() {
        navController?.apply {
            navigate(
                R.id.mainMenuFragment
            )
        }
    }

    override fun goToQuestions() {
        navController?.navigate(
            R.id.action_mainMenuFragment_to_questionsFragment
        )
    }

    override fun goToTraining() {
        navController?.navigate(
            R.id.action_mainMenuFragment_to_trainingQuestionsFragment
        )
    }

    override fun goToQuestionSettings() {
        navController?.navigate(
            R.id.action_mainMenuFragment_to_questionSettingsFragment
        )
    }

    override fun goToUser(id: String) {
        navController?.navigate(
            R.id.otherUserProfileFragment,
            bundleOf("id" to id),
            null
        )
    }
}
