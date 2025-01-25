package ru.kpfu.itis.paramonov.quiz.navigation

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import ru.kpfu.itis.paramonov.core.exception.UnsupportedArgumentException
import ru.kpfu.itis.paramonov.feature_profiles.presentation.fragments.OtherUserProfileFragment
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

    override fun popToMainMenu() {
        navController?.popBackStack(R.id.mainMenuFragment, false)
    }

    override fun goToMainMenu() {
        navController?.apply {
            popToMainMenu()
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

    override fun goToQuestionResults(vararg args: Pair<String, Any>) {
        val arguments = putArguments(args)
        navController?.navigate(
            R.id.questionResultsFragment,
            arguments
        )
    }

    private fun putArguments(args: Array<out Pair<String, Any>>): Bundle {
        return Bundle().apply {
            for (pair in args) {
                when(val value = pair.second) {
                    is Int -> putInt(pair.first, value)
                    is String -> putString(pair.first, value)
                    is Double -> putDouble(pair.first, value)
                    is Float -> putFloat(pair.first, value)
                    is Boolean -> putBoolean(pair.first, value)
                    is Long -> putLong(pair.first, value)
                    else -> throw UnsupportedArgumentException("Unsupported argument")
                }
            }
        }
    }

    private var sharedView: View? = null

    override fun goToUser(id: String) {
        val extras = extrasWithSharedView(OtherUserProfileFragment.PROFILE_PICTURE_TRANSITION_NAME) {
            it is ImageView
        }
        navController?.navigate(
            R.id.otherUserProfileFragment,
            bundleOf(OtherUserProfileFragment.USER_ID_KEY to id),
            null,
            extras
        )
    }

    override fun withSharedView(sharedView: View, block: UserRouter.() -> Unit) {
        block.invoke(this.apply {
            this.sharedView = sharedView
        })
    }

    private fun extrasWithSharedView(transitionName: String, condition: (View) -> Boolean, ): FragmentNavigator.Extras? {
        val extras = sharedView?.let {
            if (condition.invoke(it)) {
                FragmentNavigatorExtras(
                    it to transitionName
                )
            } else null
        }
        sharedView = null
        return extras
    }

}