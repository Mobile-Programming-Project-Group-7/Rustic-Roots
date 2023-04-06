    package com.example.rusticroots

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.rusticroots.Backend.BookingTable.BookingTable
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase




    class MainActivity :ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)


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
        }
    }