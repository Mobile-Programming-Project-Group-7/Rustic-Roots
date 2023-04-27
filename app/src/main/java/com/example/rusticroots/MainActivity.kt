    package com.example.rusticroots

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import com.example.rusticroots.pages.HomeScreen
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import com.example.rusticroots.pages.Navigation
import com.example.rusticroots.ui.theme.RusticRootsTheme
import com.example.rusticroots.viewmodel.PaymentGViewModel

import com.example.rusticroots.Backend.BookingTable.BookingTable
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val paymentGVM: PaymentGViewModel by viewModels() // = PaymentGViewModel(this.application)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            RusticRootsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Navigation()
                }
            }
        }
        // Check Google Pay availability
        paymentGVM.canUseGooglePay.observe(this, Observer(::setGooglePayAvailable))
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
/*
@Composable
fun MyApp() {
    val vm: ReservationsViewModel = viewModel()
    //vm.anonLogin()
    vm.getAllBookings()
    vm.getAllTables()
    //vm.createBooking(2, LocalDateTime.now(), LocalDateTime.now().plusHours(1))
    /*TESTER CODE*/

    Column {

        Button(onClick = {
            vm.checkTableAvailability(LocalDateTime.now().plusHours(2).hour)

        }) {
            Text(text = "Get Booking")
        }
        LazyColumn() {
            items(items = vm.allValidBookings) {
                Divider(thickness = 5.dp)
                Text(text = it.toString())
            }
        }
    }
}
*/
