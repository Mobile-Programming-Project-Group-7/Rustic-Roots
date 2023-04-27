package com.example.rusticroots.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth

        setContent {

        }
    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginComposable(navController: NavController) {
    val auth = Firebase.auth
    LoginScreen(auth, navController)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(auth: FirebaseAuth, navController: NavController) {
    val context = LocalContext.current
    var emailValue by remember { mutableStateOf(TextFieldValue()) }
    var passwordValue by remember { mutableStateOf(TextFieldValue()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Login") })
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = emailValue,
                    onValueChange = { emailValue = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = passwordValue,
                    onValueChange = { passwordValue = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        signInUser(
                            auth,
                            emailValue.text,
                            passwordValue.text,
                            context,
                            navController
                        )
                    }
                ) {
                    Text("Login")
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = {
                    navController.navigate("register")
                }) {
                    Text("Don't have an account? Sign up")
                }
            }
        }
    )
}

fun signInUser(auth: FirebaseAuth, email: String, password: String, context: Context, navController: NavController) {
    if (email.isEmpty() || password.isEmpty()) {
        showToast(context, "Please fill in all the details")
        return
    }
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate("destination_route") // Replace "destination_route" with the route you want to navigate to after login
            } else {
                showToast(context, "Authentication failed")
                Log.d("LoginActivity", task.exception?.message ?: "")
            }
        }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
