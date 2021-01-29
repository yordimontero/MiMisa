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
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogInFragment : BaseFragment(),
        UI.LogIn,
        UI.IsOnlineDialogClickButtonListener {

    private lateinit var navController: NavController

    private val authViewModel by activityViewModels<AuthViewModel>{
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

    private lateinit var email: String
    private lateinit var password: String

    override fun getLayout(): Int = R.layout.fragment_log_in

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        logInUser()

        goToResetPassword()

    }

    override fun logInUser() {

        btn_log_in_user.setOnClickListener {

            hideKeyboard()

            email = txt_email_log_in_user.text.toString().trim()
            password = txt_password_log_in_user.text.toString().trim()

            if (!isOnline(requireContext())) {
                showIsOnlineDialog()
                return@setOnClickListener
            }

            if (authViewModel.checkEmptyEmailUser(email)){
                txt_email_log_in_user.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (authViewModel.checkEmptyPassword1User(password)){
                txt_password_log_in_user.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (authViewModel.checkValidEmail(email)){
                txt_email_log_in_user.error = getString(R.string.wrong_email)
                return@setOnClickListener
            }

            if (authViewModel.checkValidPassword(password)){
                txt_password_log_in_user.error = getString(R.string.wrong_password)
                return@setOnClickListener
            }

            logInUserObserver()

        }

    }

    override fun logInUserObserver() {
        /*
             Método encargado de loggear un usuario existente en el sistema.
        */
        if (isOnline(requireContext())) {

            authViewModel.logInUser(email, password)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted){

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        checkCreatedAdminByEmailUserObserver(email)
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
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

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

    override fun goToResetPassword() {
        /*
             Método encargado de navegar hacia el Fragment "ResetPasswordFragment".
        */
        btn_go_to_reset_password.setOnClickListener {
            navController.navigate(R.id.action_go_to_reset_password_fragment_from_log_in_fragment)
        }
    }


    override fun goToCheckAdminCodeActivity() {
        /*
            Método para navegar hacia el Activity "CheckAdminCodeActivity".
        */
        val intent = Intent(requireContext(), CheckAdminCodeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

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
        progressbar_log_in_user.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un ProgressBar.
        */
        progressbar_log_in_user.visibility = View.GONE
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