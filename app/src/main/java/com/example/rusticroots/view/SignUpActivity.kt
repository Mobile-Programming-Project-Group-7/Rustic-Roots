package com.example.rusticroots.view



import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.rusticroots.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth
        setContent {
            RegisterScreen(auth);
        }

    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun RegisterScreen(auth: FirebaseAuth) {
        val context = LocalContext.current
        var emailValue by remember { mutableStateOf(TextFieldValue()) }
        var passwordValue by remember { mutableStateOf(TextFieldValue()) }
        val launchLoginActivity =
            remember {
                {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                }
            }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Register") })
            },
            content = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.rustic_roots),
                        contentDescription = "Rustic Roots logo",
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

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
                            signUpUser(
                                auth,
                                emailValue.text,
                                passwordValue.text,
                                context
                            )
                        }
                    ) {
                        Text("Login")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(onClick = launchLoginActivity)  {
                        Text("already have an account? Login")
                    }
                }
            }
        )
    }

    private fun signUpUser(auth: FirebaseAuth, email: String, password: String, context: Context) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please fill in all details", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                }
            }
    }
}