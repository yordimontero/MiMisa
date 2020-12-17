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

        fun goToAdminMainActivity()

        fun checkCreatedAdminByEmailUserObserver()

        fun showProgressBar()

        fun hideProgressBar()

        fun showMessage(message: String, duration: Int)

        fun showDialog()

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

        fun fetchData()

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

        fun showDialog()

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

    interface AdminSeatReservation {

        fun fetchData()

        fun fetchSavedSeats()

        fun fetchSeatLimitObserver()

        fun updateSeatLimit()

        fun setupRecyclerView()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showRecyclerView()

        fun hideRecyclerView()

        fun showDialog()

    }

    interface IntentionMain {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para el
            registro de intenciones.
        */

        fun fetchData()

        fun goToIntention()

        fun setUpRecyclerView()

        fun fetchSavedIntentionsByNameUserObserver()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showRecyclerView()

        fun hideRecyclerView()

        fun showDialog()

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

    interface AdminIntentions {

        fun fetchData()

        fun fetchSavedIntentions()

        fun setupRecyclerView()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showRecyclerView()

        fun hideRecyclerView()

        fun showDialog()

    }

    interface More {
        /*
            Interface encargada de controlar los métodos del fragment "MoreFragment".
        */
        fun goToProfile()

        fun links()

        fun goToFacebook()

        fun goToTwitter()

        fun goToWebPage()

        fun goToPrivacyPolicy()

        fun rateApp()

        fun goToPlayStoreMoreApps()

    }

    interface UserProfile {
        /*
            Interface encargada de controlar los métodos del fragment "ProfileUserFragment".
        */
        fun fetchData()

        fun setCheckedRadioButton()

        fun addListenerRadioButtons()

        fun fetchAdminCodeObserver()

        fun showUserInfo(resultEmitted: Boolean)

        fun changeRole()

        fun deleteAdminObserver()

        fun checkCreatedAdminByEmailUserObserver()

        fun createAdminObserver()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showEditText()

        fun hideEditText()

        fun showDialog()

        fun showChangeRoleLayout()

        fun hideChangeRoleLayout()

        fun goToMainActivity()

        fun goToAdminMainActivity()
        
        fun logOut()

        fun goToSignIn()

    }

    interface Home {

        fun fetchIsAvailable()

        fun fetchVersionCode()

        fun showIsOnlineDialog()

        fun showIsAvailableDialog()

        fun showUpdateAppDialog()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

    }

    interface AdminHome {

        fun fetchIsAvailable()

        fun setAvailability()

        fun fetchVersionCode()

        fun showIsOnlineDialog()

        fun showIsAvailableDialog()

        fun showUpdateAppDialog()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun activateToggle()

        fun deactivateToggle()

        fun enableSystem()

        fun disableSystem()

    }

    interface IsOnlineDialogClickButtonListener {
        fun isOnlineDialogPositiveButtonClicked()
    }

    interface IsAvailableDialogClickButtonListener {
        fun isAvailablePositiveButtonClicked()
    }

    interface UpdateAppDialogClickButtonListener {
        fun updateAppPositiveButtonClicked()
        fun updateAppNegativeButtonClicked()
    }

}