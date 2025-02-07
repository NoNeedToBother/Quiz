package ru.kpfu.itis.paramonov.quiz.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import ru.kpfu.itis.paramonov.navigation.Routes
import ru.kpfu.itis.paramonov.quiz.R
import ru.kpfu.itis.paramonov.ui.theme.AppTheme

data class TopLevelRoute<R : Routes>(val name: String, val route: R, val icon: ImageVector)

class MainActivity: ComponentActivity(), DIAware {

    override val di: DI by closestDI()

    //private val navigator: Navigator by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val topLevelRoutes = listOf(
                    TopLevelRoute(Routes.SearchUsersScreen.route, Routes.SearchUsersScreen,
                        ImageVector.vectorResource(R.drawable.search_users)),
                    TopLevelRoute(Routes.LeaderboardsScreen.route, Routes.LeaderboardsScreen,
                        ImageVector.vectorResource(R.drawable.leaderboards)),
                    TopLevelRoute(Routes.MainMenuScreen.route, Routes.MainMenuScreen,
                        ImageVector.vectorResource(R.drawable.main_menu)),
                    TopLevelRoute(Routes.FriendsScreen.route, Routes.FriendsScreen,
                        ImageVector.vectorResource(R.drawable.friends)),
                    TopLevelRoute(Routes.ProfileScreen.route, Routes.ProfileScreen,
                        ImageVector.vectorResource(R.drawable.profile)),
                )
                MainScreen(
                    topLevelRoutes = topLevelRoutes
                )
            }
        }
    }

    /*private var navController: NavController? = null

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
    }*/
}

@SuppressLint("RestrictedApi")
@Composable
fun MainScreen(
    topLevelRoutes: List<TopLevelRoute<*>>
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation(
                navController = navController,
                topLevelRoutes = topLevelRoutes
            )
        }
    ) { padding ->
        NavHost(navController, startDestination = Routes.SignInScreen, modifier = Modifier.padding(padding)) {
            //composable<> {  }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun BottomNavigation(
    navController: NavController,
    topLevelRoutes: List<TopLevelRoute<*>>
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        topLevelRoutes.forEach { topLevelRoute ->
            NavigationBarItem(
                icon = { Icon(topLevelRoute.icon, contentDescription = topLevelRoute.name) },
                label = { Text(topLevelRoute.name) },
                selected = currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.name, null) } == true,
                onClick = {
                    navController.navigate(topLevelRoute.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
