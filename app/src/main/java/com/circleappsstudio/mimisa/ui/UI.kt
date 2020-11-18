package com.circleappsstudio.mimisa.ui

interface UI {

    interface SignInUI {
        fun showMessage(message: String, duration: Int)
        fun showProgressBar()
        fun hideProgressBar()
        fun signInUserUI()
        fun goToLogin()
        fun goToResetPassword()
    }

    interface LogInUI {
        fun showMessage(message: String, duration: Int)
        fun showProgressBar()
        fun hideProgressBar()
        fun logInUserUI()
    }

    interface ResetPassword {
        fun showMessage(message: String, duration: Int)
        fun showProgressBar()
        fun hideProgressBar()
        fun resetPasswordUserUI()
    }

}