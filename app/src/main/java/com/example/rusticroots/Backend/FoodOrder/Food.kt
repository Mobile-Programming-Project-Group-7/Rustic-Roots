package com.example.rusticroots.Backend.FoodOrder

import android.util.Log
import com.example.rusticroots.Backend.BookingTable.tableBookeddata
import com.example.rusticroots.Backend.BookingTable.tableData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class Food {
    val db = Firebase.firestore;

    fun addFood(foodName:String){
        var foodId: String = UUID.randomUUID().toString();
        db.collection("foods")
            .add(addFood(foodId, foodName))
            .addOnSuccessListener { d ->
                Log.i( "table:",  "Table created")
            }
            .addOnFailureListener { e ->
                Log.i("Error adding table", e.message.toString())
            }
    }

    fun  orderedFood(userId:String, userName:String, foodId:String, foodName:String, date: String){

        db.collection("orderedFood")
            .add(
                orderFood(userId, userName,  foodId,foodName, date)
            )
            .addOnSuccessListener { d ->
                Log.i( "table:",  "Table Booked")
            }
            .addOnFailureListener { e ->
                Log.i("Error adding table", e.message.toString())
            }
    }

}