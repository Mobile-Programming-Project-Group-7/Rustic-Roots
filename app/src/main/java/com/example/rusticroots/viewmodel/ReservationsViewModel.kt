package com.example.rusticroots.viewmodel

import android.accounts.AuthenticatorDescription
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rusticroots.model.data.Reservations
import com.example.rusticroots.model.data.Tables
import com.example.rusticroots.model.data.tableID
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ReservationsViewModel: ViewModel() {

    var allTables = mutableStateListOf<Tables>()
        private set
    /**
     * Creates a table with a specified document ID
     * document id will have the structure of "table_x" where x is the passed integer
     */
    fun createTable(tableID: Int, description: String = "Round table", seats: Int = 2){
        val id = tableID(tableID)
        val table = Tables(id, description, seats)
        viewModelScope.launch {
            Firebase.firestore.collection("tables")
                .document(id)
                .set(table)
                .addOnSuccessListener {
                    Log.d("******", "Table created!")
                }
                .addOnFailureListener {
                    Log.e("******", it.message.toString())
                }
        }
    }

    /**
     * Grabs all the Tables in the database.
     * Creates a list of 'data class Tables()' and adds them to ViewModel var "allTables"
     */
    fun getAllTables(){
        viewModelScope.launch {
            Firebase.firestore.collection("Tables")
                .get()
                .addOnSuccessListener {
                    val tables = mutableStateListOf<Tables>()
                    it.documents.forEach{ doc ->
                        val desc = doc.get("description").toString()
                        val seats = doc.get("seats").toString().toInt()
                        tables.add(Tables(doc.id, desc, seats))
                    }
                    allTables.clear()
                    allTables.addAll(tables)
                }
        }
    }
}