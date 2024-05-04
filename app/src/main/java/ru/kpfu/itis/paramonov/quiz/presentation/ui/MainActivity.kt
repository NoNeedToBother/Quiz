package ru.kpfu.itis.paramonov.quiz.presentation.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.kpfu.itis.paramonov.common_android.utils.gone
import ru.kpfu.itis.paramonov.common_android.utils.show
import ru.kpfu.itis.paramonov.quiz.R
import ru.kpfu.itis.paramonov.quiz.di.dependencies.findComponentDependencies
import ru.kpfu.itis.paramonov.quiz.di.main.MainComponent
import ru.kpfu.itis.paramonov.quiz.di.main.MainDependencies
import ru.kpfu.itis.paramonov.quiz.navigation.Navigator
import ru.kpfu.itis.paramonov.quiz.presentation.viewmodel.MainViewModel
import javax.inject.Inject

class MainActivity: AppCompatActivity(R.layout.activity_main) {
    private lateinit var mainComponent: MainComponent

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        setupNavigation()
    }
    @Inject
    lateinit var navigator: Navigator

    private var navController: NavController? = null

    private fun inject() {
        mainComponent = MainComponent.init(this,
            findComponentDependencies<MainDependencies>())
        mainComponent.inject(this)
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navigator.attachNavController(navController!!, R.navigation.main_nav_graph)
        val bnv = findViewById<BottomNavigationView>(R.id.bnv_main)
        navigator.addOnDestinationChangedListener {
            val id = it.id
            val fragmentsToHideBnv = listOf(R.id.registerFragment, R.id.signInFragment)
            if (fragmentsToHideBnv.contains(id)) {
                if (bnv.visibility == View.VISIBLE) hideBottomNavigationView(bnv)
            }
            else if(bnv.visibility == View.GONE) showBottomNavigationView(bnv)
        }
        bnv.setupWithNavController(navController!!)
    }

    private fun showBottomNavigationView(bnv: BottomNavigationView) {
        bnv.show()
        val animation = AnimationUtils.loadAnimation(this, R.anim.show_anim)
        bnv.startAnimation(animation)
    }

    private fun hideBottomNavigationView(bnv: BottomNavigationView) {
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_anim)
        bnv.startAnimation(animation)
        bnv.gone()
    }

    override fun onDestroy() {
        navController?.let {
            navigator.detachNavController(it)
        }
        super.onDestroy()
    }
}