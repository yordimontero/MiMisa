package com.circleappsstudio.mimisa.ui.viewmodel

import android.content.Context
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
        fun signInUserViewModel(email: String, password: String): LiveData<Resource<Boolean>>
        fun updateUserProfileViewModel(fullName: String): LiveData<Resource<Boolean>>

        // Validación Registro de Usuarios:
        fun checkEmptyFieldsForSignInViewModel(
            fullName: String,
            email: String,
            password1: String,
            password2: String
        ): Boolean
        fun checkMatchPasswordsForSignInViewModel(password1: String, password2: String): Boolean

        // Loggeo de Usuarios:
        fun logInUserViewModel(email: String, password: String): LiveData<Resource<Boolean>>

        // Validaciones Loggeo de Usuarios:
        fun checkEmptyFieldsForLogInViewModel(email: String, password: String): Boolean

        fun chechEmptyUserName(nameUser: String): Boolean

        // Cambio de Contraseña de Usuarios.
        fun resetPasswordUserViewModel(email: String): LiveData<Resource<Boolean>>

        // Validaciones Cambio de Contraseña de Usuarios:
        fun checkEmptyFieldsForResetPasswordViewModel(email: String): Boolean

        // Validaciones Generales:
        fun checkUserLogged(): Boolean
        fun checkValidEmailViewModel(email: String): Boolean
        fun checkValidPasswordViewModel(password: String): Boolean

        // Cierra de Sesión de Usuarios:
        fun logOutUserViewModel()

        fun getUserName(): String

    }

    interface SeatReservation {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore.
        */
        fun fetchIterator(): LiveData<Resource<Int>>

        fun fetchSeatLimit(): LiveData<Resource<Int>>

        fun saveSeatReserved(seatNumber: Int, nameUser: String, idNumberUser: String): LiveData<Resource<Boolean>>

        fun addIterator(seatNumber: Int): LiveData<Resource<Boolean>>

        fun fetchRegisteredSeatsByUserName(): LiveData<Resource<List<Seat>>?>

        fun checkEmptyNameUser(nameUser: String): Boolean

        fun checkEmptyIdNumberUser(idNumberUser: String): Boolean

        fun checkValidIdNumberUser(idNumberUser: String): Boolean

        fun checkSeatLimit(seatNumber: Int, seatLimit: Int): Boolean

        fun checkSeatSavedByIdNumberUser(idNumberUser: String): LiveData<Resource<Boolean>>

    }

    interface Intentions {

        fun saveIntention(category: String, intention: String): LiveData<Resource<Boolean>>

        fun checkEmptyIntentionCategory(category: String): Boolean

        fun checkEmptyIntention(intention: String): Boolean

        fun fetchSavedIntentionsByNameUser(): LiveData<Resource<List<Intention>>?>

    }

    interface More {

        fun fetchUserName(context: Context)

        fun goToFacebook(context: Context)

        fun goToTwitter(context: Context)

        fun goToWebPage(context: Context)

        fun goToPrivacyPolicy(context: Context)

        fun rateApp(context: Context)

        fun goToPlayStoreMoreApps(context: Context)

    }

}