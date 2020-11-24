// DataSource encargado de interactuar con la autenticación de Firebase.

package com.circleappsstudio.mimisa.data.datasource.auth

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthDataSource : DataSource.Auth {

    private var user: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun signInUserDataSource(email: String, password: String) {
        /*
             Método encargado de registrar un usuario nuevo en el sistema.
        */
        user.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun updateUserProfileDataSource(fullName: String) {
        /*
             Método encargado de setear el nombre de un usuario nuevo en el sistema.
        */
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(fullName)
            .build()

        user.currentUser?.updateProfile(profileUpdates)?.await()
    }

    override suspend fun logInUserDataSource(email: String, password: String) {
        /*
             Método encargado de loggear un usuario existente en el sistema.
        */
        user.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun resetPasswordUserDataSource(email: String) {
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

}