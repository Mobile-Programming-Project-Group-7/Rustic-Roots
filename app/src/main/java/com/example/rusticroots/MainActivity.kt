package com.example.rusticroots

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity


import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import com.example.rusticroots.ui.modules.GooglePayButton
import com.example.rusticroots.ui.theme.RusticRootsTheme
import com.example.rusticroots.viewmodel.PaymentGViewModel


import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.rusticroots.ui.theme.RusticRootsTheme




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val paymentGVM: PaymentGViewModel by viewModels()

        setContent {


        setContent {
            installSplashScreen()

            RusticRootsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    MyApp(paymentGVM = paymentGVM)
                }
            }
        }
        // Check Google Pay availability
        paymentGVM.canUseGooglePay.observe(this, Observer(::setGooglePayAvailable) )
    }
    /**
     * If Google pay is not available, user gets notified
     */
    private fun setGooglePayAvailable(available: Boolean) {
        if (available) {
            Log.d("**********************","AVAILABLE")
        } else {
            Toast.makeText(
                this,
                getString(R.string.google_pay_NA),
                Toast.LENGTH_LONG
            ).show()
            Log.e("**********************","NOT AVAILABLE")
        }
    }
}

                    Greeting("Rustic Roots")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Welcome To $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RusticRootsTheme {
        Greeting("Rustic Roots")



@Composable
fun MyApp(paymentGVM: PaymentGViewModel) {
    Column() {
        GooglePayButton(paymentGVM = paymentGVM)
    }
}
