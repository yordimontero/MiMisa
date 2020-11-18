package com.circleappsstudio.mimisa.ui.auth.signin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.login.LogInDataSource
import com.circleappsstudio.mimisa.domain.userauth.LogInRepo
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.viewmodel.factory.VMFactory
import com.circleappsstudio.mimisa.viewmodel.userauth.LogInViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.*
import com.google.firebase.FirebaseException
import kotlinx.coroutines.launch

class SignInFragment : BaseFragment(), UI.SignInUI {

    private lateinit var navController: NavController

    private lateinit var fullName: String
    private lateinit var email: String
    private lateinit var password1: String
    private lateinit var password2: String

    private val logInViewModel by activityViewModels<LogInViewModel> {
        VMFactory(
            LogInRepo(
                LogInDataSource()
            )
        )
    }

    override fun getLayout(): Int {
        return R.layout.fragment_sign_in
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        btn_sign_in_user.setOnClickListener {
            signInUserUI()
        }

        goToLogin()

        goToResetPassword()

    }

    override fun showMessage(message: String, duration: Int) {
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        progressbar_sign_in_user.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressbar_sign_in_user.visibility = View.GONE
    }

    override fun signInUserUI() {

        fullName = txt_fullname_sign_in_user.text.toString()
        email = txt_email_sign_in_user.text.toString()
        password1 = txt_password1_sign_in_user.text.toString()
        password2 = txt_password2_sign_in_user.text.toString()

        if (logInViewModel.checkEmptyFieldsForSignInViewModel(fullName, email, password1, password2)) {
            txt_fullname_sign_in_user.error = "Complete los campos."
            txt_email_sign_in_user.error = "Complete los campos."
            txt_password1_sign_in_user.error = "Complete los campos."
            txt_password2_sign_in_user.error = "Complete los campos."
            return
        }

        if (logInViewModel.checkValidEmailViewModel(email)) {
            txt_email_sign_in_user.error = "El e-mail ingresado es incorrecto."
            return
        }

        if (logInViewModel.checkValidPasswordViewModel(password1)) {
            txt_password1_sign_in_user.error = "La contraseña debe tener al menos 6 caracteres."
            return
        }

        if (logInViewModel.checkMatchPasswordsForSignInViewModel(password1, password2)){
            txt_password1_sign_in_user.error = "Las contraseñas no coinciden."
            txt_password2_sign_in_user.error = "Las contraseñas no coinciden."
            return
        }

        showProgressBar()

        lifecycleScope.launch {

            try {

                logInViewModel.signInUserViewModel(email, password1)

                logInViewModel.updateUserProfileViewModel(fullName)

                showMessage("Usuario registrado con éxito.", 1)
                hideProgressBar()

            } catch (e: FirebaseException){
                showMessage(e.message.toString(), 2)
                hideProgressBar()
            }

        }

    }

    override fun goToLogin() {

        btn_go_to_login.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_logInFragment)
        }

    }

    override fun goToResetPassword() {
        btn_go_to_reset_password.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_resetPasswordFragment)
        }
    }

}