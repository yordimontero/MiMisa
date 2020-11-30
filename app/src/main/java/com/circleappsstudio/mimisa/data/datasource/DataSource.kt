package com.circleappsstudio.mimisa.data.datasource

import com.circleappsstudio.mimisa.data.model.Intention
import com.circleappsstudio.mimisa.data.model.Seat
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.coroutines.flow.Flow

interface DataSource {

    interface Auth {
        /*
            Interface encargada de controlar los métodos de autenticación de Firebase.
        */
        suspend fun signInUserDataSource(email: String, password: String)

        suspend fun updateUserProfileDataSource(fullName: String)

        suspend fun logInUserDataSource(email: String, password: String)

        suspend fun resetPasswordUserDataSource(email: String)

        fun logOutUser()

    }

    interface SeatReservation {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore.
        */
        suspend fun fetchIterator() : Flow<Resource<Int>>

        suspend fun fetchSeatLimit() : Resource<Int>

        suspend fun saveSeatReserved(seatNumber: Int, nameUser: String, idNumberUser: String)

        suspend fun addIterator(seatNumber: Int)

        suspend fun fetchRegisteredSeatsByUserName(): Resource<List<Seat>>?

    }

    interface Intentions {

        suspend fun saveIntention(category: String, intention: String)

        suspend fun fetchSavedIntentionsByNameUser(): Resource<List<Intention>>?

    }

}