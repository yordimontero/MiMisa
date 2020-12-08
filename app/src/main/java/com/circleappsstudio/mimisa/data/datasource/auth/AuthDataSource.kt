// DataSource encargado de interactuar con la autenticación de Firebase.

package com.circleappsstudio.mimisa.data.datasource.auth

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthDataSource : DataSource.Auth {

    private var currentUser: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun signInUser(email: String, password: String) {
        /*
             Método encargado de registrar un usuario nuevo en el sistema.
        */
        currentUser.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun updateUserProfile(fullName: String) {
        /*
             Método encargado de setear el nombre de un usuario nuevo en el sistema.
        */
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(fullName)
            .build()

        currentUser.currentUser?.updateProfile(profileUpdates)?.await()
    }

    override suspend fun logInUser(email: String, password: String) {
        /*
             Método encargado de loggear un usuario existente en el sistema.
        */
        currentUser.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun resetPasswordUser(email: String) {
        /*
             Método encargado de mandar un correo de cambio de contraseña a un
             usuario existente en el sistema.
        */
        currentUser.sendPasswordResetEmail(email).await()
    }

    override fun logOutUser() {
        /*
             Método encargado de cerrar la sesión de un usuario existente en el sistema.
        */
        currentUser.signOut()
    }

    /*
        Método encargado de obtener el nombre del actual usuario autenticado.
    */
    override fun getNameUser(): String = currentUser.currentUser!!.displayName.toString()

}