package com.circleappsstudio.mimisa.ui.main.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.ui.UI
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : BaseFragment(), UI.More {

    private lateinit var navController: NavController

    override fun getLayout(): Int = R.layout.fragment_more

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        links()

        goToProfile()

    }

    override fun links() {

        goToFacebook()
        goToTwitter()
        goToWebPage()
        goToUserManual()
        sendEmail()
        goToPrivacyPolicy()
        rateApp()
        goToPlayStoreMoreApps()

    }

    override fun goToProfile() {
        /*
            Método encargado de navegar hacia el fragment "ProfileUser".
        */
        btn_go_to_profile.setOnClickListener {
            navController.navigate(R.id.action_go_to_profile_user_fragment_from_more_fragment)
        }

    }

    override fun goToFacebook() {
        /*
            Método encargado de navegar hacia la URL del Facebook de Circle Apps Studio.
        */
        btn_facebook.setOnClickListener {
            val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/CircleAppsStudio/")
            )
            requireContext().startActivity(intent)
        }

    }

    override fun goToTwitter() {
        /*
            Método encargado de navegar hacia la URL del Twitter de Circle Apps Studio.
        */

        btn_twitter.setOnClickListener {
            val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/CircleApps_")
            )
            requireContext().startActivity(intent)
        }

    }

    override fun goToWebPage() {
        /*
            Método encargado de navegar hacia la URL de la página web de Circle Apps Studio.
        */

        btn_web_page.setOnClickListener {
            val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://circleappsstudio.com")
            )
            requireContext().startActivity(intent)
        }

    }

    override fun goToUserManual() {
        /*
            Método encargado de navegar hacia la URL del manual de usuario del sistema.
        */

        btn_user_manual.setOnClickListener {
            val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://circleappsstudio.com")
            )
            requireContext().startActivity(intent)
        }

    }

    override fun sendEmail() {
        /*
            Método encargado de crear un mensaje al correo electrónico "soporte.circleappsstudio@gmail.com".
        */
        btn_send_email.setOnClickListener {

            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:soporte.circleappsstudio@gmail.com"))
            startActivity(Intent.createChooser(emailIntent, "Chooser title"))

        }

    }

    override fun goToPrivacyPolicy() {
        /*
            Método encargado de navegar hacia la URL de la política de privacidad de Circle Apps Studio.
        */

        btn_privacy_policy.setOnClickListener {
            val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://circleappsstudio.com/inicio/politicaprivacidad")
            )
            requireContext().startActivity(intent)
        }

    }

    override fun rateApp() {
        /*
            Método encargado de navegar hacia la ficha de @packagename en Google Play Store.
        */

        btn_rate_app.setOnClickListener {
            val packageName: String = requireContext().packageName
            val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
            requireContext().startActivity(intent)
        }

    }

    override fun goToPlayStoreMoreApps() {
        /*
            Método encargado de navegar hacia la página principal de Circle Apps Studio en Google Play Store.
        */

        btn_more_apps.setOnClickListener {
            val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/dev?id=5626559995007331377")
            )
            requireContext().startActivity(intent)
        }

    }

}