package com.circleappsstudio.mimisa.ui

interface UI {

    interface SignInUI {
        fun showMessage(message: String)
        fun showProgressBar()
        fun hideProgressBar()
        fun signInUserUI()
    }

    interface LogInUI {

    }

    interface ResetPassword {

    }

}