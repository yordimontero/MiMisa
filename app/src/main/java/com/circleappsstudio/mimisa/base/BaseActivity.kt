// Clase Base para Activities.

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
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.ui.UI

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

    fun isOnlineDialog(buttonListener: UI.IsOnlineDialogClickButtonListener, context: Context) {
        /*
            Método encargado de mostrar un Dialog cuando no hay conexión a internet.
        */
        val builder: AlertDialog.Builder? = context.let {
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

    fun updateAppDialog(buttonListener: UI.UpdateAppDialogClickButtonListener): AlertDialog? {
        /*
            Método encargado de mostrar un Dialog cuando no hay conexión a internet.
        */
        val builder: AlertDialog.Builder? = this.let {
            AlertDialog.Builder(it)
        }

        builder!!.setTitle("¡Hay una nueva actualización disponible!")

        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_new_releases)

        builder.apply {

            setPositiveButton("Actualizar ahora") { dialog, id ->
                buttonListener.updateAppPositiveButtonClicked()
            }

            setNegativeButton("No, gracias") { dialog, id ->
                buttonListener.updateAppNegativeButtonClicked()
            }

        }

        val dialog: AlertDialog? = builder.create()

        dialog!!.show()

        return dialog

    }

    fun fetchCurrentVersionCode(): Int {

        var currentVersionCode = 0

        try {

            val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)

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

        val packageName: String = this.packageName

        val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        )

        this.startActivity(intent)

    }

    fun hideKeyboard(){
        /*
            Método encargado de ocultar el teclado de la pantalla.
        */
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

    }

}