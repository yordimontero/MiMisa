package com.circleappsstudio.mimisa.ui

import android.app.AlertDialog
import com.circleappsstudio.mimisa.data.model.IntentionSpinner

interface UI {

    interface SplashScreen {
        /*
            Interface encargada de controlar los métodos del SplashScreen.
        */
        fun checkUserLogged()

        fun checkCreatedAdminByEmailUserObserver()

        fun goToCheckAdminCodeActivity()

        fun goToSignIn()

        fun goToMainActivity()

        fun showProgressBar()

        fun hideProgressBar()

        fun showMessage(message: String, duration: Int)

        fun showIsOnlineDialog()

    }

    interface SignIn {
        /*
            Interface encargada de controlar los métodos del fragment de registro de usuarios.
        */
        fun signInUser()

        fun signInUserObserver()

        fun updateUserProfileObserver()

        fun checkCreatedAdminByEmailUserObserver(email: String)

        fun signInUserWithGoogle()

        fun goToLogin()

        fun goToMainActivity()

        fun goToCheckAdminCodeActivity()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showIsOnlineDialog()

    }

    interface LogIn {
        /*
            Interface encargada de controlar los métodos del fragment de loggeo de usuarios.
        */
        fun logInUser()

        fun logInUserObserver()

        fun checkCreatedAdminByEmailUserObserver(email: String)

        fun goToMainActivity()

        fun goToResetPassword()

        fun goToCheckAdminCodeActivity()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showIsOnlineDialog()

    }

    interface ResetPassword {
        /*
            Interface encargada de controlar los métodos del fragment de cambio de contraseña de usuarios.
        */
        fun resetPasswordUser()

        fun resetPasswordUserObserver()

        fun goToLogIn()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showIsOnlineDialog()

    }

    interface CheckAdminCode {
        /*
            Interface encargada de controlar los métodos del fragment
            de verificación del código de seguridad.
        */
        fun fetchData()

        fun fetchAdminCodeObserver()

        fun verifyAdminCode()

        fun goToAdminMainActivity()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showIsOnlineDialog()

    }

    interface MainActivity {
        /*
            Interface encargada de controlar los métodos de MainActivity.
        */
        fun setNavViewVisibility()

        fun showNavView()

        fun hideNavView()

        fun fetchData()

        fun fetchVersionCode()

        fun initAppRate()

        fun showIsOnlineDialog()

        fun showUpdateAppDialog()

    }

    interface AdminMainActivity {
        /*
            Interface encargada de controlar los métodos de AdminMainActivity.
        */
        fun setNavViewVisibility()

        fun showNavView()

        fun hideNavView()

        fun fetchData()

        fun fetchVersionCode()

        fun initAppRate()

        fun showIsOnlineDialog()

        fun showUpdateAppDialog()

    }



    interface SeatReservationMain {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para
            la reservación de asientos.
        */
        fun fetchData()

        fun fetchRegisteredSeatsByUserNameObserver()

        fun fetchIsSeatReservationAvailable()

        fun goToSeatReservation()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun setUpRecyclerView()

        fun showRecyclerView()

        fun hideRecyclerView()

        fun showInfoMessage()

        fun hideInfoMessage()

        fun changeTextViewToNoSeatsAvailable()

        fun changeTextViewToDisabledSeatReservation()

        fun showNoRegisteredSeatsYetMessage()

        fun hideNoRegisteredSeatsYetMessage()

        fun showButton()

        fun hideButton()

        fun showIsOnlineDialog()

    }

    interface SeatCategory {

        fun checkIfUserIsAdmin()

        fun goToCoupleSeat()

        fun goToThreesomeSeat()

        fun goToBubbleSeat()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

    }

    interface AllSeatCouples {

        fun fetchData()

        fun checkIfUserIsAdmin()

        fun checkIfIsCoupleAvailable()

        fun loadAvailableCouplesObserver()

        fun loadNoAvailableCouplesObserver()

        fun showAvailableCoupleLinearLayout(documentId: String)

        fun showNoAvailableCoupleLinearLayout(documentId: String)

        fun callAllToGoCouples()

        fun goToCouple1()

        fun goToCouple2()

        fun goToCouple3()

        fun goToCouple4()

        fun goToCouple5()

        fun goToCouple6()

        fun goToCouple7()

        fun goToCouple8()

        fun goToCouple9()

        fun goToCouple10()

        fun goToCouple11()

        fun goToCouple12()

        fun goToCouple13()

        fun goToCouple14()

        fun goToCouple15()

        fun goToCouple16()

        fun goToCouple17()

        fun goToCouple18()

        fun goToCouple19()

        fun goToCouple20()

        fun goToCouple21()

        fun goToCouple22()

        fun goToCouple23()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showIsOnlineDialog()

    }

    interface CoupleSeatCategory {

        fun fetchData()

        fun getBundle()

        fun checkIfUserIsAdmin()

