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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.time.*
import java.util.*

class ReservationsViewModel: ViewModel() {
    private val db = Firebase.firestore

    //TODO user Auth
    var user = mutableStateOf<FirebaseUser?>(null)

    //Holds information about all the Tables in the restaurant
    var _allTables = mutableStateListOf<Tables>()

    //Holds information about all of the bookings that are not expired
    private var _allValidBookings = mutableStateListOf<Booking>()
    val allValidBookings: List<Booking> = _allValidBookings

    //Holds all the Bookings a user made
    private var _userBookings = mutableStateListOf<Booking>()
    val userBookings: List<Booking> = _userBookings

    /**
     * Converters:
     * Date to LocalDateTime object
     * LocalDateTime to Date object
     * (OLD CODE: Timestamp to LocalDateTime)
     */
    private fun Date.toLocalDateTime(zone: ZoneId = ZoneId.systemDefault()): LocalDateTime = this.toInstant().atZone(zone).toLocalDateTime()
    private fun LocalDateTime.toDate(zone: ZoneId = ZoneId.systemDefault()): Date = Date.from(this.atZone(zone).toInstant())
    //private fun Timestamp.toLocalDateTime(zone: ZoneId = ZoneId.systemDefault()): LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(seconds * 1000 + nanoseconds / 1000000), zone)


    /**
     * Creates a table with a specified document ID
     * document id will have the structure of "table_x" where x is the passed integer
     */
    fun createTable(tableID: Int, description: String = "Round table", seats: Long = 2) {
        val id = tableID(tableID)
        val table = Tables(id, description, seats)
        viewModelScope.launch {
            db.collection(COLLECTION_TABLES)
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
    fun getAllTables() {
        viewModelScope.launch {
            db.collection(COLLECTION_TABLES)
                .get()
                .addOnSuccessListener {
                    val tables = mutableStateListOf<Tables>()
                    it.documents.forEach { doc ->
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
     * Grabs all the upcoming bookings
     * !! WIll NOT GRAB PAST BOOKINGS
     */
    fun getAllBookings() {
        viewModelScope.launch {
            db.collection(COLLECTION_BOOKINGS)
                .whereGreaterThanOrEqualTo("time_end", Timestamp.now())
                .get().addOnSuccessListener {
                    val docs = mutableStateListOf<Booking>()
                    it.documents.forEach { doc ->
                        val tId = doc.get("ref_tableID") as String
                        val uId = doc.get("ref_userID") as String
                        val start = doc.get("time_start") as Timestamp
                        val end = doc.get("time_end") as Timestamp
                        docs.add(Booking(tId, uId, start.toDate(), end.toDate(), doc.id))
                    }
                    _allValidBookings.clear()
                    _allValidBookings.addAll(docs)
                }
                .addOnFailureListener {
                    Log.e("!!!!!!!getAllBookings!!!!!!!!", it.message.toString())
                }

        }
    }

    /**
     * Gets the booking document by the user ID
     * NEEDS USER ID
     */
    fun getBookingsByUser() {
        viewModelScope.launch {
            user.value?.let { fUser ->
                db.collection(COLLECTION_BOOKINGS)
                    .whereEqualTo("ref_userID", fUser.uid)
                    .get().addOnSuccessListener {
                        val docs = mutableStateListOf<Booking>()
                        it.documents.forEach { doc ->
                            val tId = doc.get("ref_tableID") as String
                            val uId = doc.get("ref_userID") as String
                            val start = doc.get("time_start") as Timestamp
                            val end = doc.get("time_end") as Timestamp
                            docs.add(Booking(tId, uId, start.toDate(), end.toDate(), doc.id))
                        }
                        _userBookings.clear()
                        _userBookings.addAll(docs)
                    }
                    .addOnFailureListener {
                        Log.e("!!!!!!getBookingsByUser!!!!!!", it.message.toString())
                    }
            }

        }
    }

    //TODO
    fun checkTableAvailability(hour: Int, minute: Int = 0, date: LocalDate = LocalDate.now()) {
        val time = LocalDateTime.of(date, LocalTime.of(hour, minute))
        val minTime = time.plusMinutes(30)
        val allTables: List<Tables> = _allTables
        allValidBookings.forEach {
            val table = allTables.filter { table -> it.ref_tableID == table.tableID }
            if (it.time_start < minTime.toDate() && time.toDate() < it.time_end) {
                table[0].available = false
            }
            Log.e("***********AVAILABLE", table[0].available.toString())
        }
    }

        /**
         * Creates a booking
         */
    fun createBooking(tableID: Int, timeStart: LocalDateTime, timeEnd: LocalDateTime) {
        viewModelScope.launch {
            user.value?.let {
                val booking =
                    Booking(tableID(tableID), it.uid, timeStart.toDate(), timeEnd.toDate())
                db.collection(COLLECTION_BOOKINGS)
                    .add(booking)
                    .addOnSuccessListener {
                        Log.d("******", "Booking created!")
                    }
                    .addOnFailureListener { err ->
                        Log.e("!!!!!!!!BOOKING CREATION FAILED!!!!!!!", err.message.toString())
                    }
            }
        }
    }

    //TODO
    /**
     * Creates an anonymous user
     */
    fun anonLogin() {
        viewModelScope.launch {
            Firebase.auth.signInAnonymously().addOnSuccessListener {
                user.value = it.user
                Log.d("******", "Anon sign in done!!")
            }
        }
    }
}
