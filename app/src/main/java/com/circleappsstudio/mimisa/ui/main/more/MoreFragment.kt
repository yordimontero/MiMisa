package com.circleappsstudio.mimisa.ui.main.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.auth.AuthDataSource
import com.circleappsstudio.mimisa.domain.auth.AuthRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.auth.LogInActivity
import com.circleappsstudio.mimisa.ui.viewmodel.auth.AuthViewModel
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAuth
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : BaseFragment(), UI.More {

    private lateinit var navController: NavController

    private val authViewModel by activityViewModels<AuthViewModel>{
        VMFactoryAuth(
                AuthRepository(
                        AuthDataSource()
                )
        )
    }

    override fun getLayout(): Int {
        return R.layout.fragment_more
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        logOut()

        fetchUserName()

        links()

    }

    override fun logOut() {

        btn_log_out.setOnClickListener {
            authViewModel.logOutUser()
            goToSignIn()
        }

    }

    override fun fetchUserName() {
        txt_user_name_more.text = authViewModel.getUserName()
    }

    override fun links() {

        goToFacebook()
        goToTwitter()
        goToWebPage()
        goToPrivacyPolicy()
        rateApp()
        goToPlayStoreMoreApps()

    }

    override fun goToFacebook() {
        /**
            Método para navegar hacia la URL del Facebook de Circle Apps Studio.
         */
        btn_facebook.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/CircleAppsStudio/"))
            requireContext().startActivity(intent)
        }

    }

    override fun goToTwitter() {
        /**
            Método para navegar hacia la URL del Twitter de Circle Apps Studio.
         */

        btn_twitter.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/CircleApps_"))
            requireContext().startActivity(intent)
        }

    }

    override fun goToWebPage() {
        /**
            Método para navegar hacia la URL de la página web de Circle Apps Studio.
         */

        btn_web_page.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://circleappsstudio.com"))
            requireContext().startActivity(intent)
        }

    }

    override fun goToPrivacyPolicy() {
        /**
            Método para navegar hacia la URL de la política de privacidad de Circle Apps Studio.
         */

        btn_privacy_policy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://circleappsstudio.com/inicio/politicaprivacidad"))
            requireContext().startActivity(intent)
        }

    }

    override fun rateApp() {
        /**
            Método para navegar hacia la ficha de @packagename en Google Play Store.
         */

        btn_rate_app.setOnClickListener {
            val packageName: String = requireContext().packageName
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            requireContext().startActivity(intent)
        }

    }

    override fun goToPlayStoreMoreApps() {
        /**
            Método para navegar hacia la página principal de Circle Apps Studio en Google Play Store.
         */

        btn_more_apps.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=5626559995007331377"))
            requireContext().startActivity(intent)
        }

    }

    override fun goToSignIn() {
        val intent = Intent(requireContext(), LogInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

}