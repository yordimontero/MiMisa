// ViewModel encargado de interactuar con el Repo "AuthRepo".

package com.circleappsstudio.mimisa.ui.viewmodel.auth

import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.ui.viewmodel.MainViewModel
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers

class AuthViewModel(private val authRepository: Repository.Auth) : ViewModel(), MainViewModel.Auth {

    private val user by lazy { FirebaseAuth.getInstance() }

    override fun signInUserViewModel(
        email: String,
        password: String
    ): LiveData<Resource<Boolean>> =
        /*
             Método encargado de registrar un usuario nuevo en el sistema.
        */
        liveData(Dispatchers.IO) {

            emit(Resource.Loading())

            try {

                authRepository.signInUserRepo(email, password)

                emit(Resource.Success(true))

            } catch (e: FirebaseException){

                emit(Resource.Failure(e))

            }

        }


    override fun updateUserProfileViewModel(
        fullName: String
    ): LiveData<Resource<Boolean>> =
        /*
             Método encargado de setear el nombre de un usuario nuevo en el sistema.
        */
        liveData(Dispatchers.IO) {

            emit(Resource.Loading())

            try {

                authRepository.updateUserProfileRepo(fullName)

                emit(Resource.Success(true))

            } catch (e: FirebaseException){

                emit(Resource.Failure(e))

            }

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

    override fun logInUserViewModel(
        email: String, password: String
    ): LiveData<Resource<Boolean>> =
        /*
             Método encargado de loggear un usuario existente en el sistema.
        */
        liveData(Dispatchers.IO) {

            emit(Resource.Loading())

            try {

                authRepository.logInUserRepo(email, password)

                emit(Resource.Success(true))

            } catch (e: FirebaseException){

                emit(Resource.Failure(e))

            }

        }


    override fun checkEmptyFieldsForLogInViewModel(email: String, password: String): Boolean {
        /*
             Método encargado de validar que los campos de texto en la pantalla de
             loggear usuario no sean vacíos.
        */
        return email.isEmpty() && password.isEmpty()
    }

    override fun checkUserLogged(): Boolean {
        /*
             Método encargado de validar que exista actualmente un usuario loggeado en el sistema.
        */
        return user.currentUser == null
    }

    override fun resetPasswordUserViewModel(email: String): LiveData<Resource<Boolean>> =
        /*
             Método encargado de mandar un correo de cambio de contraseña a un
             usuario existente en el sistema.
        */
        liveData(Dispatchers.IO) {

            emit(Resource.Loading())

            try {

                authRepository.resetPasswordUserRepo(email)

                emit(Resource.Success(true))

            } catch (e: FirebaseException){

                emit(Resource.Failure(e))

            }

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
        authRepository.logOutUserRepo()
    }

}