package com.circleappsstudio.mimisa.ui.viewmodel

import androidx.lifecycle.LiveData
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

    }

    interface SeatReservation {
        fun fetchIterator(): LiveData<Resource<Int>>
        fun saveSeatReserved(seatNumber: Int, nameUser: String, idNumberUser: String): LiveData<Resource<Boolean>>
        fun addIterator(seatNumber: Int): LiveData<Resource<Boolean>>
        fun checkEmptyFieldsForSeatReservation(nameUser: String, idNumberUser: String): Boolean
        fun checkValidIdNumberUser(idNumberUser: String): Boolean
    }

}