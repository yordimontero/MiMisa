// Clase Base para Fragments.

package com.circleappsstudio.mimisa.base

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.circleappsstudio.mimisa.R
import java.lang.Exception

abstract class BaseFragment : Fragment() {

    // Método para obtener el layout del Fragment.
    protected abstract fun getLayout() : Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(),container, false)
    }

    fun Context.toast(context: Context = applicationContext, message: String?,
                      duration: Int = Toast.LENGTH_SHORT) {
        /*
            Método para mostrar un Toast.
        */
        Toast.makeText(context, message, duration).show()
    }

    fun isOnline(context: Context?): Boolean {
        /*
            Método para detectar la conexión a internet dentro de los fragments.
        */

        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connectivityManager != null) {

            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            if (capabilities != null) {

                when {

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        //Hay conexión 2G, 3G o 4G.
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        //Hay conexión WiFi.
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        //Hay conexión Ethernet.
                        return true
                    }

                }

            }

        }

        return false
    }

}