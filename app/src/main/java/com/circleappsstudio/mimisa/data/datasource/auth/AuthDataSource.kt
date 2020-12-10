// DataSource encargado de interactuar con la autenticación de Firebase.

package com.circleappsstudio.mimisa.data.datasource.auth

import android.content.Intent
import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthDataSource : DataSource.Auth {

    companion object{
        //Constante para hacer a verificación con el RequestCode lanzado con el onActivityResult.
        private const val RC_SIGN_IN = 423
    }

    private var user: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun signInUser(email: String, password: String) {
        /*
             Método encargado de registrar un usuario nuevo en el sistema.
        */
        user.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun updateUserProfile(fullName: String) {
        /*
             Método encargado de setear el nombre de un usuario nuevo en el sistema.
        */
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(fullName)
            .build()

        user.currentUser?.updateProfile(profileUpdates)?.await()
    }

    override suspend fun logInUser(email: String, password: String) {
        /*
             Método encargado de loggear un usuario existente en el sistema.
        */
        user.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun resetPasswordUser(email: String) {
        /*
             Método encargado de mandar un correo de cambio de contraseña a un
             usuario existente en el sistema.
        */
        user.sendPasswordResetEmail(email).await()
    }

    override fun logOutUser() {
        /*
             Método encargado de cerrar la sesión de un usuario existente en el sistema.
        */
        user.signOut()
    }

    /*
        Método encargado de obtener el nombre del actual usuario autenticado.
    */
    override fun getNameUser(): String = user.currentUser?.displayName.toString()

    override fun getEmailUser(): String = user.currentUser?.email.toString()

    override fun intentForGoogleAuth(): Intent {
        /*
            Método encargado de crear el Intent para la autenticación de Google.
        */
        val providers = arrayListOf( AuthUI.IdpConfig.GoogleBuilder().build() )

        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(true)
            .build()

    }

    /*
        Método encargado de obtener el ResultCode para la autenticación de Google.
    */
    override fun getResultCode(): Int = RC_SIGN_IN

}