@file:OptIn(ExperimentalMaterial3Api::class)

package com.fingerprintjs.android.playground.ui.screens.main

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Stable
class MainContentState(
    val drawerState: DrawerState,
    val navController: NavHostController,
) {

    fun navigateFromStartToHomeScreen() {
        require(navController.currentDestination?.route == NavSections.START.route)
        navController.popBackStack(NavSections.START.route, inclusive = true, saveState = false)
        navController.navigate(NavSections.HOME.route)
    }

    @Composable
    fun shouldShowNavDrawer(): Boolean {
        val state by navController.currentBackStackEntryAsState()
        return state?.destination?.route?.let { it != NavSections.START.route } ?: false
    }
}

/**
 * Remembers and creates an instance of [MainContentState]
 */
@Composable
fun rememberMainContentState(
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    navController: NavHostController = rememberNavController(),
) = remember(drawerState, navController) {
    MainContentState(drawerState, navController)
}

enum class NavSections(val route: String) {
    START("start"),
    HOME("home");
}
