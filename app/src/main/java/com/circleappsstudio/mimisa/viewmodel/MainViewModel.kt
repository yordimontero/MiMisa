package com.circleappsstudio.mimisa.viewmodel

interface MainViewModel {

    interface LogInUser {
        suspend fun signInUserViewModel(email: String, password: String)
        suspend fun updateUserProfileViewModel(fullName: String)
        fun checkEmptyFieldsForSignInViewModel(fullName: String, email: String, password1: String, password2: String) : Boolean
        fun checkMatchPasswordsForSignInViewModel(password1: String, password2: String) : Boolean

        suspend fun logInUserViewModel(email: String, password: String)
        fun checkEmptyFieldsForLogInViewModel(email: String, password: String) : Boolean

        suspend fun resetPasswordUserViewModel(email: String)
        fun checkEmptyFieldsForResetPasswordViewModel(email: String) : Boolean

        fun checkValidEmailViewModel(email: String) : Boolean
        fun checkValidPasswordViewModel(password: String) : Boolean

        fun logOutUserViewModel()

    }

}