        fun fetchIsSeatReservationAvailableObserver()

        fun saveSeatReserved()

        fun checkSeatSavedByIdNumberUserObserver()

        fun saveSeatReservedObserver()

        fun setIsCoupleAvailable()

        fun setIsNotCoupleAvailable()

        fun setVisibilityIdNumberUserField()

        fun clearFields()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun goToMainSeatReservation()

        fun showIsOnlineDialog()

        fun showIsSeatReservationAvailableDialog()

        fun showConfirmDialog(): AlertDialog?

        fun showReserveSeatDialog(): AlertDialog?

    }

    interface AllSeatThreesomes {

        fun fetchData()

        fun checkIfUserIsAdmin()

        fun checkIfIsThreesomeAvailable()

        fun loadAvailableThreesomesObserver()

        fun loadNoAvailableThreesomesObserver()

        fun showAvailableThreesomeTextView(documentId: String)

        fun showNoAvailableThreesomeTextView(documentId: String)

        fun callAllToGoThreesomes()

        fun goToThreesome1()

        fun goToThreesome2()

        fun goToThreesome3()

        fun goToThreesome4()

        fun goToThreesome5()

        fun goToThreesome6()

        fun goToThreesome7()

        fun goToThreesome8()

        fun goToThreesome9()

        fun goToThreesome10()

        fun goToThreesome11()

        fun goToThreesome12()

        fun goToThreesome13()

        fun goToThreesome14()

        fun goToThreesome15()

        fun goToThreesome16()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showIsOnlineDialog()

    }

    interface ThreesomeSeatCategory {

        fun fetchData()

        fun getBundle()

        fun checkIfUserIsAdmin()

        fun fetchIsSeatReservationAvailableObserver()

        fun saveSeatReserved()

        fun checkSeatSavedByIdNumberUserObserver()

        fun saveSeatReservedObserver()

        fun setIsThreesomeAvailable()

        fun setIsNotThreesomeAvailable()

        fun setVisibilityIdNumberUserField()

        fun clearFields()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun goToMainSeatReservation()

        fun showIsOnlineDialog()

        fun showIsSeatReservationAvailableDialog()

        fun showConfirmDialog(): AlertDialog?

        fun showReserveSeatDialog(): AlertDialog?

    }

    interface AllSeatBubbles {

        fun fetchData()

        fun checkIfUserIsAdmin()

        fun checkIfIsBubbleAvailable()

        fun loadAvailableBubblesObserver()

        fun loadNoAvailableBubblesObserver()

        fun showAvailableBubbleLinearLayout(documentId: String)

        fun showNoAvailableBubbleLinearLayout(documentId: String)

        fun callAllToGoBubbles()

        fun goToBubble1()

        fun goToBubble2()

        fun goToBubble3()

        fun goToBubble4()

        fun goToBubble5()

        fun goToBubble6()

        fun goToBubble7()

        fun goToBubble8()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showIsOnlineDialog()

    }

    interface BubbleSeatCategory {

        fun fetchData()

        fun getBundle()

        fun checkIfUserIsAdmin()

        fun fetchIsSeatReservationAvailableObserver()

        fun saveSeatReserved()

        fun checkSeatSavedByIdNumberUserObserver()

        fun saveSeatReservedObserver()

        fun setIsBubbleAvailable()

        fun setIsNotBubbleAvailable()

        fun setVisibilityIdNumberUserField()

        fun clearFields()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun goToMainSeatReservation()

        fun showIsOnlineDialog()

        fun showIsSeatReservationAvailableDialog()

        fun showConfirmDialog(): AlertDialog?

        fun showReserveSeatDialog(): AlertDialog?

    }

    interface SeatReservation {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para
            la reservación de asientos.
        */
        fun fetchData()

        fun fetchIsSeatReservationAvailable()

        fun fetchIteratorObserver()

        fun fetchSeatLimitObserver()

        fun saveSeatReserved()

        fun saveSeatReservedObserver()

        fun addIteratorObserver()

        fun goToSeatReservationMain()

        fun checkSeatSavedByIdNumberUserObserver()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showIsOnlineDialog()

        fun showIsSeatReservationAvailableDialog()

        fun showConfirmDialog(): AlertDialog?

    }

    interface AdminSeatReservation {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para
            la reservación de asientos en el rol de Administrador.
        */
        fun fetchData()

        fun fetchAllRegisteredSeatsObserver()

        //fun fetchRegisteredSeatByRegisteredPersonObserver(registeredPerson: String)

        fun fetchRegisteredSeatBySeatNumberObserver(seatNumber: String)

        fun goToSeatCategoryReservation()

        fun goToOptionAdminSeatReservation()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showRecyclerView()

        fun hideRecyclerView()

        fun setupSearchView()

        fun setupRecyclerView()

        fun showNotSeatFoundedMessage()

        fun hideNotSeatFoundedMessage()

