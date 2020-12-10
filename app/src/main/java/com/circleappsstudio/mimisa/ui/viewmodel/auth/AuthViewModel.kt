// ViewModel encargado de interactuar con el Repo "AuthRepo".

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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers

class AuthViewModel(private val authRepository: Repository.Auth) : ViewModel(), MainViewModel.Auth {

    private val currentUser by lazy { FirebaseAuth.getInstance().currentUser }

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

                authRepository.signInUserRepo(email, password)
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

                authRepository.updateUserProfileRepo(fullName)
                emit(Resource.Success(true))

            } catch (e: FirebaseException){
                emit(Resource.Failure(e))
            }

        }

    /*
        Método encargado de validar que los campos de texto en la pantalla
        de registrar usuario no sean vacíos.
    */
    override fun checkEmptyFieldsForSignIn(
        fullName: String,
        email: String,
        password1: String,
        password2: String
    ): Boolean = email.isEmpty() && password1.isEmpty() && password2.isEmpty()

    /*
        Método encargado de validar que el nombre no sea vacío.
    */
    override fun checkEmptyNameUser(nameUser: String): Boolean = nameUser.isEmpty()

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

                authRepository.logInUserRepo(email, password)
                emit(Resource.Success(true))

            } catch (e: FirebaseException){
                emit(Resource.Failure(e))
            }

        }

    /*
        Método encargado de validar que los campos de texto en la pantalla de
        loggear usuario no sean vacíos.
    */
    override fun checkEmptyFieldsForLogIn(email: String, password: String)
            : Boolean = email.isEmpty() && password.isEmpty()

    /*
        Método encargado de validar que exista actualmente un usuario loggeado en el sistema.
    */
    override fun checkUserLogged(): Boolean = currentUser == null

    override fun resetPasswordUser(email: String): LiveData<Resource<Boolean>> =
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

    /*
        Método encargado de validar que los campos de texto en la pantalla de
        cambiar contraseña del usuario no sean vacíos.
    */
    override fun checkEmptyFieldsForResetPassword(email: String)
            : Boolean = email.isEmpty()

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
    override fun logOutUser() = authRepository.logOutUserRepo()

    /*
        Método encargado de obtener el nombre del actual usuario autenticado.
    */
    override fun getUserName(): String = authRepository.getUserName()

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