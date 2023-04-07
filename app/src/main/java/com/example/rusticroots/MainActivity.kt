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
import com.example.rusticroots.Backend.BookingTable.BookingTable
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

   

        val paymentGVM: PaymentGViewModel by viewModels() // = PaymentGViewModel(this.application)

        val db = Firebase.firestore;
        val user = hashMapOf(
            "first" to "Adaddd",
            "last" to "Lovelace",
            "born" to 1815
        )
        db.collection("users")
            .add(user)
            .addOnSuccessListener { d->
                Log.i( "DocumentSnapshot added with ID", "created successfully")
            }
            .addOnFailureListener { e ->
                Log.i("Error adding document", e.toString())
            }


        setContent {
            RusticRootsTheme {
             val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()
                Scaffold(
                    scaffoldState= scaffoldState,
                    topBar = {
                             AppBar(
                                 onNavigationIconClick = {
                                     scope.launch {
                                         scaffoldState.drawerState.open()
                                     }

                                 }
                             )
                    },
                    drawerContent = {
                        DrawerHeader()
                        DrawerBody(
                            items=listOf(
                                MenuItem("profile",
                                    title = "Profile",
                                    contentDescription = "Go to your profile page",
                                    icon = Icons.Default.Person
                                ),
                            MenuItem("Home",
                                title = "Home",
                                contentDescription = "Go to homescreen",
                                icon = Icons.Default.Home
                                ),
                                MenuItem("favourite",
                                    title = "Orders",
                                    contentDescription = "Your favourite orders",
                                    icon = Icons.Default.Favorite
                                ),
                                MenuItem("settings",
                                    title = "Settings",
                                    contentDescription = "Go to settings screen",
                                    icon = Icons.Default.Settings
                                ),
                                MenuItem("feedback",
                                    title = "Feedback",
                                    contentDescription = "Go to feedback screen",
                                    icon = Icons.Default.Notifications
                                ),
                                MenuItem("help",
                                    title = "Help",
                                    contentDescription = "Get help",
                                    icon = Icons.Default.Info
                                ),
                        ),
                            onItemClick= {
                                println("Clicked on ${it.title}")
                            }
                        )
                    }
                ) {


                }
                // A surface container using the 'background' color from the theme

                Surface(

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

@Composable
fun MyApp(paymentGVM: PaymentGViewModel) {
    Column() {
        GooglePayButton(paymentGVM = paymentGVM)
    }
}
