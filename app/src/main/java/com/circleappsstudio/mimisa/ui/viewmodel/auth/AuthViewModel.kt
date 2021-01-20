package com.circleappsstudio.mimisa.ui.viewmodel.auth

import android.content.Intent
import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.ui.viewmodel.MainViewModel
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.FirebaseException
import kotlinx.coroutines.Dispatchers

class AuthViewModel(
        private val authRepository: Repository.Auth
) : ViewModel(), MainViewModel.Auth {

    //private val currentUser by lazy { FirebaseAuth.getInstance().currentUser }

    override fun signInUser(
        email: String,
        password: String
    ): LiveData<Resource<Boolean>> =
        /*
             Método encargado de registrar un usuario nuevo en el sistema.
        */
        liveData(Dispatchers.IO) {

            emit(Resource.Loading())

            try {

                authRepository.signInUser(email, password)
                emit(Resource.Success(true))

            } catch (e: FirebaseException){
                emit(Resource.Failure(e))
            }

        }


    override fun updateUserProfile(
        fullName: String
    ): LiveData<Resource<Boolean>> =
        /*
             Método encargado de setear el nombre de un usuario nuevo en el sistema.
        */
        liveData(Dispatchers.IO) {

            emit(Resource.Loading())

            try {

                authRepository.updateUserProfile(fullName)
                emit(Resource.Success(true))

            } catch (e: FirebaseException){
                emit(Resource.Failure(e))
            }

        }

    /*
        Método encargado de validar que el nombre no sea vacío.
    */
    override fun checkEmptyNameUser(nameUser: String): Boolean = nameUser.isEmpty()

    /*
        Método encargado de validar que el e-mail no sea vacío.
    */
    override fun checkEmptyEmailUser(email: String)
            : Boolean = email.isEmpty()

    /*
        Método encargado de validar que la contraseña 1 no sea vacía.
    */
    override fun checkEmptyPassword1User(password1: String)
            : Boolean = password1.isEmpty()

    /*
        Método encargado de validar que la contraseña 2 no sea vacía.
    */
    override fun checkEmptyPassword2User(password2: String)
            : Boolean = password2.isEmpty()

    /*
        Método encargado de validar que las contraseñas ingresadas sean iguales.
    */
    override fun checkMatchPasswordsForSignIn(password1: String, password2: String)
            : Boolean = password1 != password2

    override fun logInUser(
        email: String, password: String
    ): LiveData<Resource<Boolean>> =
        /*
             Método encargado de loggear un usuario existente en el sistema.
        */
        liveData(Dispatchers.IO) {

            emit(Resource.Loading())

            try {

                authRepository.logInUser(email, password)
                emit(Resource.Success(true))

            } catch (e: FirebaseException){
                emit(Resource.Failure(e))
            }

        }

    /*
        Método encargado de validar que exista actualmente un usuario loggeado en el sistema.
    */
    //override fun checkUserLogged(): Boolean = currentUser == null

    /*
        Método encargado de validar que exista actualmente un usuario loggeado en el sistema.
    */
    override fun checkUserLogged(): Boolean = authRepository.getInstanceUser() == null

    override fun resetPasswordUser(email: String): LiveData<Resource<Boolean>> =
        /*
             Método encargado de mandar un correo de cambio de contraseña a un
             usuario existente en el sistema.
        */
        liveData(Dispatchers.IO) {

            emit(Resource.Loading())

            try {

                authRepository.resetPasswordUser(email)
                emit(Resource.Success(true))

            } catch (e: FirebaseException){
                emit(Resource.Failure(e))
            }

        }

    /*
        Método encargado de validar que el correo ingresado sea válido.
    */
    override fun checkValidEmail(email: String)
            : Boolean = !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()

    /*
        Método encargado de validar que la contraseña ingresada sea válida.
    */
    override fun checkValidPassword(password: String)
            : Boolean = password.length <= 5

    /*
        Método encargado de cerrar la sesión de un usuario existente en el sistema.
    */
    override fun logOutUser() = authRepository.logOutUser()

    /*
        Método encargado de obtener el nombre del actual usuario autenticado.
    */
    override fun getUserName(): String = authRepository.getNameUser()

    override fun getEmailUser(): String = authRepository.getEmailUser()

    /*
            Método encargado de crear el Intent para la autenticación de Google.
        */
    override fun intentForGoogleAuth(): Intent = authRepository.intentForGoogleAuth()

    /*
        Método encargado de obtener el ResultCode para la autenticación de Google.
    */
    override fun getResultCode(): Int = authRepository.getResultCode()

}