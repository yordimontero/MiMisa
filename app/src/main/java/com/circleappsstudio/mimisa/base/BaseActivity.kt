// Clase Base para Activities.

package com.circleappsstudio.mimisa.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    /*
        Método encargado de obtener el layout del Fragment.
    */
    protected abstract fun getLayout(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
    }

    fun Context.toast(context: Context = applicationContext, message: String?,
        duration: Int = Toast.LENGTH_SHORT) {
        /*
            Método encargado de mostrar un Toast.
        */
        Toast.makeText(context, message, duration).show()
    }

}