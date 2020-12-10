package com.circleappsstudio.mimisa.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseActivity
import com.circleappsstudio.mimisa.ui.auth.LogInActivity

class SplashScreenActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_splash_screen
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        goToLogin()

    }

    fun goToLogin() {

        Handler().postDelayed({

            val intent = Intent(this, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }, 2000)

    }

}