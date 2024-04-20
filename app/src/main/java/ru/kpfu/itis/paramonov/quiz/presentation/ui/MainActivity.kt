package ru.kpfu.itis.paramonov.quiz.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ru.kpfu.itis.paramonov.quiz.R
import ru.kpfu.itis.paramonov.quiz.di.dependencies.findComponentDependencies
import ru.kpfu.itis.paramonov.quiz.di.main.MainComponent
import ru.kpfu.itis.paramonov.quiz.di.main.MainDependencies
import ru.kpfu.itis.paramonov.quiz.navigation.Navigator
import ru.kpfu.itis.paramonov.quiz.presentation.viewmodel.MainViewModel
import javax.inject.Inject

class MainActivity: AppCompatActivity(R.layout.activity_main) {
    lateinit var mainComponent: MainComponent

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        initViews()
    }
    @Inject
    lateinit var navigator: Navigator

    private var navController: NavController? = null

    private fun inject() {
        mainComponent = MainComponent.init(this,
            findComponentDependencies<MainDependencies>())
        mainComponent.inject(this)
    }

    private fun initViews() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navigator.attachNavController(navController!!, R.navigation.main_nav_graph)
    }

    override fun onDestroy() {
        //viewModel.logoutUser()
        navController?.let {
            navigator.detachNavController(it)
        }
        super.onDestroy()
    }
}