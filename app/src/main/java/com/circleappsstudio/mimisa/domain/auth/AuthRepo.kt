// Repo encargado de interactuar con el DataSource "AuthDataSource".

package com.circleappsstudio.mimisa.domain.auth

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.domain.Repo

class AuthRepo(private val authDataSource: DataSource.Auth) : Repo.Auth {

    override suspend fun signInUserRepo(email: String, password: String) {
        /*
             Método encargado de registrar un usuario nuevo en el sistema.
        */
        authDataSource.signInUserDataSource(email, password)
    }

    override suspend fun updateUserProfileRepo(fullName: String) {
        /*
             Método encargado de setear el nombre de un usuario nuevo en el sistema.
        */
        authDataSource.updateUserProfileDataSource(fullName)
    }

    override suspend fun logInUserRepo(email: String, password: String) {
        /*
             Método encargado de loggear un usuario existente en el sistema.
        */
        authDataSource.logInUserDataSource(email, password)
    }

    override suspend fun resetPasswordUserRepo(email: String) {
        /*
             Método encargado de mandar un correo de cambio de contraseña a un
             usuario existente en el sistema.
        */
        authDataSource.resetPasswordUserDataSource(email)
    }

    override fun logOutUserRepo() {
        /*
             Método encargado de cerrar la sesión de un usuario existente en el sistema.
        */
        authDataSource.logOutUser()
    }

}