package com.example.four_practik.auth

import android.util.Log as AndroidLog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.four_practik.data.remote.models.GroupDto
import com.example.four_practik.data.remote.models.PersonDto
import com.example.four_practik.data.remote.models.RegisterRequest
import com.example.four_practik.data.remote.models.UserDto
import com.google.gson.Gson
import com.google.gson.GsonBuilder

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(vm: AuthViewModel, onRegistered: () -> Unit, onGoToLogin: () -> Unit) {
    // Minimal fields only
    var login by remember { mutableStateOf("john_doe") }
    var password by remember { mutableStateOf("securePassword123") }
    var selectedGroup by remember { mutableStateOf<GroupDto?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val state = vm.uiState.collectAsState().value

    // Default values for all other fields (computed from login)
    val email = remember(login) { "${login.replace("_", ".")}@example.com" }
    val phoneNumber = "+1234567890"
    val firstName = "Ivan"
    val lastName = "Ivanov"
    val patronymic = "Ivanovich"
    val dateOfBirth = "2000-01-31"
    val gender = "MALE"
    
    // Gson for JSON formatting
    //val gson = remember { GsonBuilder().setPrettyPrinting().create() }

    // Fetch groups when screen is first displayed
    //LaunchedEffect(Unit) {
    if (state.groups.isEmpty()) {
        vm.loadGroups()
    }
    //}

    // Generate JSON preview
    val jsonPreview = remember(login, password, email, selectedGroup) {
        if (selectedGroup != null) {
            try {
                val person = PersonDto(
                    firstName = firstName,
                    lastName = lastName,
                    middleName = patronymic,
                    birthDate = dateOfBirth,
                    gender = gender,
                    groupId = selectedGroup!!.id
                )
                val request = RegisterRequest(
                    login = login,
                    password = password,
                    email = email,
                    phoneNumber = phoneNumber,
                    roleId = 1,
                    authAllowed = true,
                    person = person
                )
                //gson.toJson(request)  //HERE HERE HERE
            } catch (e: Exception) {
                "Error generating JSON: ${e.message}"
            }
        } else {
            "Select a group to see JSON preview"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        
        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Login *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))
        
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password *") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))
        
        // Groups dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedGroup?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Group") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                enabled = state.groups.isNotEmpty()
            )
            DropdownMenu(
                expanded = expanded && state.groups.isNotEmpty(),
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                state.groups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group.name) },
                        onClick = {
                            selectedGroup = group
                            expanded = false
                        }
                    )
                }
            }
        }
        if (state.groups.isEmpty() && !state.isLoading) {
            Text(
                text = "No groups available. ${state.errorMessage ?: ""}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        
        /*Spacer(Modifier.height(24.dp))
        
        // JSON Preview Card at the bottom
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "JSON to be sent:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = jsonPreview,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }*/
        
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                if (selectedGroup != null) {
                    //AndroidLog.d("RegisterScreen", "Selected Group - ID: ${selectedGroup!!.id}, Name: ${selectedGroup!!.name}")
                    vm.register(
                        login = login,
                        email = email,
                        password = password,
                        phoneNumber = phoneNumber,
                        firstName = firstName,
                        lastName = lastName,
                        patronymic = patronymic,
                        dateOfBirth = dateOfBirth,
                        gender = gender,
                        groupId = selectedGroup!!.id, // Using the groupId from the selected group
                        onSuccess = onRegistered
                    )
                }
            },
            enabled = !state.isLoading && selectedGroup != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register & Logout")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onGoToLogin, modifier = Modifier.fillMaxWidth()) {
            Text("Go to Login")
        }
        if (state.isLoading) {
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        }
        
        // Display success with token info  ???
        if (state.registrationSuccess && state.token != null) {
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Registration Successful!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "HTTP Response Code: ${state.tokenResponseCode ?: "N/A"}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Token:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = state.token,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Check Logcat for full request/response details",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    )
                }
            }
        }
        
        // Display error
        state.errorMessage?.let {
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Registration Error",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
fun LoginScreen(vm: AuthViewModel, onLoggedIn: () -> Unit) {
    var login by remember { mutableStateOf("john_doe") }
    var password by remember { mutableStateOf("securePassword123") }
    val state = vm.uiState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(24.dp))
        
        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Login") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(16.dp))
        
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(24.dp))
        
        Button(
            onClick = { vm.login(login, password, onLoggedIn) },
            enabled = !state.isLoading && login.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        
        if (state.isLoading) {
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        }
        
        state.errorMessage?.let {
            Spacer(Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun UsersScreen(vm: AuthViewModel, onLogout: () -> Unit) {
    val state = vm.uiState.collectAsState().value

    //LaunchedEffect(Unit) {
        if (state.token != null) {
            vm.loadUsers()
   //     }
   // }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onLogout) { Text("Logout") }
        Spacer(Modifier.height(8.dp))
        
        if (state.token == null) {
            Text("Please login to view users")
        } else if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            UsersList(users = state.users)
        }
        state.errorMessage?.let { Text(it) }
    }
}

@Composable
fun UsersList(users: List<UserDto>) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) { user ->
            UserTile(user = user)
        }
    }
}

@Composable
private fun UserTile(user: UserDto) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.login ?: "User ${user.userId ?: "N/A"}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }
            
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    user.userId?.let {
                        Text(
                            text = "User ID: $it",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    user.phoneNumber?.let {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Phone: $it",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    user.createdDate?.let {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Created: $it",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    user.lastLoginDate?.let {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Last Login: $it",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}


