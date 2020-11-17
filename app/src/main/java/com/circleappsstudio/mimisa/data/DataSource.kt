package com.circleappsstudio.mimisa.data

interface DataSource {

    interface SignInUser {
        suspend fun signInUserDataSource(email: String, password: String)
    }

    interface LogInUser {
        suspend fun logInUserDataSource(email: String, password: String)
    }

    interface ResetPasswordUser {
        suspend fun resetPasswordUserDataSource(email: String)
    }

}