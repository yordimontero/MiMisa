package com.circleappsstudio.mimisa.domain

import com.circleappsstudio.mimisa.data.model.Seat
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.coroutines.flow.Flow

interface Repo {

    interface Auth {
        /*
            Interface encargada de controlar los métodos de autenticación de Firebase.
        */

        suspend fun signInUserRepo(email: String, password: String)

        suspend fun updateUserProfileRepo(fullName: String)

        suspend fun logInUserRepo(email: String, password: String)

        suspend fun resetPasswordUserRepo(email: String)

        fun logOutUserRepo()

    }

    interface SeatReservation {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore.
        */
        suspend fun fetchIterator() : Flow<Resource<Int>>

        suspend fun fetchSeatLimit(): Resource<Int>

        suspend fun saveSeatReserved(seatNumber: Int, nameUser: String, idNumberUser: String)

        suspend fun addIterator(seatNumber: Int)

        suspend fun fetchRegisteredSeatsByUserName(): Resource<List<Seat>>?

    }

    interface Intention {

        suspend fun saveIntention(category: String, intention: String)

    }

}