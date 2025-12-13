package com.example.AuthApp.auth.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.AuthApp.auth.AuthViewModel
import com.example.AuthApp.data.TokenManager
import com.example.AuthApp.data.remote.models.GroupDto
import com.example.AuthApp.R

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


    // Fetch groups when screen is first displayed
    LaunchedEffect(Unit) {
        if (state.groups.isEmpty()) {
            vm.loadGroups()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.register), style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text(stringResource(R.string.login_textfield)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password_textfield)) },
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
                label = { Text(stringResource(R.string.group_selecter)) },
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

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                if (selectedGroup != null) {
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
            Text(stringResource(R.string.reg_n_logout))
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onGoToLogin, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.go_to_login))
        }
        if (state.isLoading) {
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}