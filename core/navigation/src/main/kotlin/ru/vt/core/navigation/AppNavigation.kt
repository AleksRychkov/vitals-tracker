package ru.vt.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.vt.core.navigation.contract.Routes
import ru.vt.core.navigation.contract.ScreenParams
import ru.vt.presentation.dashboard.main.ScreenDashboard
import ru.vt.presentation.measurements.headaches.create.ScreenCreateHeadaches
import ru.vt.presentation.measurements.headaches.summary.ScreenHeadaches
import ru.vt.presentation.measurements.pressureheartrate.create.ScreenCreateBloodPressure
import ru.vt.presentation.measurements.pressureheartrate.summary.ScreenBloodPressure
import ru.vt.presentation.measurements.weight.create.ScreenCreateWeight
import ru.vt.presentation.measurements.weight.summary.ScreenWeight
import ru.vt.presentation.profile.create.ScreenCreateProfile
import ru.vt.presentation.splash.ScreenSplash

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.splash
    ) {
        addSplash()

        addProfileCreate()

        addDashboard()

        addMeasurements()
    }
}

private fun NavGraphBuilder.addSplash() {
    composable(Routes.splash) {
        ScreenSplash()
    }
}

private fun NavGraphBuilder.addProfileCreate() {
    composable(Routes.profileCreate) {
        ScreenCreateProfile()
    }
}

private fun NavGraphBuilder.addDashboard() {
    composable(
        route = Routes.dashboard,
        arguments = listOf(
            navArgument(ScreenParams.PROFILE_ID) {
                type = NavType.LongType
            }
        )
    ) { navBackStackEntry ->
        ScreenDashboard(profileId = navBackStackEntry.profileId())
    }
}

private fun NavGraphBuilder.addMeasurements() {
    composable(
        route = Routes.pressureAndHeartRate,
        arguments = listOf(
            navArgument(ScreenParams.PROFILE_ID) {
                type = NavType.LongType
            }
        )
    ) { navBackStackEntry ->
        ScreenBloodPressure(profileId = navBackStackEntry.profileId())
    }
    composable(
        route = Routes.pressureAndHeartRateCreate,
        arguments = listOf(
            navArgument(ScreenParams.PROFILE_ID) {
                type = NavType.LongType
            }
        )
    ) { navBackStackEntry ->
        ScreenCreateBloodPressure(profileId = navBackStackEntry.profileId())
    }
    composable(
        route = Routes.headaches,
        arguments = listOf(
            navArgument(ScreenParams.PROFILE_ID) {
                type = NavType.LongType
            }
        )
    ) { navBackStackEntry ->
        ScreenHeadaches(profileId = navBackStackEntry.profileId())
    }
    composable(
        route = Routes.headachesCreate,
        arguments = listOf(
            navArgument(ScreenParams.PROFILE_ID) {
                type = NavType.LongType
            }
        )
    ) { navBackStackEntry ->
        ScreenCreateHeadaches(profileId = navBackStackEntry.profileId())
    }

    composable(
        route = Routes.weight,
        arguments = listOf(
            navArgument(ScreenParams.PROFILE_ID) {
                type = NavType.LongType
            }
        )
    ) { navBackStackEntry ->
        ScreenWeight(profileId = navBackStackEntry.profileId())
    }

    composable(
        route = Routes.weightCreate,
        arguments = listOf(
            navArgument(ScreenParams.PROFILE_ID) {
                type = NavType.LongType
            }
        )
    ) { navBackStackEntry ->
        ScreenCreateWeight(profileId = navBackStackEntry.profileId())
    }
}

private fun NavBackStackEntry.profileId(): Long = this.arguments?.getLong(ScreenParams.PROFILE_ID)!!
