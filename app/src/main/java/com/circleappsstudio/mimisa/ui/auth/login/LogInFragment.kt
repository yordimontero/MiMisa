package com.circleappsstudio.mimisa.ui.auth.login

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
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAuth
import com.circleappsstudio.mimisa.ui.viewmodel.auth.AuthViewModel
import com.google.firebase.FirebaseException
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.coroutines.launch

class LogInFragment : BaseFragment(), UI.LogInUI {

    private lateinit var navController: NavController

    private lateinit var email: String
    private lateinit var password: String

    private val authViewModel by activityViewModels<AuthViewModel>{
        VMFactoryAuth(
            AuthRepo(
                AuthDataSource()
            )
        )
    }

    override fun getLayout(): Int {
        return R.layout.fragment_log_in
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        btn_log_in_user.setOnClickListener {
            logInUserUI()
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
            Método encargado de mostrar un progress bar.
        */
        progressbar_log_in_user.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un progress bar.
        */
        progressbar_log_in_user.visibility = View.GONE
    }

    override fun logInUserUI() {
        /*
             Método encargado de loggear un usuario existente en el sistema.
        */

        email = txt_email_log_in_user.text.toString()
        password = txt_password_log_in_user.text.toString()

        if (authViewModel.checkEmptyFieldsForLogInViewModel(email, password)){
            txt_email_log_in_user.error = "Complete los campos."
            txt_password_log_in_user.error = "Complete los campos."
            return
        }

        if (authViewModel.checkValidEmailViewModel(email)){
            txt_email_log_in_user.error = "El e-mail ingresado es incorrecto."
            return
        }

        if (authViewModel.checkValidPasswordViewModel(password)){
            txt_password_log_in_user.error = "Contraseña incorrecta."
            return
        }

        showProgressBar()

        lifecycleScope.launch {

            try {

                authViewModel.logInUserViewModel(email, password)

                goToMainActivity()

            } catch (e: FirebaseException){
                showMessage(e.message.toString(), 2)
                hideProgressBar()
            }

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