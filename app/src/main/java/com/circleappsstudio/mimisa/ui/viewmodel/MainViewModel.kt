package com.circleappsstudio.mimisa.ui.viewmodel

import androidx.lifecycle.LiveData
import com.circleappsstudio.mimisa.vo.Resource

interface MainViewModel {

    interface Auth {
        /*
            Interface encargada de controlar los métodos de autenticación de Firebase.
        */

        // Registro de Usuarios:
        suspend fun signInUserViewModel(email: String, password: String)
        suspend fun updateUserProfileViewModel(fullName: String)

        // Validación Registro de Usuarios:
        fun checkEmptyFieldsForSignInViewModel(
            fullName: String,
            email: String,
            password1: String,
            password2: String
        ): Boolean
        fun checkMatchPasswordsForSignInViewModel(password1: String, password2: String): Boolean

        // Loggeo de Usuarios:
        suspend fun logInUserViewModel(email: String, password: String)

        // Validaciones Loggeo de Usuarios:
        fun checkEmptyFieldsForLogInViewModel(email: String, password: String): Boolean

        // Cambio de Contraseña de Usuarios.
        suspend fun resetPasswordUserViewModel(email: String)

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
    }

}