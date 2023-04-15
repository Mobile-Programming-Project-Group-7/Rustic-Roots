package com.example.rusticroots.model.data

import com.google.firebase.firestore.Exclude
import java.util.Date


/**
 * Must be given a custom tableID
 */
data class Tables(
    @get:Exclude val tableID: String,
    val description: String,
    val seats: Int,
)

fun tableID(num: Int): String = "table_$num"

data class Reservations(
    val tableID: String,
    val time_start: Date,
    val time_end: Date,
)
