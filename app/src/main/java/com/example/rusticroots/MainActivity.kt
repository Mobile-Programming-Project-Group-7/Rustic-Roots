package com.example.rusticroots

import android.os.Bundle
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import com.example.rusticroots.pages.HomeScreen
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.rusticroots.ui.theme.RusticRootsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            RusticRootsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}
/*
               val scaffoldState=rememberScaffoldState()
                val scope=rememberCoroutineScope()
                Scaffold(
                    scaffoldState=scaffoldState,
                    topBar={
                        AppBar(
                            onNavigationIconClick={
                                scope.launch {
                                    scaffoldState.drawerState.open()
                                }

                            }
                        )
                    },
                    drawerContent={
                        DrawerHeader()
                        DrawerBody(
                            items=listOf(
                                MenuItem(
                                    "profile",
                                    title="Profile",
                                    contentDescription="Go to your profile page",
                                    icon=Icons.Default.Person
                                ),
                                MenuItem(
                                    "Home",
                                    title="Home",
                                    contentDescription="Go to home screen",
                                    icon=Icons.Default.Home
                                ),
                                MenuItem(
                                    "favourite",
                                    title="Orders",
                                    contentDescription="Your favourite orders",
                                    icon=Icons.Default.Favorite
                                ),
                                MenuItem(
                                    "settings",
                                    title="Settings",
                                    contentDescription="Go to settings screen",
                                    icon=Icons.Default.Settings
                                ),
                                MenuItem(
                                    "feedback",
                                    title="Feedback",
                                    contentDescription="Go to feedback screen",
                                    icon=Icons.Default.Notifications
                                ),

                                MenuItem(
                                    "help",
                                    title="Help",
                                    contentDescription="Get help",
                                    icon=Icons.Default.Info
                                ),
                            ),
                            onItemClick={
                                println("Clicked on ${it.title}")
                            }
                        )
                    }){

                }


                      val paymentGVM: PaymentGViewModel by viewModels() // = PaymentGViewModel(this.application)


                val db=Firebase.firestore;
                val user=hashMapOf(
                    "first" to "Adaddd",
                    "last" to "Lovelace",
                    "born" to 1815
                )
                db.collection("users")
                    .add(user)
                    .addOnSuccessListener { d ->
                        Log.i("DocumentSnapshot added with ID", "created successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.i("Error adding document", e.toString())
                    }
// These four closing  bracket are kept because of commenting below payment codes.
            }}}}


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
        val paymentGVM: PaymentGViewModel by viewModels() // = PaymentGViewModel(this.application)

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