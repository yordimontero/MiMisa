package com.circleappsstudio.mimisa.data

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
        suspend fun fetchIterator() : Flow<Resource<Int>>
        suspend fun saveSeatReserved(seatNumber: Int, nameUser: String, idNumberUser: String)
        suspend fun addIterator(seatNumber: Int)
    }

}