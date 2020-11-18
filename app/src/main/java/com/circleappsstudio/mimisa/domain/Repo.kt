package com.circleappsstudio.mimisa.domain

interface Repo {

    interface Auth {
        /*
            Interface encargada de controlar los métodos de autenticación de Firebase.
        */

        suspend fun signInUserRepo(email: String, password: String)

        suspend fun updateUserProfileRepo(fullName: String)

        suspend fun logInUserRepo(email: String, password: String)

        suspend fun resetPasswordUserRepo(email: String)

        fun logOutUserRepo()

    }

}