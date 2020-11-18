// ViewModel encargado de interactuar con el Repo "AuthRepo".

package com.circleappsstudio.mimisa.ui.viewmodel.auth

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import com.circleappsstudio.mimisa.domain.Repo
import com.circleappsstudio.mimisa.ui.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel(private val authRepo: Repo.Auth) : ViewModel(), MainViewModel.Auth {

    override suspend fun signInUserViewModel(email: String, password: String) {
        /*
             Método encargado de registrar un usuario nuevo en el sistema.
        */
        authRepo.signInUserRepo(email, password)
    }

    override suspend fun updateUserProfileViewModel(fullName: String) {
        /*
             Método encargado de setear el nombre de un usuario nuevo en el sistema.
        */
        authRepo.updateUserProfileRepo(fullName)
    }

    override fun checkEmptyFieldsForSignInViewModel(
        fullName: String,
        email: String,
        password1: String,
        password2: String
    ): Boolean {
        /*
             Método encargado de validar que los campos de texto en la pantalla
             de registrar usuario no sean vacíos.
        */
        return email.isEmpty() && password1.isEmpty() && password2.isEmpty()
    }

    override fun checkMatchPasswordsForSignInViewModel(password1: String, password2: String): Boolean {
        /*
             Método encargado de validar que las contraseñas ingresadas sean iguales.
        */
        return password1 != password2
    }

    override suspend fun logInUserViewModel(email: String, password: String) {
        /*
             Método encargado de loggear un usuario existente en el sistema.
        */
        authRepo.logInUserRepo(email, password)
    }

    override fun checkEmptyFieldsForLogInViewModel(email: String, password: String): Boolean {
        /*
             Método encargado de validar que los campos de texto en la pantalla de
             loggear usuario no sean vacíos.
        */
        return email.isEmpty() && password.isEmpty()
    }

    override fun checkUserLogged() : Boolean {
        /*
             Método encargado de validar que exista actualmente un usuario loggeado en el sistema.
        */
        val user = FirebaseAuth.getInstance().currentUser
        return user == null
    }

    override suspend fun resetPasswordUserViewModel(email: String) {
        /*
             Método encargado de mandar un correo de cambio de contraseña a un
             usuario existente en el sistema.
        */
        authRepo.resetPasswordUserRepo(email)
    }

    override fun checkEmptyFieldsForResetPasswordViewModel(email: String) : Boolean {
        /*
             Método encargado de validar que los campos de texto en la pantalla de
             cambiar contraseña del usuario no sean vacíos.
        */
        return email.isEmpty()
    }

    override fun checkValidEmailViewModel(email: String): Boolean {
        /*
             Método encargado de validar que el correo ingresado sea válido.
        */
        return !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun checkValidPasswordViewModel(password: String): Boolean {
        /*
             Método encargado de validar que la contraseña ingresada sea válida.
        */
        return password.length <= 5
    }

    override fun logOutUserViewModel() {
        /*
             Método encargado de cerrar la sesión de un usuario existente en el sistema.
        */
        authRepo.logOutUserRepo()
    }

}