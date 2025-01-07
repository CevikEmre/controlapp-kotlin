package com.emrecevik.noroncontrolapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.emrecevik.noroncontrolapp.NoroncontrolappApplication
import com.emrecevik.noroncontrolapp.model.requestBody.RegisterBody
import com.emrecevik.noroncontrolapp.navigation.Screen
import com.emrecevik.noroncontrolapp.viewmodel.ClientViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    val usernameState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val nameState = remember { mutableStateOf("") }
    val addressState = remember { mutableStateOf("") }
    val cityState = remember { mutableStateOf("") }
    val countryState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val phoneState = remember { mutableStateOf("") }
    val clientViewModel: ClientViewModel = ClientViewModel()

    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf(false) }

    val deviceToken = NoroncontrolappApplication.Companion.deviceToken ?: "Token alınamadı"

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(text = "Register", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = usernameState.value,
                onValueChange = { usernameState.value = it },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = addressState.value,
                onValueChange = { addressState.value = it },
                label = { Text("Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = cityState.value,
                onValueChange = { cityState.value = it },
                label = { Text("City") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = countryState.value,
                onValueChange = { countryState.value = it },
                label = { Text("Country") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = phoneState.value,
                onValueChange = { phoneState.value = it },
                label = { Text("Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        val registerBody = RegisterBody(
                            username = usernameState.value,
                            password = passwordState.value,
                            name = nameState.value,
                            address = addressState.value,
                            city = cityState.value,
                            country = countryState.value,
                            email = emailState.value,
                            phone = phoneState.value,
                            enable = true,
                            deviceToken = deviceToken
                        )
                        val result = clientViewModel.register(registerBody)
                        if (result) {
                            snackbarHostState.showSnackbar("Registration successful!")
                            navController.navigate(Screen.Login.screen)
                        } else {
                            snackbarHostState.showSnackbar("Registration failed. Please try again.")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(text = "Register")
            }

            SnackbarHost(hostState = snackbarHostState)
        }
    }
}

