package ru.kpfu.itis.paramonov.quiz.navigation

import androidx.navigation.NavController
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.quiz.R

class Navigator: AuthenticationRouter {

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
}