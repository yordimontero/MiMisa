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

    /*interface SeatReservation {

        suspend fun fetchIterator() : Resource<Int?>

    }*/

    interface SeatReservation {
        suspend fun fetchIterator() : Resource<Int?>
    }

}