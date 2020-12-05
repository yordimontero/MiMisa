package com.circleappsstudio.mimisa.ui.auth.login

import android.content.Intent
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
import com.circleappsstudio.mimisa.ui.main.MainActivity
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAuth
import com.circleappsstudio.mimisa.ui.viewmodel.auth.AuthViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogInFragment : BaseFragment(), UI.LogInUI, OnDialogClickButtonListener {

    private lateinit var navController: NavController

    private val authViewModel by activityViewModels<AuthViewModel>{
        VMFactoryAuth(
                AuthRepository(
                        AuthDataSource()
                )
        )
    }

    private lateinit var email: String
    private lateinit var password: String

    override fun getLayout(): Int {
        return R.layout.fragment_log_in
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

        if (!isOnline(requireContext())) {
            showDialog()
            return
        }

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

        logInUserObserver()

    }

    override fun logInUserObserver() {

        if (isOnline(requireContext())) {

            authViewModel.logInUserViewModel(email, password).observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted){

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
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

            logInUserUI()

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