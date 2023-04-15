package com.example.rusticroots.model.data

import java.util.Date

data class Tables(
    val description: String,
    val seats: Int,
)

data class Reservations(
    val time_start: Date,
    val time_end: Date,
)
