package com.circleappsstudio.mimisa.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.content.res.AppCompatResources
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : BaseActivity() {

    override fun getLayout(): Int = R.layout.activity_splash_screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSplashScreenUIByCurrentTheme()

        goToLoadingActivity()

    }

    private fun setSplashScreenUIByCurrentTheme() {
        /*
            Método encargado de cambiar la UI de la SplashScreen con base en si el Modo Oscuro está activo o no.
        */
        if (isDarkModeActivated(this)) {

            layout_splash_screen.background = AppCompatResources.getDrawable(this, R.drawable.bg_gold)
            image_view_splash_screen.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.circle_apps_studio_gold))

        } else {

            layout_splash_screen.background = AppCompatResources.getDrawable(this, R.drawable.bg)
            image_view_splash_screen.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.circle_apps_studio_logo))
        }

    }

    private fun goToLoadingActivity() {
        /*
            Método encargado de navegar hacia la Activity "LoadingActivity".
        */
        Handler().postDelayed({

            val intent = Intent(this, LoadingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }, 2000)

    }

}