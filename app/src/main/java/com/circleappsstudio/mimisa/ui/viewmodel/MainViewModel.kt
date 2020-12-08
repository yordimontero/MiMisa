package com.circleappsstudio.mimisa.ui.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import com.circleappsstudio.mimisa.data.model.Intention
import com.circleappsstudio.mimisa.data.model.Seat
import com.circleappsstudio.mimisa.vo.Resource

interface MainViewModel {

    interface Auth {
        /*
            Interface encargada de controlar los métodos de autenticación de Firebase.
        */
        // Registro de Usuarios:
        fun signInUser(email: String, password: String): LiveData<Resource<Boolean>>
        fun updateUserProfile(fullName: String): LiveData<Resource<Boolean>>

        // Validación Registro de Usuarios:
        fun checkEmptyFieldsForSignIn(
            fullName: String,
            email: String,
            password1: String,
            password2: String
        ): Boolean
        fun checkMatchPasswordsForSignIn(password1: String, password2: String): Boolean

        // Loggeo de Usuarios:
        fun logInUser(email: String, password: String): LiveData<Resource<Boolean>>

        // Validaciones Loggeo de Usuarios:
        fun checkEmptyFieldsForLogIn(email: String, password: String): Boolean

        fun checkEmptyNameUser(nameUser: String): Boolean

        // Cambio de Contraseña de Usuarios.
        fun resetPasswordUser(email: String): LiveData<Resource<Boolean>>

        // Validaciones Cambio de Contraseña de Usuarios:
        fun checkEmptyFieldsForResetPassword(email: String): Boolean

        // Validaciones Generales:
        fun checkUserLogged(): Boolean
        fun checkValidEmail(email: String): Boolean
        fun checkValidPassword(password: String): Boolean

        // Cierra de Sesión de Usuarios:
        fun logOutUser()

        fun getUserName(): String

        fun intentForGoogleAuth(): Intent

        fun getResultCode(): Int

    }

    interface SeatReservation {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para
            la reservación de asientos.
        */
        fun fetchIterator(): LiveData<Resource<Int>>

        fun fetchSeatLimit(): LiveData<Resource<Int>>

        fun saveSeatReserved(seatNumber: Int, nameUser: String, idNumberUser: String): LiveData<Resource<Boolean>>

        fun addIterator(seatNumber: Int): LiveData<Resource<Boolean>>

        fun fetchRegisteredSeatsByNameUser(): LiveData<Resource<List<Seat>>?>

        fun checkEmptyNameUser(nameUser: String): Boolean

        fun checkEmptyIdNumberUser(idNumberUser: String): Boolean

        fun checkValidIdNumberUser(idNumberUser: String): Boolean

        fun checkSeatLimit(seatNumber: Int, seatLimit: Int): Boolean

        fun checkSeatSavedByIdNumberUser(idNumberUser: String): LiveData<Resource<Boolean>>

    }

    interface Intentions {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para el
            registro de intenciones.
        */
        fun saveIntention(category: String, intention: String): LiveData<Resource<Boolean>>

        fun checkValidIntentionCategory(category: String): Boolean

        fun checkEmptyIntention(intention: String): Boolean

        fun fetchSavedIntentionsByNameUser(): LiveData<Resource<List<Intention>>?>

    }

}