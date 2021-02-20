// Clase Base para Fragments.

package com.circleappsstudio.mimisa.base

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.ui.UI
import java.util.regex.Pattern

abstract class BaseFragment() : Fragment() {

    /*
        Método encargado de obtener el layout del Fragment.
    */
    protected abstract fun getLayout(): Int

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    fun Context.toast(context: Context = applicationContext, message: String?,
                      duration: Int = Toast.LENGTH_SHORT) {
        /*
            Método encargado de mostrar un Toast.
        */
        Toast.makeText(context, message, duration).show()
    }

    fun isOnline(context: Context?): Boolean {
        /*
            Método encargado de detectar la conexión a internet dentro de los fragments.
        */
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

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

    fun isOnlineDialog(buttonListener: UI.IsOnlineDialogClickButtonListener) {
        /*
            Método encargado de mostrar un Dialog cuando no hay conexión a internet.
        */
        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }

        builder!!.setTitle(getString(R.string.no_internet))
        builder.setMessage(getString(R.string.verify_internet))

        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_wifi_off)

        builder.apply {
            setPositiveButton(getString(R.string.try_again)) { dialog, id ->
                buttonListener.isOnlineDialogPositiveButtonClicked()
            }
        }

        val dialog: AlertDialog? = builder.create()

        dialog!!.show()

    }

    fun isSeatReservationAvailableDialog(buttonListenerSeatReservation:
                                         UI.IsSeatReservationAvailableDialogClickButtonListener) {
        /*
            Método encargado de mostrar un Dialog cuando no hay conexión a internet.
        */
        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }

        builder!!.setMessage(getString(R.string.seat_reservation_is_disabled))

        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_info)

        builder.apply {
            setPositiveButton(getString(R.string.to_accept)) { dialog, id ->
                buttonListenerSeatReservation.isSeatReservationAvailablePositiveButtonClicked()
            }
        }

        val dialog: AlertDialog? = builder.create()

        dialog!!.show()

    }

    fun isRegisterIntentionAvailableDialog(buttonListenerRegisterIntention:
                                         UI.IsRegisterIntentionAvailableDialogClickButtonListener) {
        /*
            Método encargado de mostrar un Dialog cuando no hay conexión a internet.
        */
        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }

        builder!!.setMessage(getString(R.string.register_intention_is_disabled))

        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_info)

        builder.apply {
            setPositiveButton(getString(R.string.to_accept)) { dialog, id ->
                buttonListenerRegisterIntention.isRegisterIntentionAvailablePositiveButtonClicked()
            }
        }

        val dialog: AlertDialog? = builder.create()

        dialog!!.show()

    }


    fun confirmDialog(
            buttonListener: UI.ConfirmDialogClickButtonListener,
            message: String
    ): AlertDialog? {
        /*
            Método encargado de mostrar un Dialog cuando no hay conexión a internet.
        */
        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }

        builder!!.setMessage(message)

        builder.setCancelable(true)
        builder.setIcon(R.drawable.ic_info)

        builder.apply {

            setPositiveButton(getString(R.string.to_accept)) { dialog, id ->
                buttonListener.confirmPositiveButtonClicked()
            }

            setNegativeButton(getString(R.string.to_cancel)) { dialog, id ->
                buttonListener.confirmNegativeButtonClicked()
            }

        }

        val dialog: AlertDialog? = builder.create()

        dialog!!.show()

        return dialog

    }

    fun reserveSeatDialog(
            buttonListener: UI.ReserveSeatDialogClickButtonListener,
            message: String
    ): AlertDialog? {
        /*
            Método encargado de mostrar un Dialog cuando no hay conexión a internet.
        */
        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }

        builder!!.setMessage(message)

        builder.setCancelable(true)
        builder.setIcon(R.drawable.ic_info)

        builder.apply {

            setPositiveButton("Sí") { dialog, id ->
                buttonListener.reserveSeatPositiveButtonClicked()
            }

            setNegativeButton("No") { dialog, id ->
                buttonListener.reserveSeatNegativeButtonClicked()
            }

        }

        val dialog: AlertDialog? = builder.create()

        dialog!!.show()

        return dialog

    }

    fun hideKeyboard(){
        /*
            Método encargado de ocultar el teclado de la pantalla.
        */
        val imm: InputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE)
                as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)

    }

}