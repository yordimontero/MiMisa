package com.circleappsstudio.mimisa.ui

import com.circleappsstudio.mimisa.data.model.IntentionSpinner

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

    interface SeatReservationMain {

        fun setUpRecyclerView()

        fun fetchRegisteredSeatsByUserNameObserver()

        fun goToSeatReservation()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showNoSeatsAvailable()

        fun hideButton()

        fun fetchIteratorObserver()

        fun fetchSeatLimitObserver()

        fun checkAvailableSeats()

    }

    interface SeatReservation {

        /*fun fetchIteratorObserver()

        fun fetchSeatLimitObserver()

        fun saveSeatReservedObserver()

        fun addIteratorObserver()

        fun saveSeatReserved()

        fun gotToSeatReservationMain()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun hideForm()*/

        fun fetchIteratorObserver()

        fun fetchSeatLimitObserver()

        fun saveSeatReserved()

        fun saveSeatReservedObserver()

        fun addIteratorObserver()

        fun goToSeatReservationMain()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showDialog()

    }

    interface IntentionMain {

        fun goToIntention()

        fun setUpRecyclerView()

        fun fetchSavedIntentionsByNameUserObserver()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

    }

    interface Intentions {

        fun setUpSpinner()

        fun getSelectedCategoryFromSpinner(intentionSpinnerAdapter: IntentionSpinner)

        fun saveIntentionObserver()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun gotToSeatReservationMain()

    }

}