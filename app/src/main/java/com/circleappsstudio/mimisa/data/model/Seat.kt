package com.circleappsstudio.mimisa.data.model

data class Seat(
        var seatNumber: Int = 0,
        var nameUser: String = "",
        var idNumberUser: String = "",
        var seatRegisteredBy: String = ""
)

data class SeatList(
        val seatList: List<Seat>
)