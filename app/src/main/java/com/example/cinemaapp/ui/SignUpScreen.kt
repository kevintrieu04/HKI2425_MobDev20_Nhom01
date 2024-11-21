package com.example.cinemaapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cinemaapp.network.AuthResponse
import com.example.cinemaapp.network.LoginManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavHostController) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val coroutinescope = rememberCoroutineScope()
    val context = LocalContext.current
    val manager = remember {
        LoginManager(context)
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Back to Sign In") },
                modifier = Modifier,
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                navigationIcon = {
                    IconButton(onClick = {
                    /*Ve lai man hinh Sign In*/
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        },
        content = {
                paddingValues ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)){
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Registration",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    maxLines = 1,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(300.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    maxLines = 1,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(300.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    maxLines = 1,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(300.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                /*Register*/
                    manager.createAccount(email, password)
                        .onEach { respond ->
                            if (respond is AuthResponse.Success) {
                                Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT)
                                    .show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, (respond as AuthResponse.Error).message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }.launchIn(coroutinescope)
                },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)) {
                    Text(text = "Register")
                }
            }

        }
    )

}