package com.circleappsstudio.mimisa.data

interface DataSource {

    interface LogInUser {
        suspend fun signInUserDataSource(email: String, password: String)
        suspend fun updateUserProfileDataSource(fullName: String)
        suspend fun logInUserDataSource(email: String, password: String)
        suspend fun resetPasswordUserDataSource(email: String)
        fun logOutUser()
    }

}