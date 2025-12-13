package com.example.AuthApp.auth.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.example.AuthApp.R
import com.example.AuthApp.auth.AuthViewModel
import com.example.AuthApp.data.TokenManager
import com.example.AuthApp.data.remote.models.UserDto

@Composable
fun UsersScreen(vm: AuthViewModel, onLogout: () -> Unit) {
    val state = vm.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        if (TokenManager.token != null) {
            vm.loadUsers()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onLogout) { Text(stringResource(R.string.logout)) }
        Spacer(Modifier.height(8.dp))

        if (TokenManager.token == null) {
            Text(stringResource(R.string.need_to_login))
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