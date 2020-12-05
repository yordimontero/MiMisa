package com.circleappsstudio.mimisa.ui

import android.content.Context
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
        fun showDialog()

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
        fun showDialog()

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
        fun showDialog()

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

        fun showRecyclerView()

        fun hideRecyclerView()

    }

    interface SeatReservation {

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

        fun checkSeatSavedByIdNumberUser()

        fun checkSeatSavedByIdNumberUserObserver()

    }

    interface IntentionMain {

        fun goToIntention()

        fun setUpRecyclerView()

        fun fetchSavedIntentionsByNameUserObserver()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showRecyclerView()

        fun hideRecyclerView()

    }

    interface Intentions {

        fun setUpSpinner()

        fun getSelectedCategoryFromSpinner(intentionSpinnerAdapter: IntentionSpinner)

        fun saveIntentionObserver()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun gotToSeatReservationMain()

        fun showDialog()

    }

    interface More {

        fun logOut()

        fun fetchUserName()

        fun links()

        fun goToFacebook()

        fun goToTwitter()

        fun goToWebPage()

        fun goToPrivacyPolicy()

        fun rateApp()

        fun goToPlayStoreMoreApps()

        fun goToSignIn()

    }

}