package com.example.rusticroots.model.data

import com.google.firebase.firestore.Exclude
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.List


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

data class PassBookingData(
    val table: List<Tables>,
    val guests: Long,
    val time_start: LocalDateTime,
    val time_end: LocalDateTime,
)

data class Booking(
    val ref_tableID: String,
    val ref_userID: String,
    val guests: Long,
    val time_start: Date,
    val time_end: Date,
    @get:Exclude val bookingID: String = "",
)
