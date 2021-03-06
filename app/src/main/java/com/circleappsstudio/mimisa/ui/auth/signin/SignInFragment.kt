package com.circleappsstudio.mimisa.ui.auth.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.auth.AuthDataSource
import com.circleappsstudio.mimisa.data.datasource.roleuser.RoleUserDataSource
import com.circleappsstudio.mimisa.domain.auth.AuthRepository
import com.circleappsstudio.mimisa.domain.roleuser.RoleUserRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.auth.checkadmincode.CheckAdminCodeActivity
import com.circleappsstudio.mimisa.ui.main.MainActivity
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAuth
import com.circleappsstudio.mimisa.ui.viewmodel.auth.AuthViewModel
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAdmin
import com.circleappsstudio.mimisa.ui.viewmodel.roleuser.RoleUserViewModel
import com.circleappsstudio.mimisa.vo.Resource
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : BaseFragment(),
        UI.SignIn,
        UI.IsOnlineDialogClickButtonListener {

    private lateinit var navController: NavController

    private val authViewModel by activityViewModels<AuthViewModel> {
        VMFactoryAuth(
                AuthRepository(
                        AuthDataSource()
                )
        )
    }

    private val adminViewModel by activityViewModels<RoleUserViewModel> {
        VMFactoryAdmin(
                RoleUserRepository(
                        RoleUserDataSource()
                )
        )
    }

    private lateinit var fullName: String
    private lateinit var email: String
    private lateinit var password1: String
    private lateinit var password2: String

    override fun getLayout(): Int = R.layout.fragment_sign_in

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        signInUser()

        signInUserWithGoogle()

        goToLogin()

    }

    override fun signInUser() {

        btn_sign_in_user.setOnClickListener {

            hideKeyboard()

            fullName = txt_fullname_sign_in_user.text.toString().trim()
            email = txt_email_sign_in_user.text.toString().trim()
            password1 = txt_password1_sign_in_user.text.toString().trim()
            password2 = txt_password2_sign_in_user.text.toString().trim()

            if (!isOnline(requireContext())) {
                showIsOnlineDialog()
                return@setOnClickListener
            }

            if (authViewModel.checkEmptyNameUser(fullName)){
                txt_fullname_sign_in_user.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (authViewModel.checkEmptyEmailUser(email)){
                txt_email_sign_in_user.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (authViewModel.checkEmptyPassword1User(password1)){
                txt_password1_sign_in_user.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (authViewModel.checkEmptyPassword2User(password2)){
                txt_password2_sign_in_user.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (authViewModel.checkValidEmail(email)) {
                txt_email_sign_in_user.error = getString(R.string.wrong_email)
                return@setOnClickListener
            }

            if (authViewModel.checkValidPassword(password1)) {
                txt_password1_sign_in_user.error = getString(R.string.password_must_be_6_characters)
                return@setOnClickListener
            }

            if (authViewModel.checkMatchPasswordsForSignIn(password1, password2)){
                txt_password1_sign_in_user.error = getString(R.string.passwords_do_not_matches)
                txt_password2_sign_in_user.error = getString(R.string.passwords_do_not_matches)
                return@setOnClickListener
            }

            signInUserObserver()

        }

    }

    override fun signInUserObserver() {
        /*
             Método encargado de registrar un usuario nuevo en el sistema.
        */
        if (isOnline(requireContext())) {

            authViewModel.signInUser(email, password1)
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted){

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        updateUserProfileObserver()
                    }

                    is Resource.Failure -> {
                        showMessage(resultEmitted.exception.message.toString(), 2)
                        hideProgressBar()
                    }

                }

            })

        }

    }

    override fun updateUserProfileObserver() {
        /*
             Método encargado de setear el nombre de un usuario nuevo en el sistema.
        */
        if (isOnline(requireContext())) {

            authViewModel.updateUserProfile(fullName).observe(
                    viewLifecycleOwner,
                    Observer { resultEmitted ->

                        when (resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {
                                showMessage(getString(R.string.user_created_successfully), 2)
                                goToMainActivity()
                            }

                            is Resource.Failure -> {
                                showMessage(resultEmitted.exception.message.toString(), 2)
                                hideProgressBar()
                            }

                        }

                    })

        }

    }

    override fun checkCreatedAdminByEmailUserObserver(email: String) {
        /*
            Método encargado de verificar si el usuario registrado tiene el rol de Administrador.
        */

        if (isOnline(requireContext())) {

            adminViewModel.checkCreatedAdminByEmailUser(email)
                    .observe(this, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                if (resultEmitted.data) {
                                    goToCheckAdminCodeActivity()
                                } else {
                                    goToMainActivity()
                                }

                            }

                            is Resource.Failure -> {
                                showMessage(resultEmitted.exception.message.toString(), 2)
                                hideProgressBar()
                            }

                        }

                    })

        }

    }


    override fun signInUserWithGoogle() {
        /*
            Método encargado de autenticar por medio de Google.
        */
        btn_google_sign_in_user.setOnClickListener {

            startActivityForResult(
                    authViewModel.intentForGoogleAuth(),
                    authViewModel.getResultCode()
            )

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*
            OnActivityResult para autenticar por medio de Google.
        */
        if (requestCode == authViewModel.getResultCode()) {
            //Validación del RequestCode con la constante "RC_SIGN_IN".
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                //Autenticación exitosa.

                checkCreatedAdminByEmailUserObserver(authViewModel.getEmailUser())

            } else {
                //Error de autenticación.
                showMessage(response!!.error!!.errorCode.toString(), 2)
            }

        }

    }

    override fun goToLogin() {
        /*
             Método encargado de navegar hacia el Fragment "LogInFragment".
        */
        btn_go_to_login.setOnClickListener {
            navController.navigate(R.id.action_go_to_log_in_fragment_from_sign_in_fragment)
        }

    }

    override fun goToMainActivity() {
        /*
             Método encargado de navegar hacia el Activity "MainActivity".
        */
        if (!authViewModel.checkUserLogged()){

            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }

    }

    override fun goToCheckAdminCodeActivity() {
        /*
            Método para navegar hacia el Activity "CheckAdminCodeActivity".
        */
        if (!authViewModel.checkUserLogged()) {

            val intent = Intent(requireContext(), CheckAdminCodeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }

    }

    override fun showMessage(message: String, duration: Int) {
        /*
            Método encargado de mostrar un Toast.
        */
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        /*
            Método encargado de mostrar un ProgressBar.
        */
        progressbar_sign_in_user.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un ProgressBar.
        */
        progressbar_sign_in_user.visibility = View.GONE
    }

    override fun showIsOnlineDialog() {
        /*
             Método encargado de mostrar el Dialog "isOnlineDialog".
        */
        isOnlineDialog(this)

    }

    override fun isOnlineDialogPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog "isOnlineDialog".
        */
        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
        }

    }

}