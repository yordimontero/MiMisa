package com.circleappsstudio.mimisa.data

interface DataSource {

    interface Auth {
        /*
            Interface encargada de controlar los métodos de autenticación de Firebase.
        */
        suspend fun signInUserDataSource(email: String, password: String)

        suspend fun updateUserProfileDataSource(fullName: String)

        suspend fun logInUserDataSource(email: String, password: String)

        suspend fun resetPasswordUserDataSource(email: String)

        fun logOutUser()

    }

}