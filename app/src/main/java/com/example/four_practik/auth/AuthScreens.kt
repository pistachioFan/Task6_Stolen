package com.example.four_practik.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.four_practik.data.remote.models.UserDto

enum class AuthScreenRoute { Register, Login, Users }

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
                    // as per assignment, logout right after registration
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

@Composable
fun RegisterScreen(vm: AuthViewModel, onRegistered: () -> Unit, onGoToLogin: () -> Unit) {
    var email by remember { mutableStateOf("eve.holt@reqres.in") }
    var password by remember { mutableStateOf("pistol") }
    val state = vm.uiState.value

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register")
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(12.dp))
        Button(onClick = { vm.register(email, password, onRegistered) }, enabled = !state.isLoading) {
            Text("Register & Logout")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onGoToLogin) { Text("Go to Login") }
        if (state.isLoading) { Spacer(Modifier.height(8.dp)); CircularProgressIndicator() }
        state.errorMessage?.let { Text(it) }
    }
}

@Composable
fun LoginScreen(vm: AuthViewModel, onLoggedIn: () -> Unit) {
    var email by remember { mutableStateOf("eve.holt@reqres.in") }
    var password by remember { mutableStateOf("cityslicka") }
    val state = vm.uiState.value

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login")
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(12.dp))
        Button(onClick = { vm.login(email, password, onLoggedIn) }, enabled = !state.isLoading) {
            Text("Login")
        }
        if (state.isLoading) { Spacer(Modifier.height(8.dp)); CircularProgressIndicator() }
        state.errorMessage?.let { Text(it) }
    }
}

@Composable
fun UsersScreen(vm: AuthViewModel, onLogout: () -> Unit) {
    val state = vm.uiState.value

    LaunchedEffect(Unit) {
        vm.loadUsers()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onLogout) { Text("Logout") }
        Spacer(Modifier.height(8.dp))
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            UsersList(users = state.users)
        }
        state.errorMessage?.let { Text(it) }
    }
}

@Composable
private fun UsersList(users: List<UserDto>) {
    LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
        items(users) { user ->
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "${user.first_name} ${user.last_name}")
                Text(text = user.email)
                val painter = rememberAsyncImagePainter(user.avatar)
                Image(painter = painter, contentDescription = null, modifier = Modifier.height(120.dp), contentScale = ContentScale.Crop)
            }
        }
    }
}


