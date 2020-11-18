package com.circleappsstudio.mimisa.ui.auth.resetpassword

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
import com.google.firebase.FirebaseException
import kotlinx.android.synthetic.main.fragment_reset_password.*
import kotlinx.coroutines.launch

class ResetPasswordFragment : BaseFragment(), UI.ResetPassword {

    private lateinit var navController: NavController

    private lateinit var email: String

    private val logInViewModel by activityViewModels<LogInViewModel> {
        VMFactory(
            LogInRepo(
                LogInDataSource()
            )
        )
    }

    override fun getLayout(): Int {
        return R.layout.fragment_reset_password
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        btn_reset_password_user.setOnClickListener {
            resetPasswordUserUI()
        }

    }

    override fun showMessage(message: String, duration: Int) {
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        progressbar_reset_password_user.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressbar_reset_password_user.visibility = View.GONE
    }

    override fun resetPasswordUserUI() {

        email = txt_email_reset_password_user.text.toString()

        if (logInViewModel.checkEmptyFieldsForResetPasswordViewModel(email)){
            txt_email_reset_password_user.error = "Complete los campos."
            return
        }

        if (logInViewModel.checkValidEmailViewModel(email)){
            txt_email_reset_password_user.error = "El e-mail ingresado es inválido."
            return
        }

        showProgressBar()

        lifecycleScope.launch {

            try {

                logInViewModel.resetPasswordUserViewModel(email)
                showMessage("Correo de cambio de contraseña enviado.", 2)
                hideProgressBar()

            } catch (e: FirebaseException){
                showMessage(e.message.toString(), 2)
                hideProgressBar()
            }

        }

    }

}