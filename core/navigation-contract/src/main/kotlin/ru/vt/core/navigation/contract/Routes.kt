package ru.vt.core.navigation.contract

object Routes {
    const val splash: String = "splash"

    const val profileCreate: String = "profile_create"

    const val dashboard: String = "dashboard/{${ScreenParams.PROFILE_ID}}"

    const val pressureAndHeartRate: String = "measurements/pressure_heart_rate/{${ScreenParams.PROFILE_ID}}"
    const val pressureAndHeartRateCreate: String = "measurements/pressure_heart_rate/{${ScreenParams.PROFILE_ID}}/create"

    const val headaches: String = "measurements/headaches/{${ScreenParams.PROFILE_ID}}"
    const val headachesCreate: String = "measurements/headaches/{${ScreenParams.PROFILE_ID}}/create"

    const val weight: String = "measurements/weight/{${ScreenParams.PROFILE_ID}}"
    const val weightCreate: String = "measurements/weight/{${ScreenParams.PROFILE_ID}}/create"
}

sealed class RouteArgs(
    val route: String,
    val popUpTo: String? = null,
    val popUpToInclusive: Boolean = false
)

// region from ScreenSplash
object SplashToProfileCreate : RouteArgs(
    route = Routes.profileCreate,
    popUpTo = Routes.splash,
    popUpToInclusive = true
)

data class SplashToDashboard(val id: Long) : RouteArgs(
    route = "dashboard/$id",
    popUpTo = Routes.splash,
    popUpToInclusive = true
)
// endregion

// region from ScreenCreateProfile
data class ProfileCreateToDashboard(val id: Long) : RouteArgs(
    route = "dashboard/$id",
    popUpTo = Routes.profileCreate,
    popUpToInclusive = true
)
// endregion

// region from ScreenDashboard
data class DashboardToPressureAndHeartRate(val id: Long): RouteArgs(
    route = "measurements/pressure_heart_rate/$id"
)
data class DashboardToHeadaches(val id: Long): RouteArgs(
    route = "measurements/headaches/$id"
)
data class DashboardToWeight(val id: Long): RouteArgs(
    route = "measurements/weight/$id"
)
// endregion

// region from ScreenPHR
data class ScreenPHRToCreate(val id: Long): RouteArgs(
    route = "measurements/pressure_heart_rate/$id/create"
)
// endregion

// region ScreenHeadaches
data class ScreenHeadachesToCreate(val id: Long): RouteArgs(
    route = "measurements/headaches/$id/create"
)
//endregion

// region ScreenWeight
data class ScreenWeightToCreate(val id: Long): RouteArgs(
    route = "measurements/weight/$id/create"
)
//endregion


object ScreenParams {
    const val PROFILE_ID = "profile_id"
}
