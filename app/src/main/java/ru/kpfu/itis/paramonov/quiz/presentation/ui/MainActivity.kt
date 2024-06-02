package ru.kpfu.itis.paramonov.quiz.presentation.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.kpfu.itis.paramonov.common_android.utils.gone
import ru.kpfu.itis.paramonov.common_android.utils.show
import ru.kpfu.itis.paramonov.quiz.R
import ru.kpfu.itis.paramonov.quiz.databinding.ActivityMainBinding
import ru.kpfu.itis.paramonov.quiz.di.dependencies.findComponentDependencies
import ru.kpfu.itis.paramonov.quiz.di.main.MainComponent
import ru.kpfu.itis.paramonov.quiz.di.main.MainDependencies
import ru.kpfu.itis.paramonov.quiz.navigation.Navigator
import ru.kpfu.itis.paramonov.quiz.presentation.viewmodel.MainViewModel
import javax.inject.Inject

class MainActivity: AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

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
        binding.bnvMain.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.mainMenuFragment -> navigator.goToMainMenu()
                R.id.friendsFragment -> navController?.navigate(R.id.friendsFragment)
                R.id.leaderboardsFragment -> navController?.navigate(R.id.leaderboardsFragment)
                R.id.profileFragment -> navController?.navigate(R.id.profileFragment)
                R.id.searchUsersFragment -> navController?.navigate(R.id.searchUsersFragment)
            }
            return@setOnItemSelectedListener true
        }
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
            if (bnv.menu.findItem(id) != null) bnv.menu.findItem(id).isChecked = true
        }
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