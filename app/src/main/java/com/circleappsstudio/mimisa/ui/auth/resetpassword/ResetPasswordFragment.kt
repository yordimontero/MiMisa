package com.circleappsstudio.mimisa.ui.auth.resetpassword

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
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAuth
import com.circleappsstudio.mimisa.ui.viewmodel.auth.AuthViewModel
import com.google.firebase.FirebaseException
import kotlinx.android.synthetic.main.fragment_reset_password.*
import kotlinx.coroutines.launch

class ResetPasswordFragment : BaseFragment(), UI.ResetPassword {

    private lateinit var navController: NavController

    private lateinit var email: String

    private val authViewModel by activityViewModels<AuthViewModel> {
        VMFactoryAuth(
            AuthRepo(
                AuthDataSource()
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
        /*
            Método encargado de mostrar un Toast.
        */
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        /*
            Método encargado de mostrar un progress bar.
        */
        progressbar_reset_password_user.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un progress bar.
        */
        progressbar_reset_password_user.visibility = View.GONE
    }

    override fun resetPasswordUserUI() {
        /*
             Método encargado de mandar un correo de cambio de contraseña a un
             usuario existente en el sistema.
        */
        email = txt_email_reset_password_user.text.toString()

        if (authViewModel.checkEmptyFieldsForResetPasswordViewModel(email)){
            txt_email_reset_password_user.error = "Complete los campos."
            return
        }

        if (authViewModel.checkValidEmailViewModel(email)){
            txt_email_reset_password_user.error = "El e-mail ingresado es inválido."
            return
        }

        showProgressBar()

        lifecycleScope.launch {

            try {

                authViewModel.resetPasswordUserViewModel(email)
                showMessage("Correo de cambio de contraseña enviado.", 2)
                hideProgressBar()

            } catch (e: FirebaseException){
                showMessage(e.message.toString(), 2)
                hideProgressBar()
            }

        }

    }

}