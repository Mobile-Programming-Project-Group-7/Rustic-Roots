package com.example.rusticroots.model.data

import com.google.firebase.firestore.Exclude
import java.util.*


/**
 * Must be given a custom tableID
 * val available:Boolean can be set to false IF there is a booking between
 * Booking.time_start-30min <= X < Booking.time_end
 */
data class Tables(
    @get:Exclude val tableID: String,
    val description: String,
    val seats: Long,
){ @get:Exclude var available: Boolean = true }


fun tableID(num: Int): String = "table_$num"

data class Booking(
    val ref_tableID: String,
    val ref_userID: String,
    val time_start: Date,
    val time_end: Date,
    @get:Exclude val bookingID: String = "",
)
