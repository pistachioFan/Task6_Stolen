package com.example.AuthApp.auth

import com.example.AuthApp.auth.Screens.RegisterScreen
import com.example.AuthApp.auth.Screens.LoginScreen
import com.example.AuthApp.auth.Screens.UsersScreen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


enum class AuthScreenRoute {
    Register,
    Login,
    Users
}

@Composable
fun AuthApp() {
    val navController = rememberNavController()
    val vm: AuthViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = AuthScreenRoute.Register.name
    ) {
        composable(AuthScreenRoute.Register.name) {
            RegisterScreen(
                vm = vm,
                onRegistered = {
                    vm.logout { navController.navigate(AuthScreenRoute.Login.name) }
                },
                onGoToLogin = { navController.navigate(AuthScreenRoute.Login.name) }
            )
        }
        composable(AuthScreenRoute.Login.name) {
            LoginScreen(
                vm = vm,
                onLoggedIn = { navController.navigate(AuthScreenRoute.Users.name) }
            )
        }
        composable(AuthScreenRoute.Users.name) {
            UsersScreen(
                vm = vm,
                onLogout = {
                    vm.logout { navController.navigate(AuthScreenRoute.Login.name) }
                }
            )
        }
    }
}




