// Repositorio encargado de interactuar con el DataSource "AuthDataSource".

package com.circleappsstudio.mimisa.domain.auth

import android.content.Intent
import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.domain.Repository

class AuthRepository(private val authDataSource: DataSource.Auth) : Repository.Auth {

    /*
        Método encargado de registrar un usuario nuevo en el sistema.
    */
    override suspend fun signInUserRepo(email: String, password: String)
            = authDataSource.signInUser(email, password)

    /*
        Método encargado de setear el nombre de un usuario nuevo en el sistema.
    */
    override suspend fun updateUserProfileRepo(fullName: String)
            = authDataSource.updateUserProfile(fullName)


    /*
        Método encargado de loggear un usuario existente en el sistema.
    */
    override suspend fun logInUserRepo(email: String, password: String)
            = authDataSource.logInUser(email, password)


    /*
        Método encargado de mandar un correo de cambio de contraseña a un
        usuario existente en el sistema.
    */
    override suspend fun resetPasswordUserRepo(email: String)
            = authDataSource.resetPasswordUser(email)


    /*
        Método encargado de cerrar la sesión de un usuario existente en el sistema.
    */
    override fun logOutUserRepo() = authDataSource.logOutUser()

    /*
        Método encargado de obtener el nombre del actual usuario autenticado.
    */
    override fun getUserName(): String = authDataSource.getNameUser()

    /*
        Método encargado de crear el Intent para la autenticación de Google.
    */
    override fun intentForGoogleAuth(): Intent = authDataSource.intentForGoogleAuth()

    /*
        Método encargado de obtener el ResultCode para la autenticación de Google.
    */
    override fun getResultCode(): Int = authDataSource.getResultCode()

}