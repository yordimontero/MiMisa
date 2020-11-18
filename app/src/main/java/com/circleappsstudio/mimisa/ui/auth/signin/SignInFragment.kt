package com.circleappsstudio.mimisa.ui.auth.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.auth.AuthDataSource
import com.circleappsstudio.mimisa.domain.auth.AuthRepo
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.main.MainActivity
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactory
import com.circleappsstudio.mimisa.ui.viewmodel.auth.AuthViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.*
import com.google.firebase.FirebaseException
import kotlinx.coroutines.launch

class SignInFragment : BaseFragment(), UI.SignInUI {

    private lateinit var navController: NavController

    private lateinit var fullName: String
    private lateinit var email: String
    private lateinit var password1: String
    private lateinit var password2: String

    private val authViewModel by activityViewModels<AuthViewModel> {
        VMFactory(
            AuthRepo(
                AuthDataSource()
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
        /*
            Método encargado de mostrar un Toast.
        */
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        /*
            Método encargado de mostrar un progress bar.
        */
        progressbar_sign_in_user.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un progress bar.
        */
        progressbar_sign_in_user.visibility = View.GONE
    }

    override fun signInUserUI() {
        /*
             Método encargado de registrar un usuario nuevo en el sistema
             y setear el nombre de un usuario nuevo en el sistema.
        */

        fullName = txt_fullname_sign_in_user.text.toString()
        email = txt_email_sign_in_user.text.toString()
        password1 = txt_password1_sign_in_user.text.toString()
        password2 = txt_password2_sign_in_user.text.toString()

        if (authViewModel.checkEmptyFieldsForSignInViewModel(fullName, email, password1, password2)) {
            txt_fullname_sign_in_user.error = "Complete los campos."
            txt_email_sign_in_user.error = "Complete los campos."
            txt_password1_sign_in_user.error = "Complete los campos."
            txt_password2_sign_in_user.error = "Complete los campos."
            return
        }

        if (authViewModel.checkValidEmailViewModel(email)) {
            txt_email_sign_in_user.error = "El e-mail ingresado es incorrecto."
            return
        }

        if (authViewModel.checkValidPasswordViewModel(password1)) {
            txt_password1_sign_in_user.error = "La contraseña debe tener al menos 6 caracteres."
            return
        }

        if (authViewModel.checkMatchPasswordsForSignInViewModel(password1, password2)){
            txt_password1_sign_in_user.error = "Las contraseñas no coinciden."
            txt_password2_sign_in_user.error = "Las contraseñas no coinciden."
            return
        }

        showProgressBar()

        lifecycleScope.launch {

            try {

                authViewModel.signInUserViewModel(email, password1)

                authViewModel.updateUserProfileViewModel(fullName)

                showMessage("Usuario registrado con éxito.", 1)

                goToMainActivity()

            } catch (e: FirebaseException){
                showMessage(e.message.toString(), 2)
                hideProgressBar()
            }

        }

    }

    override fun goToLogin() {
        /*
             Método encargado de navegar hacia el login.
        */
        btn_go_to_login.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_logInFragment)
        }

    }

    override fun goToResetPassword() {
        /*
             Método encargado de navegar hacia la pantalla de cambiar contraseña.
        */
        btn_go_to_reset_password.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_resetPasswordFragment)
        }
    }

    override fun goToMainActivity() {
        /*
             Método encargado de navegar hacia el menú principal.
        */
        if (!authViewModel.checkUserLogged()){

            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }

    }

}