package com.circleappsstudio.mimisa.ui

interface UI {

    interface SplashScreen {
        /*
            Interface encargada de controlar los métodos del SplashScreen.
        */

        fun checkUserLogged()
        fun goToSignIn()
        fun goToMainActivity()

    }

    interface SignInUI {
        /*
            Interface encargada de controlar los métodos del fragment de registro de usuarios.
        */

        fun showMessage(message: String, duration: Int)
        fun showProgressBar()
        fun hideProgressBar()
        fun signInUserUI()
        fun signInUserObserver()
        fun setNameUserObserver()
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
        fun logInUserObserver()
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
        fun resetPasswordUserObserver()

    }

    interface Home {
        fun goToSeatReservation()
    }

    interface SeatReservation {
        fun showMessage(message: String, duration: Int)
        fun showProgressBar()
        fun hideProgressBar()
        fun observeFetchIterator()
        fun saveSeatReserved()
        fun saveSeatReservedObserver()
        fun addIteratorObserver()
    }

}