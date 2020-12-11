package com.circleappsstudio.mimisa.domain

import android.app.Activity
import android.content.Intent
import com.circleappsstudio.mimisa.data.model.Intention
import com.circleappsstudio.mimisa.data.model.Seat
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {

    interface Auth {
        /*
            Interface encargada de controlar los métodos de autenticación de Firebase.
        */
        suspend fun signInUserRepo(email: String, password: String)

        suspend fun updateUserProfileRepo(fullName: String)

        suspend fun logInUserRepo(email: String, password: String)

        suspend fun resetPasswordUserRepo(email: String)

        fun logOutUserRepo()

        fun getUserName(): String

        fun getEmailUser(): String

        fun intentForGoogleAuth(): Intent

        fun getResultCode(): Int

    }

    interface RoleUser {
        /*
            Interface encargada de controlar los métodos de creación de roles.
        */
        suspend fun fetchAdminCode(): Resource<String?>

        suspend fun checkCreatedAdminByEmailUser(emailUser: String): Resource<Boolean>

        suspend fun createAdmin(emailUser: String, nameUser: String)

        suspend fun deleteAdmin(emailUser: String)

    }

    interface SeatReservation {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para
            la reservación de asientos.
        */
        suspend fun fetchIterator() : Flow<Resource<Int>>

        suspend fun fetchSeatLimit(): Resource<Int>

        suspend fun saveSeatReserved(seatNumber: Int, nameUser: String, idNumberUser: String)

        suspend fun addIterator(seatNumber: Int)

        suspend fun fetchRegisteredSeatsByNameUser(): Resource<List<Seat>>?

        suspend fun fetchAllRegisteredSeats(): Resource<List<Seat>>?

        suspend fun checkSeatSavedByIdNumberUser(idNumberUser: String): Resource<Boolean>

    }

    interface Intentions {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para el
            registro de intenciones.
        */
        suspend fun saveIntention(category: String, intention: String)

        suspend fun fetchSavedIntentionsByNameUser(): Resource<List<Intention>>?

        suspend fun fetchAllSavedIntentions(): Resource<List<Intention>>?

    }

}