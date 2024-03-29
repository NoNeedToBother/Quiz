package ru.kpfu.itis.paramonov.quiz.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ru.kpfu.itis.paramonov.quiz.R
import ru.kpfu.itis.paramonov.feature_authentication.presentation.registration.RegisterFragment
import ru.kpfu.itis.paramonov.quiz.di.dependencies.findComponentDependencies
import ru.kpfu.itis.paramonov.quiz.di.main.MainComponent
import ru.kpfu.itis.paramonov.quiz.navigation.Navigator
import javax.inject.Inject

class MainActivity: AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        initViews()
    }
    @Inject
    lateinit var navigator: Navigator

    private var navController: NavController? = null

    private fun inject() {
        MainComponent.init(this, findComponentDependencies())
            .inject(this)
    }

    private fun initViews() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navigator.attachNavController(navController!!, R.navigation.main_nav_graph)
    }

    override fun onDestroy() {
        super.onDestroy()
        navController?.let {
            navigator.detachNavController(it)
        }
    }
}