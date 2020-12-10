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

        fun signInUser()

        fun signInUserObserver()

        fun updateUserProfileObserver()

        fun signInUserWithGoogle()

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

        fun logInUser()

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

        fun resetPasswordUser()

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
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para
            la reservación de asientos.
        */
        fun fetchIteratorObserver()

        fun fetchSeatLimitObserver()

        fun saveSeatReservedObserver()

        fun addIteratorObserver()

        fun goToSeatReservationMain()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showDialog()

        fun checkSeatSavedByIdNumberUserObserver()

        fun checkSeatSavedByIdNumberUser()

    }

    interface IntentionMain {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para el
            registro de intenciones.
        */
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
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para el
            registro de intenciones.
        */
        fun setUpSpinner()

        fun getSelectedCategoryFromSpinner(intentionSpinnerAdapter: IntentionSpinner)

        fun saveIntentionObserver()

        fun saveIntention()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun gotToSeatReservationMain()

        fun showDialog()

    }

    interface More {
        /*
            Interface encargada de controlar los métodos del fragment "MoreFragment".
        */
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

    interface UserProfile {

        fun addListenerRadioButtons()

        fun fetchAdminCodeObserver()

        fun changeRole()

        fun createAdminObserver()

        fun showUserInfo()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showEditText()

        fun hideEditText()

        fun showDialog()

        fun showProfileLayout()

        fun hideProfileLayout()

        fun showChangeRoleLayout()

        fun hideChangeRoleLayout()

    }

    interface IsOnlineDialogClickButtonListener {
        fun onPositiveButtonClicked()
    }

}