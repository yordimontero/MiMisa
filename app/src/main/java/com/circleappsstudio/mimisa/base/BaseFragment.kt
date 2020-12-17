// Clase Base para Fragments.

package com.circleappsstudio.mimisa.base

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
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.ui.UI

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

    fun isOnlineDialog(buttonListener: UI.IsOnlineDialogClickButtonListener) {
        /*
            Método encargado de mostrar un Dialog cuando no hay conexión a internet.
        */
        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }

        builder!!.setTitle("¡No hay conexión a Internet!")
        builder.setMessage("Verifique su conexión e inténtelo de nuevo.")

        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_wifi_off)

        builder.apply {
            setPositiveButton("Intentar de nuevo") { dialog, id ->
                buttonListener.isOnlineDialogPositiveButtonClicked()
            }
        }

        val dialog: AlertDialog? = builder.create()

        dialog!!.show()

    }

    fun isAvailableDialog(buttonListener: UI.IsAvailableDialogClickButtonListener) {
        /*
            Método encargado de mostrar un Dialog cuando no hay conexión a internet.
        */
        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }

        builder!!.setTitle("¡El sistema no está disponible!")
        builder.setMessage(
            "Horario de disponibilidad:\n\n" +
                    "Miércoles:" +
                    "\n9 a.m a 12 m.d." +
                    "\n1 p.m. a 9 p.m.\n\n" +
                    "Jueves:\n" +
                    "7 p.m. a 10 p.m."
        )

        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_info)

        builder.apply {
            setPositiveButton("Aceptar") { dialog, id ->
                buttonListener.isAvailablePositiveButtonClicked()
            }
        }

        val dialog: AlertDialog? = builder.create()

        dialog!!.show()

    }

    fun updateAppDialog(buttonListener: UI.UpdateAppDialogClickButtonListener) {
        /*
            Método encargado de mostrar un Dialog cuando no hay conexión a internet.
        */
        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }

        builder!!.setTitle("¡Hay una nueva actualización disponible!")
        //builder.setMessage("Verifique su conexión e inténtelo de nuevo.")

        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_new_releases)

        builder.apply {

            setPositiveButton("Actualizar ahora.") { dialog, id ->
                buttonListener.updateAppPositiveButtonClicked()
            }

            setNegativeButton("No, gracias.") { dialog, id ->
                buttonListener.updateAppNegativeButtonClicked()
            }

        }

        val dialog: AlertDialog? = builder.create()

        dialog!!.show()

    }

    fun confirmDialog(
            buttonListener: UI.ConfirmDialogClickButtonListener,
            title: String
    ): AlertDialog? {
        /*
            Método encargado de mostrar un Dialog cuando no hay conexión a internet.
        */
        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }

        builder!!.setTitle(title)
        //builder.setMessage("Verifique su conexión e inténtelo de nuevo.")

        builder.setCancelable(true)
        builder.setIcon(R.drawable.ic_info)

        builder.apply {

            setPositiveButton("Aceptar.") { dialog, id ->
                buttonListener.confirmPositiveButtonClicked()
            }

            setNegativeButton("Cancelar.") { dialog, id ->
                buttonListener.confirmNegativeButtonClicked()
            }

        }

        val dialog: AlertDialog? = builder.create()

        dialog!!.show()

        return dialog

    }

    fun fetchCurrentVersionCode(): Int {

        var currentVersionCode = 0

        try {

            val packageInfo = requireContext().packageManager.getPackageInfo(requireActivity().packageName, 0)

            currentVersionCode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toInt()
            } else {
                packageInfo.versionCode
            }

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return currentVersionCode

    }

    fun goToPlayStore() {

        val packageName: String = requireContext().packageName

        val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        )

        requireContext().startActivity(intent)

    }

}