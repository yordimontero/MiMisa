package com.circleappsstudio.mimisa.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseActivity
import com.circleappsstudio.mimisa.data.datasource.auth.AuthDataSource
import com.circleappsstudio.mimisa.data.datasource.roleuser.RoleUserDataSource
import com.circleappsstudio.mimisa.domain.auth.AuthRepository
import com.circleappsstudio.mimisa.domain.roleuser.RoleUserRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.auth.LogInActivity
import com.circleappsstudio.mimisa.ui.auth.checkadmincode.CheckAdminCodeActivity
import com.circleappsstudio.mimisa.ui.main.MainActivity
import com.circleappsstudio.mimisa.ui.viewmodel.auth.AuthViewModel
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAdmin
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAuth
import com.circleappsstudio.mimisa.ui.viewmodel.roleuser.RoleUserViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.activity_loading.*

class LoadingActivity : BaseActivity(),
    UI.SplashScreen,
    UI.IsOnlineDialogClickButtonListener {

    private val authViewModel by lazy {
        ViewModelProvider(
            this, VMFactoryAuth(
                AuthRepository(
                    AuthDataSource()
                )
            )
        ).get(AuthViewModel::class.java)
    }

    private val adminViewModel by lazy {
        ViewModelProvider(
            this, VMFactoryAdmin(
                RoleUserRepository(
                    RoleUserDataSource()
                )
            )
        ).get(RoleUserViewModel::class.java)

    }

    private val emailUser by lazy { authViewModel.getEmailUser() }

    override fun getLayout(): Int = R.layout.activity_loading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkUserLogged()

    }

    override fun checkUserLogged() {
        /*
             Método encargado de validar que exista actualmente un usuario loggeado en el sistema.
        */

        if (!isOnline(this)) {
            showIsOnlineDialog()
            return
        }

        if (authViewModel.checkUserLogged()) {
            goToSignIn()
        } else {
            checkCreatedAdminByEmailUserObserver()
        }

    }

    override fun checkCreatedAdminByEmailUserObserver() {
        /*
            Método encargado de verificar si el usuario registrado tiene el rol de Administrador.
        */

        if (isOnline(this)) {

            adminViewModel.checkCreatedAdminByEmailUser(emailUser)
                .observe(this, Observer { resultEmitted ->

                    when (resultEmitted) {

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

    override fun goToCheckAdminCodeActivity() {
        /*
            Método para navegar hacia el Activity "CheckAdminCodeActivity".
        */
        val intent = Intent(this, CheckAdminCodeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    override fun goToSignIn() {
        /*
            Método para navegar hacia el Activity "LogInActivity".
        */
        val intent = Intent(this, LogInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    override fun goToMainActivity() {
        /*
            Método para navegar hacia el Activity "MainActivity".
        */
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    override fun showProgressBar() {
        /*
            Método encargado de mostrar un ProgressBar.
        */
        progressbar_loading_activity.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un ProgressBar.
        */
        progressbar_loading_activity.visibility = View.GONE
    }

    override fun showMessage(message: String, duration: Int) {
        /*
             Método encargado de mostrar un Toast.
        */
        this.toast(this, message, duration)
    }

    override fun showIsOnlineDialog() {
        /*
             Método encargado de mostrar el Dialog "isOnlineDialog".
        */
        isOnlineDialog(this, this)
    }

    override fun isOnlineDialogPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog "isOnlineDialog".
        */
        if (isOnline(this)) {
            checkUserLogged()
        } else {
            showIsOnlineDialog()
        }

    }

}