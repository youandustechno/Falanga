package com.clovis.falanga.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


@Composable
fun Routes() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.SplashRoute.route) {
        //CryptoView
        composable(Screen.CryptosRoute.route + ArgumentsPath.cryptoPath, arguments = listOf(
            navArgument(name = ArgumentsKeys.cryptoName) {
                type = NavType.StringType
            }
        )) {
            CryptosView(navController, it.arguments?.getString(ArgumentsKeys.cryptoName))
        }

        //Crypto Details
        composable(Screen.CryptoDetailsRoute.route + ArgumentsPath.cryptoDetailPath, arguments = listOf(
            navArgument(name = ArgumentsKeys.cryptoDetailName) {
                type = NavType.StringType
            }
        )) {
            CryptoDetails(navController, it.arguments?.getString(ArgumentsKeys.cryptoDetailName))
        }

        //Crypto Settings
        composable(Screen.CryptosSettingsRoute.route + ArgumentsPath.cryptoSettingsPath, arguments = listOf(
            navArgument(name = ArgumentsKeys.cryptoSettingsName) {
                type = NavType.StringType
            }
        )) {
            CryptoDetailSettings(navController, it.arguments?.getString(ArgumentsKeys.cryptoSettingsName))
        }

        //Account Settings
        composable(Screen.AccountRoute.route + ArgumentsPath.accounPath, arguments = listOf(
            navArgument(name = ArgumentsKeys.accountName) {
                type = NavType.StringType
            }
        )) {
            AccountSettings(navController, it.arguments?.getString(ArgumentsKeys.accountName))
        }

        //Splash
        composable(Screen.SplashRoute.route) {
            Splash(navController)
        }
    }
}

sealed class Screen(val route: String) {

    data object SplashRoute: Screen("splash")
    data object CryptoDetailsRoute: Screen("details")
    data object CryptosRoute: Screen("cryptos")
    data object CryptosSettingsRoute: Screen("settings")
    data object AccountRoute: Screen("account")

}

object ArgumentsKeys {
    const val cryptoName: String = "crypto"
    const val cryptoDetailName: String = "detail"
    const val cryptoSettingsName: String = "settings"
    const val accountName: String = "account"
}

object ArgumentsPath {

    const val cryptoPath: String = "/{crypto}"
    const val cryptoDetailPath: String = "/{detail}"
    const val cryptoSettingsPath: String = "/{settings}"
    const val accounPath: String = "/{account}"

}