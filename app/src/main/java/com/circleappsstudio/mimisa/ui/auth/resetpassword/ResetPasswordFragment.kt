package com.circleappsstudio.mimisa.ui.auth.resetpassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.auth.AuthDataSource
import com.circleappsstudio.mimisa.domain.auth.AuthRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAuth
import com.circleappsstudio.mimisa.ui.viewmodel.auth.AuthViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_reset_password.*

class ResetPasswordFragment : BaseFragment(),
        UI.ResetPassword,
        UI.IsOnlineDialogClickButtonListener {

    private lateinit var navController: NavController

    private val authViewModel by activityViewModels<AuthViewModel> {
        VMFactoryAuth(
                AuthRepository(
                        AuthDataSource()
                )
        )
    }

    private lateinit var email: String

    override fun getLayout(): Int = R.layout.fragment_reset_password

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        resetPasswordUser()

    }

    override fun resetPasswordUser() {

        btn_reset_password_user.setOnClickListener {

            hideKeyboard()

            email = txt_email_reset_password_user.text.toString().trim()

            if (!isOnline(requireContext())) {
                showIsOnlineDialog()
                return@setOnClickListener
            }

            if (authViewModel.checkEmptyEmailUser(email)){
                txt_email_reset_password_user.error = "Complete los campos."
                return@setOnClickListener
            }

            if (authViewModel.checkValidEmail(email)){
                txt_email_reset_password_user.error = "El e-mail ingresado es inválido."
                return@setOnClickListener
            }

            resetPasswordUserObserver()

        }

    }

    override fun resetPasswordUserObserver() {
        /*
             Método encargado de mandar un correo de cambio de contraseña a un
             usuario existente en el sistema.
        */
        if (isOnline(requireContext())) {

            authViewModel.resetPasswordUser(email)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted){

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        showMessage("Se ha enviado un correo electrónico a su buzón para el cambio de contraseña.", 2)
                        hideProgressBar()
                    }

                    is Resource.Failure -> {
                        showMessage(resultEmitted.exception.message.toString(), 2)
                        hideProgressBar()
                    }

                }

            })

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
        progressbar_reset_password_user.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un ProgressBar.
        */
        progressbar_reset_password_user.visibility = View.GONE
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