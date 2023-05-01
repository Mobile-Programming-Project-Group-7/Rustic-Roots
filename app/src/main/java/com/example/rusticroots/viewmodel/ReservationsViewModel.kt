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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.time.*
import java.util.*

class ReservationsViewModel: ViewModel() {
    private val db = Firebase.firestore
    private var user = mutableStateOf(Firebase.auth.currentUser)

    //Holds information about all the Tables in the restaurant
    var allTables = mutableStateListOf<Tables>()

    //Holds information about all of the bookings that are not expired
    private var allValidBookings = mutableStateListOf<Booking>()
    private var daysBookings = mutableStateListOf<Booking>()


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
    private fun Date.toLocalDate(zone: ZoneId = ZoneId.systemDefault()): LocalDate = this.toInstant().atZone(zone).toLocalDate()
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
                    allTables.clear()
                    allTables.addAll(tables)
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
                    allValidBookings.clear()
                    allValidBookings.addAll(docs)
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

    private fun getDayBookings(date: LocalDate = LocalDate.now()){
        val book = mutableStateListOf<Booking>()
        allValidBookings.forEach {
            if (it.time_start.toLocalDate() == date){
                book.add(it)
            }
        }
        daysBookings.clear()
        daysBookings.addAll(book)
    }

    fun checkTimeAvailability(
        hour: Int,
        minute: Int = 0,
        date: LocalDate = LocalDate.now(),
        tables: List<Tables>,
        setTableList: (table: List<Tables>) -> Unit
    ) {
        val time = LocalDateTime.of(date, LocalTime.of(hour, minute))
        val t = mutableStateListOf<Tables>()
        if (daysBookings.isEmpty()) t.addAll(tables)
        daysBookings.forEach{
            val table = tables.filter { table -> it.ref_tableID == table.tableID }
            if (it.time_start.toLocalDateTime() >= time) {
                t.add(table[0])
            }
        }
        setTableList(t)
    }

    /**
     * Checks available tables by the hour and returns a list of them
     */
    fun checkTableAvailability(
        hour: Int,
        minute: Int = 0,
        date: LocalDate = LocalDate.now(),
    ): List<Tables> {
        getDayBookings(date)
        val time = LocalDateTime.of(date, LocalTime.of(hour, minute))
        val minTime = time.plusMinutes(30)
        // The returned values
        val t = mutableStateListOf<Tables>()
        if (daysBookings.isEmpty()) return allTables
        daysBookings.forEach {
            val table = allTables.filter { table -> it.ref_tableID == table.tableID }
            if (it.time_start < minTime.toDate() && time.toDate() < it.time_end) {
                table[0].available = false
            } else {
                t.add(table[0])
            }
        }
        return t
    }

    /**
     *     if the amount of guests are more than the largest table, add the smallest table until none needed.
     *     if the amount of people needed are larger than the smallest table, keep looking throuhg the best fit until match
     *     else just add the smallest table
     */
    fun tableSelector(it: List<Tables>, guests: Int, setTable: (table: List<Tables>) -> Unit){
        val seatMapping = it.associate {tables ->  tables.tableID to tables.seats }.toMutableMap()
        var maxSeat = seatMapping.maxBy{ value -> value.value }
        var minSeat = seatMapping.minBy{ value -> value.value }
        var remaining = guests.toLong()
        val tables = mutableStateListOf<Tables>()
        if(maxSeat.value <= remaining){
            while (remaining > 0) {
                it.forEach { table ->
                    if (table.tableID == maxSeat.key){
                        tables.add(table)
                    }
                }
                remaining -= maxSeat.value.toInt()
                seatMapping.remove(maxSeat.key)
                if (seatMapping.isNotEmpty()) maxSeat = seatMapping.minBy{ value -> value.value } //?: seatMapping.toMap()) as Map.Entry<String, Long>
            }
        } else if(minSeat.value < remaining){
            var filteredMap: MutableMap<String, Long>
            while(minSeat.value < remaining){
                filteredMap = seatMapping.filterValues {value -> value > minSeat.value }.toMutableMap()
                minSeat = filteredMap.minBy{ value -> value.value }
                if(minSeat.value >= remaining){
                    it.forEach { table ->
                        if (table.tableID == minSeat.key){
                            tables.add(table)
                        }
                    }
                }
            }
        } else {
            it.forEach { table ->
                if (table.tableID == minSeat.key){
                    tables.add(table)
                }
            }
        }
        setTable(tables)
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