        fun showIsOnlineDialog()

    }

    interface OptionsAdminSeatReservation {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para
            la reservación de asientos en el rol de Administrador.
        */
        fun fetchData()

        fun generateAllSeatsReport()

        fun generateAllSeatsReportObserver()

        fun fetchIsSeatReservationAvailableObserver()

        fun setAvailability()

        fun enableSeatReservation()

        fun disableSeatReservation()

        fun fetchSeatLimitObserver()

        fun updateSeatLimit()

        fun updateSeatLimitObserver()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun activateToggle()

        fun deactivateToggle()

        fun showIsOnlineDialog()

        fun showConfirmDialog(): AlertDialog?

    }

    interface IntentionMain {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para el
            registro de intenciones.
        */
        fun fetchData()

        fun fetchIsRegisterIntentionAvailable()

        fun fetchSavedIntentionsByNameUserObserver()

        fun goToIntention()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun setUpRecyclerView()

        fun showRecyclerView()

        fun hideRecyclerView()

        fun showNoRegisteredSeatsYetMessage()

        fun hideNoRegisteredSeatsYetMessage()

        fun showInfoMessage()

        fun hideInfoMessage()

        fun changeTextViewToDisabledSeatReservation()

        fun showButton()

        fun hideButton()

        fun showIsOnlineDialog()

    }

    interface Intentions {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para el
            registro de intenciones.
        */
        fun checkIfUserIsAdmin()

        fun saveIntention()

        fun saveIntentionObserver()

        fun setUpSpinner()

        fun getSelectedCategoryFromSpinner(intentionSpinnerAdapter: IntentionSpinner)

        fun gotToSeatReservationMain()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showIsOnlineDialog()

        fun showConfirmDialog(): AlertDialog?

    }

    interface AdminIntentions {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para el
            registro de intenciones en el rol de Administrador.
        */
        fun fetchData()

        fun fetchAllSavedIntentionsObserver()

        fun fetchSavedIntentionsByCategory()

        fun fetchSavedIntentionsByCategoryObserver(category: String)

        fun goToIntentionAdmin()

        fun goToOptionAdminIntention()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun setupRecyclerView()

        fun showRecyclerView()

        fun hideRecyclerView()

        fun showMainLayout()

        fun hideMainLayout()

        fun showNotIntentionFoundedMessage()

        fun hideNotIntentionFoundedMessage()

        fun showIsOnlineDialog()

    }

    interface OptionsAdminIntentions {
        /*
            Interface encargada de controlar los métodos de la base de datos Firestore para el
            registro de intenciones en el rol de Administrador.
        */
        fun fetchData()

        fun generateAllIntentionsReport()

        fun generateAllIntentionsReportObserver()

        fun fetchIsRegisterIntentionAvailableObserver()

        fun setAvailability()

        fun enableRegisterIntention()

        fun disableRegisterIntention()

        fun activateToggle()

        fun deactivateToggle()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showIsOnlineDialog()

        fun showConfirmDialog(): AlertDialog?

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

        fun goToUserManual()

        fun goToPrivacyPolicy()

        fun sendEmail()

        fun rateApp()

        fun goToPlayStoreMoreApps()

    }

    interface UserProfile {
        /*
            Interface encargada de controlar los métodos del fragment "ProfileUserFragment".
        */
        fun fetchData()

        fun setCheckedRadioButton()

        fun showUserInfo(resultEmitted: Boolean)

        fun fetchAdminCodeObserver()

        fun changeRole()

        fun checkCreatedAdminByEmailUserObserver()

        fun createAdminObserver()

        fun deleteAdminObserver()

        fun addListenerRadioButtons()

        fun logOut()

        fun goToMainActivity()

        fun goToAdminMainActivity()

        fun goToSignIn()

        fun showMessage(message: String, duration: Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun showEditText()

        fun hideEditText()

        fun showChangeRoleLayout()

        fun hideChangeRoleLayout()

        fun showIsOnlineDialog()

        fun showConfirmDialog(): AlertDialog?

    }

    interface IsOnlineDialogClickButtonListener {
        fun isOnlineDialogPositiveButtonClicked()
    }

    interface IsSeatReservationAvailableDialogClickButtonListener {
        fun isSeatReservationAvailablePositiveButtonClicked()
    }

    interface IsRegisterIntentionAvailableDialogClickButtonListener {
        fun isRegisterIntentionAvailablePositiveButtonClicked()
    }

    interface UpdateAppDialogClickButtonListener {
        fun updateAppPositiveButtonClicked()
        fun updateAppNegativeButtonClicked()
    }

    interface ConfirmDialogClickButtonListener {
        fun confirmPositiveButtonClicked()
        fun confirmNegativeButtonClicked()
    }

    interface ReserveSeatDialogClickButtonListener {
        fun reserveSeatPositiveButtonClicked()
        fun reserveSeatNegativeButtonClicked()
    }


}