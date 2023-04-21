package com.example.rusticroots.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rusticroots.model.data.Booking
import com.example.rusticroots.model.data.Constants.COLLECTION_BOOKINGS
import com.example.rusticroots.model.data.Constants.COLLECTION_TABLES
import com.example.rusticroots.model.data.Tables
import com.example.rusticroots.model.data.tableID
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.*

class ReservationsViewModel: ViewModel() {
    private val db = Firebase.firestore
    //TODO user Auth
    var user = mutableStateOf<FirebaseUser?>(null)

    private var _allTables = mutableStateListOf<Tables>()
    val allTables: List<Tables> = _allTables

    private var _allBookings = mutableStateListOf<Booking>()
    val allBookings: List<Booking> = _allBookings

    private var _userBookings = mutableStateListOf<Booking>()
    val userBookings: List<Booking> = _userBookings

    var tableRef = mutableStateOf<Tables?>(null)

    private fun createTableDocRef(id: Int): DocumentReference = db.document("$COLLECTION_TABLES/table_$id")


    /**
     * Creates a table with a specified document ID
     * document id will have the structure of "table_x" where x is the passed integer
     */
    fun createTable(tableID: Int, description: String = "Round table", seats: Long = 2){
        val id = tableID(tableID)
        val table = Tables(id, description, seats)
        viewModelScope.launch {
            db.collection("tables")
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
            db.collection(COLLECTION_TABLES)
                .get()
                .addOnSuccessListener {
                    val tables = mutableStateListOf<Tables>()
                    it.documents.forEach{ doc ->
                        val desc = doc.get("description") as String
                        val seats = doc.get("seats") as Long
                        tables.add(Tables(doc.id, desc, seats))
                    }
                    _allTables.clear()
                    _allTables.addAll(tables)
                }
        }
    }

    /**
     * Gets the booking document by the user ID
     */
    fun getBookingsByUser(){
        viewModelScope.launch {
            user.value?.let { fUser ->
                db.collection(COLLECTION_BOOKINGS)
                    .whereEqualTo("ref_userID", fUser.uid)
                    .get().addOnSuccessListener {
                        val docs = mutableStateListOf<Booking>()
                        it.documents.forEach { doc ->
                            val tId = doc.get("ref_tableID") as DocumentReference
                            val uId = doc.get("ref_userID") as String
                            val start = doc.get("time_start") as com.google.firebase.Timestamp
                            val end = doc.get("time_end") as com.google.firebase.Timestamp
                            docs.add(Booking(tId, uId, start.toDate(), end.toDate(), doc.id))
                        }
                        _userBookings.clear()
                        _userBookings.addAll(docs)
                    }
                    .addOnFailureListener {
                        Log.e("!!!!!!!!!!!!!!!!!!!!!!!!!!!!", it.message.toString())
                    }
            }

        }
    }

    /**
     * Pass Booking data class to retrieve table info from cloud
     * Returns Tables data class
     */
    fun getTableFromBooking(booking: Booking): Tables? {
        booking.ref_tableID.get()
            .addOnSuccessListener {
                val desc = it.get("description") as String
                val seats = it.get("seats") as Long

                tableRef.value = Tables(booking.ref_tableID.id, desc, seats)
            }
        return tableRef.value
    }

    //TODO
    fun checkTableAvailability(booking: Booking): Boolean {
        val id = booking.ref_tableID

        val table = allTables[1]
        //if (){        }
        return table.available
    }

    /**
     * Creates a booking
     */
    fun createBooking(tableID: Int, timeStart: Date, timeEnd:Date){
        viewModelScope.launch {
            user.value?.let {
                val booking = Booking(createTableDocRef(tableID), it.uid, timeStart, timeEnd)

                db.collection(COLLECTION_BOOKINGS)
                    .add(booking)
                    .addOnSuccessListener {
                        Log.d("******", "Booking created!")
                    }
                    .addOnFailureListener {err ->
                        Log.e("******", err.message.toString())
                    }
            }
        }
    }

    /**
     * Creates an anonymous user
     */
    fun anonLogin(){
        viewModelScope.launch {
            Firebase.auth.signInAnonymously().addOnSuccessListener {
                user.value = it.user
                Log.d("******", "Anon sign in done!!")
            }
        }
    }
}