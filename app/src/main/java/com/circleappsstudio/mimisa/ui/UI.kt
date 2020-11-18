package com.circleappsstudio.mimisa.ui

interface UI {

    interface SignInUI {
        /*
            Interface encargada de controlar los métodos del fragment de registro de usuarios.
        */

        fun showMessage(message: String, duration: Int)
        fun showProgressBar()
        fun hideProgressBar()
        fun signInUserUI()
        fun goToLogin()
        fun goToResetPassword()
        fun goToMainActivity()

    }

    interface LogInUI {
        /*
            Interface encargada de controlar los métodos del fragment de loggeo de usuarios.
        */

        fun showMessage(message: String, duration: Int)
        fun showProgressBar()
        fun hideProgressBar()
        fun logInUserUI()
        fun goToMainActivity()

    }

    interface ResetPassword {
        /*
            Interface encargada de controlar los métodos del fragment de cambio de contraseña de usuarios.
        */

        fun showMessage(message: String, duration: Int)
        fun showProgressBar()
        fun hideProgressBar()
        fun resetPasswordUserUI()

    }

    interface SplashScreen {
        /*
            Interface encargada de controlar los métodos del SplashScreen.
        */

        fun checkUserLogged()
        fun goToSignIn()
        fun goToMainActivity()

    }

}