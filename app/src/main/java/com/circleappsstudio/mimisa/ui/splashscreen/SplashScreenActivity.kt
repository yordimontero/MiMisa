package com.circleappsstudio.mimisa.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseActivity
import com.circleappsstudio.mimisa.data.datasource.auth.AuthDataSource
import com.circleappsstudio.mimisa.domain.auth.AuthRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.auth.LogInActivity
import com.circleappsstudio.mimisa.ui.main.MainActivity
import com.circleappsstudio.mimisa.ui.viewmodel.auth.AuthViewModel
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAuth

class SplashScreenActivity : BaseActivity(), UI.SplashScreen {

    private val authViewModel by lazy {
        ViewModelProvider(
            this, VMFactoryAuth(
                AuthRepository(
                    AuthDataSource()
                )
            )
        ).get(AuthViewModel::class.java)
    }

    override fun getLayout(): Int {
        return R.layout.activity_splash_screen
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkUserLogged()

    }

    override fun checkUserLogged() {
        /*
             Método encargado de validar que exista actualmente un usuario loggeado en el sistema.
        */
        Handler().postDelayed({

            if (authViewModel.checkUserLogged()) {
                goToSignIn()
            } else {
                goToMainActivity()
            }

        }, 2000)

    }

    override fun goToSignIn() {
        /*
            Método para navegar hacia el fragment de registro de usuarios.
        */
        val intent = Intent(this, LogInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    override fun goToMainActivity() {
        /*
            Método para navegar hacia el menú principal.
        */
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

}