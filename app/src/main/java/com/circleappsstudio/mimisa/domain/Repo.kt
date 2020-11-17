package com.circleappsstudio.mimisa.domain

interface Repo {

    interface SignInUser {
        suspend fun signInUserRepo(email: String, password: String)
    }

    interface LogInUser {
        suspend fun logInUserRepo(email: String, password: String)
    }

    interface ResetPasswordUser {
        suspend fun resetPasswordUserRepo(email: String)
    }

}