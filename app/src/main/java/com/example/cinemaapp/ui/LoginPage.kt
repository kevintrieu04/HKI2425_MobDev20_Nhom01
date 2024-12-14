package com.example.cinemaapp.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cinemaapp.R
import com.example.cinemaapp.network.AuthResponse
import com.example.cinemaapp.network.LoginManager
import com.example.cinemaapp.ui.navigation.AppRouteName
import com.example.compose.AppTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@Composable
fun LoginPage (navController: NavHostController) {

    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}

    val context = LocalContext.current

    val manager = remember {
        LoginManager(context)
    }

    val coroutinescope = rememberCoroutineScope()

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image (
            painterResource(R.drawable.logo),
            contentDescription = "Logo"
        )

        Text (
            text = "Rotten Egg"
        )

        Spacer (modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = {
                Text(text = "Email")
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = {
                Text(text = "Password")
            },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            manager.login(email,password)
                .onEach { response ->
                    if (response is AuthResponse.Success) {
                        Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT)
                            .show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, (response as AuthResponse.Error).message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }.launchIn(coroutinescope)
            Log.i("Login", "Email: $email, Password: $password")
        }) {
            Text(text = "Login")
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(modifier = Modifier.clickable{
            //trang phuc hoi mat khau
        },
            text = "Forgot password",
            textDecoration = TextDecoration.Underline)
        Spacer(modifier = Modifier.height(8.dp))
        Text(modifier = Modifier.clickable{
            //trang dang ki
            navController.navigate(AppRouteName.Register)
        },
            text = "Don't have an account?",
            textDecoration = TextDecoration.Underline)
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Or Sign in with")
        Image(
            painterResource(R.drawable.google_symbol),
            contentDescription = "Google logo",
            modifier = Modifier.clickable {
                Log.i("Login", "Sign in with Google")
                //goi dengoogle de dang nhap
                manager.loginWithGoogle()
                    .onEach { response ->
                        if (response is AuthResponse.Success) {
                            Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT)
                                .show()
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, (response as AuthResponse.Error).message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    .launchIn(coroutinescope)
            }
                .size(100.dp)

        )
    }

}
