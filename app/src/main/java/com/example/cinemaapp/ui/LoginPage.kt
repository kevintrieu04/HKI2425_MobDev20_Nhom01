package com.example.cinemaapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cinemaapp.R

@Composable
fun LoginPage (){

    var username by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}

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
            value = username,
            onValueChange = {username = it},
            label = {
                Text(text = "Username")
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
            Log.i("Login", "Username: $username, Password: $password")
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
            }
                .size(100.dp)

        )
    }

}