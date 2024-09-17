package com.sopht.bible

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.MainScope
import org.jetbrains.compose.ui.tooling.preview.Preview
import styles.SophtBibleTheme
import com.sopht.bible.ui.BibleScreen
import com.sopht.bible.ui.BibleViewModel
import org.koin.compose.koinInject
import com.sopht.bible.ui.VersionsScreen
import com.sopht.bible.ui.HomeScreen
import com.sopht.bible.ui.SearchScreen

enum class Screens(val route: String, val icon: ImageVector) {
    Home(route = "home", icon = Icons.Filled.Home),
    Bible(route = "bible", icon = Icons.Filled.Book),
    Versions(route = "versions", icon = Icons.Filled.Download),
    Search(route = "search", icon = Icons.Filled.Search)
}

val items = listOf(
    Screens.Home,
    Screens.Bible,
    Screens.Versions,
    Screens.Search
)

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(vm: BibleViewModel = koinInject<BibleViewModel>() ) {
    val mainScope = MainScope()
    var shouldShowOnboarding by remember { mutableStateOf(true) }
    val navController = rememberNavController()

    // Get current back stack entry

//    val backStackEntry by navController.currentBackStackEntryAsState()
//    // Get the name of the current screen
//    val currentScreen = Screens.valueOf(
//        backStackEntry?.destination?.route ?: Screens.Home.route
//    )

    if (shouldShowOnboarding) {
        SplashScreen(onComplete = {
            shouldShowOnboarding = false
        })
    } else {
        SophtBibleTheme {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                topBar = { Text(text = "Sopht Bible") },
                bottomBar = {
                    NavigationBar {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        items.forEachIndexed { index, screen ->
                            NavigationBarItem(
                                icon = { Icon(screen.icon, contentDescription = null) },
                                label = { Text(screen.name) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
//                                    mainScope.launch {
//                                        vm?.downloadVersion()
//                                    }
                                    navController.navigate(screen.name) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        // on the back stack as users select items
                                        navController.popBackStack(Screens.Home.name, inclusive = false)
                                        // Avoid multiple copies of the same destination when
                                        // reselecting the same item
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            )
                { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screens.Home.route,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(innerPadding)
                    ) {
                        composable(route = Screens.Home.route) {
                            HomeScreen(navController, vm!!)
                        }
                        composable(route = Screens.Bible.name) {
                            BibleScreen(vm!!)
                        }
                        composable(route = Screens.Search.name) {
                            SearchScreen()
                        }
                        composable(route = Screens.Versions.name) {
                            VersionsScreen(vm!!)
                        }
                }
            }
        }
    }
}
