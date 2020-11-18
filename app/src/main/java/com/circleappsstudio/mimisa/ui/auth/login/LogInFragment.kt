package com.circleappsstudio.mimisa.ui.auth.login

import android.os.Bundle
import android.view.View
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
import com.google.firebase.FirebaseException
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.coroutines.launch

class LogInFragment : BaseFragment(), UI.LogInUI {

    private lateinit var navController: NavController

    private lateinit var email: String
    private lateinit var password: String

    private val loginViewModel by activityViewModels<LogInViewModel>{

        VMFactory(
            LogInRepo(
                LogInDataSource()
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
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        progressbar_log_in_user.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressbar_log_in_user.visibility = View.GONE
    }

    override fun logInUserUI() {

        email = txt_email_log_in_user.text.toString()
        password = txt_password_log_in_user.text.toString()

        if (loginViewModel.checkEmptyFieldsForLogInViewModel(email, password)){
            txt_email_log_in_user.error = "Complete los campos."
            txt_password_log_in_user.error = "Complete los campos."
            return
        }

        if (loginViewModel.checkValidEmailViewModel(email)){
            txt_email_log_in_user.error = "El e-mail ingresado es incorrecto."
            return
        }

        if (loginViewModel.checkValidPasswordViewModel(password)){
            txt_password_log_in_user.error = "Contraseña incorrecta."
            return
        }

        showProgressBar()

        lifecycleScope.launch {

            try {

                loginViewModel.logInUserViewModel(email, password)
                showMessage("Login exitoso.", 1)
                hideProgressBar()

            } catch (e: FirebaseException){
                showMessage(e.message.toString(), 2)
                hideProgressBar()
            }

        }

    }

}