package com.circleappsstudio.mimisa.ui.auth.resetpassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.base.OnDialogClickButtonListener
import com.circleappsstudio.mimisa.data.datasource.auth.AuthDataSource
import com.circleappsstudio.mimisa.domain.auth.AuthRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAuth
import com.circleappsstudio.mimisa.ui.viewmodel.auth.AuthViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_reset_password.*

class ResetPasswordFragment : BaseFragment(), UI.ResetPassword, OnDialogClickButtonListener {

    private lateinit var navController: NavController

    private val authViewModel by activityViewModels<AuthViewModel> {
        VMFactoryAuth(
                AuthRepository(
                        AuthDataSource()
                )
        )
    }

    private lateinit var email: String

    override fun getLayout(): Int {
        return R.layout.fragment_reset_password
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

        if (!isOnline(requireContext())) {
            showDialog()
            return
        }

        if (authViewModel.checkEmptyFieldsForResetPasswordViewModel(email)){
            txt_email_reset_password_user.error = "Complete los campos."
            return
        }

        if (authViewModel.checkValidEmailViewModel(email)){
            txt_email_reset_password_user.error = "El e-mail ingresado es inválido."
            return
        }

        resetPasswordUserObserver()

    }

    override fun resetPasswordUserObserver() {

        if (isOnline(requireContext())) {

            authViewModel.resetPasswordUserViewModel(email).observe(viewLifecycleOwner, Observer { resultEmitted ->

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

    override fun showDialog() {

        dialog(this,
                "¡No hay conexión a Internet!",
                "Verifique su conexión e inténtelo de nuevo.",
                R.drawable.ic_wifi_off,
                "Intentar de nuevo",
                ""
        )

    }

    override fun onPositiveButtonClicked() {

        if (isOnline(requireContext())){

            resetPasswordUserUI()

        } else {

            dialog(this,
                    "¡No hay conexión a Internet!",
                    "Verifique su conexión e inténtelo de nuevo.",
                    R.drawable.ic_wifi_off,
                    "Intentar de nuevo",
                    ""
            )

        }

    }

    override fun onNegativeButtonClicked() {
        TODO("Not yet implemented")
    }

}