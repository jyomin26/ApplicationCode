package com.android.applicationcode.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.applicationcode.presentation.vehicledetail.VehicleDetailScreen
import com.android.applicationcode.presentation.vehicledetail.VehicleDetailViewModel
import com.android.applicationcode.presentation.vehiclelist.VehicleListScreen
import com.android.applicationcode.presentation.vehiclelist.VehiclesListViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoutes.LISTING_SCREEN) {
        //Vehicle Listing screen to show vehicles from Db as a single source of Truth
        composable(
            route = NavRoutes.LISTING_SCREEN,
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(500)
                )
            }
        ) {
            val viewModel: VehiclesListViewModel = hiltViewModel()
            VehicleListScreen(navController = navController, viewModel = viewModel)
        }

        //Vehicle Detail Screen
        composable(
            route = "${NavRoutes.DETAIL_SCREEN}/{${NavRoutes.VIN_ARG}}",
            arguments = listOf(
                navArgument(NavRoutes.VIN_ARG) { type = NavType.StringType }
            ),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(500)
                )
            }
        ) {
            val viewModel: VehicleDetailViewModel = hiltViewModel()
            VehicleDetailScreen(navController = navController, viewModel = viewModel)
        }
    }
}
