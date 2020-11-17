package com.circleappsstudio.mimisa.viewmodel

import android.content.Context

interface ViewModel {

    interface SignInUser {
        fun signInUserViewModel(context: Context, email: String, password: String)
    }

    interface LogInUser {
        fun logInUserViewModel(email: String, password: String)
    }

    interface ResetPasswordUser {
        fun resetPasswordUserViewModel(email: String)
    }

}