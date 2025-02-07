package ru.kpfu.itis.paramonov.quiz.presentation.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance
import ru.kpfu.itis.paramonov.core.utils.gone
import ru.kpfu.itis.paramonov.core.utils.show
import ru.kpfu.itis.paramonov.quiz.R
import ru.kpfu.itis.paramonov.quiz.databinding.ActivityMainBinding
import ru.kpfu.itis.paramonov.quiz.navigation.Navigator

class MainActivity: AppCompatActivity(R.layout.activity_main), DIAware {

    override val di: DI by closestDI()

    private val navigator: Navigator by instance()

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
    }

    private var navController: NavController? = null

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