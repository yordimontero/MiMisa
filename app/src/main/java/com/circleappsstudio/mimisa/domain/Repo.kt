package com.circleappsstudio.mimisa.domain

interface Repo {

    interface LogInUser {
        suspend fun signInUserRepo(email: String, password: String)
        suspend fun updateUserProfileRepo(fullName: String)
        suspend fun logInUserRepo(email: String, password: String)
        suspend fun resetPasswordUserRepo(email: String)
        fun logOutUserRepo()
    }

}