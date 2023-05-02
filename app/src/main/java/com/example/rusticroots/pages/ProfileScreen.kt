package com.example.rusticroots.pages



import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Button
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.rusticroots.view.LoginActivity
import com.example.rusticroots.view.SignUpActivity

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val launchLoginActivity =
        remember {
            {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }
        }
    val launchSignUpActivity = remember {
        {
            val intent = Intent(context, SignUpActivity::class.java)
            context.startActivity(intent)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {


            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = launchLoginActivity) {
                Text(
                    text = "Login",
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}
