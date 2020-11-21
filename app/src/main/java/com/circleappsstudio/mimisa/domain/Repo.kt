package com.circleappsstudio.mimisa.domain

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
        suspend fun fetchIterator() : Resource<Int?>
    }

}