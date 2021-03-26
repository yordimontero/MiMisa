package com.circleappsstudio.mimisa.data.datasource

import android.content.Intent
import androidx.lifecycle.LiveData
import com.circleappsstudio.mimisa.data.model.Intention
import com.circleappsstudio.mimisa.data.model.Seat
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface DataSource {

    interface Auth {
        /*
            Interface encargada de controlar los métodos de autenticación de Firebase.
        */
        suspend fun signInUser(email: String, password: String)

        suspend fun updateUserProfile(fullName: String)

        suspend fun logInUser(email: String, password: String)

        suspend fun resetPasswordUser(email: String)

        fun logOutUser()

        fun getInstanceUser(): FirebaseUser?

        fun getNameUser(): String

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

        suspend fun saveSeatReserved(
            seatCategory: String,
            seatNumber: String,
            nameUser: String,
            lastNameUser: String,
            idNumberUser: String
        ): Resource<Boolean>

        suspend fun fetchAllRegisteredSeats(): Resource<List<Seat>>?

        suspend fun fetchRegisteredSeatsByNameUser(): Resource<List<Seat>>?

        suspend fun fetchRegisteredSeatByRegisteredPerson(registeredPerson: String): Resource<List<Seat>>?

        suspend fun fetchRegisteredSeatBySeatNumber(seatNumber: String): Resource<List<Seat>>?

        suspend fun checkSeatSavedByIdNumberUser(idNumberUser: String): Resource<Boolean>

        suspend fun checkIfIsCoupleAvailable(coupleNumber: String): Resource<Boolean>

        suspend fun updateIsCoupleAvailable(coupleNumber: String, isAvailable: Boolean)

        suspend fun loadAvailableCouples(): Flow<Resource<String>>

        suspend fun loadNoAvailableCouples(): Flow<Resource<String>>

        suspend fun checkIfIsThreesomeAvailable(threesomeNumber: String): Resource<Boolean>

        suspend fun updateIsThreesomeAvailable(threesomeNumber: String, isAvailable: Boolean)

        suspend fun loadAvailableThreesomes(): Flow<Resource<String>>

        suspend fun loadNoAvailableThreesomes(): Flow<Resource<String>>

        suspend fun checkIfIsBubbleAvailable(bubbleNumber: String): Resource<Boolean>

        suspend fun updateIsBubbleAvailable(bubbleNumber: String, isAvailable: Boolean)

        suspend fun loadAvailableBubbles(): Flow<Resource<String>>

        suspend fun loadNoAvailableBubbles(): Flow<Resource<String>>

    }

    interface Intentions {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para el
            registro de intenciones.
        */
        suspend fun saveIntention(category: String, intention: String)

        suspend fun fetchAllSavedIntentions(): Resource<List<Intention>>?

        suspend fun fetchSavedIntentionsByNameUser(): Resource<List<Intention>>?

        suspend fun fetchSavedIntentionsByCategory(category: String): Resource<List<Intention>>?

    }

    interface Params {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para
            la lectura y escritura de los parámetros generales.
        */
        suspend fun fetchIsSeatReservationAvailable(): Flow<Resource<Boolean>>

        suspend fun setIsSeatReservationAvailable(isSeatReservationAvailable: Boolean)

        suspend fun fetchVersionCode(): Flow<Resource<Int>>

        suspend fun fetchIsRegisterIntentionAvailable(): Flow<Resource<Boolean>>

        suspend fun setIsRegisterIntentionAvailable(isRegisterIntentionAvailable: Boolean)

    }

}