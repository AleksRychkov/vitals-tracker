package ru.vt.core.navigation

import androidx.navigation.NavHostController
import ru.vt.core.navigation.contract.Navigator
import ru.vt.core.navigation.contract.RouteArgs

class AppNavigator(
    private val navController: NavHostController
) : Navigator {

    override fun navigate(args: RouteArgs) {
        navController.navigate(args.route) {
            args.popUpTo?.let {
                popUpTo(it) {
                    inclusive = args.popUpToInclusive
                }
            }
        }
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }
}
