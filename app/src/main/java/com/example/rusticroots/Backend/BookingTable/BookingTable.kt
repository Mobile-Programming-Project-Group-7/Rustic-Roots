package com.example.rusticroots.Backend.BookingTable

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class BookingTable {
    val db = Firebase.firestore;

    fun addtable(tableName:String){
        var tableId: String = UUID.randomUUID().toString();
        var status: String = "available";
        db.collection("table")
            .add(tableData(tableId, tableName, status))
            .addOnSuccessListener { d ->
                Log.i( "table:",  "Table created")
            }
            .addOnFailureListener { e ->
                Log.i("Error adding table", e.message.toString())
            }
    }

    fun  bookedTable(userId:String, userName:String, tableId:String, tableName:String, date: String){

        db.collection("bookedTable")
            .add(tableBookeddata(tableId, tableName, userId,
                userName, date))
            .addOnSuccessListener { d ->
                Log.i( "table:",  "Table Booked")
            }
            .addOnFailureListener { e ->
                Log.i("Error adding table", e.message.toString())
            }
    }

    fun approveTable(tableId: String){
        var status:String = "reserved";
        db.collection("table")
            .whereEqualTo("tableId", tableId)
            .get()
            .addOnSuccessListener {result ->
                for (document in result) {
                    db.document(document.id).update("status", status) ;
                }

            }
            .addOnFailureListener { e ->
                Log.i("Error adding table", e.message.toString())
            }

    }


}



