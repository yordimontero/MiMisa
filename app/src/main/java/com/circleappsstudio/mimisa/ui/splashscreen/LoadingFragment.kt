package com.circleappsstudio.mimisa.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.auth.AuthDataSource
import com.circleappsstudio.mimisa.data.datasource.roleuser.RoleDataSource
import com.circleappsstudio.mimisa.domain.auth.AuthRepository
import com.circleappsstudio.mimisa.domain.roleuser.RoleUserRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.auth.LogInActivity
import com.circleappsstudio.mimisa.ui.main.MainActivity
import com.circleappsstudio.mimisa.ui.main.admin.AdminMainActivity
import com.circleappsstudio.mimisa.ui.viewmodel.auth.AuthViewModel
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAdmin
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAuth
import com.circleappsstudio.mimisa.ui.viewmodel.roleuser.RoleUserViewModel
import com.circleappsstudio.mimisa.vo.Resource

class LoadingFragment : BaseFragment(), UI.SplashScreen {

    private lateinit var navController: NavController

    private val authViewModel by lazy {
        ViewModelProvider(
            this, VMFactoryAuth(
                AuthRepository(
                    AuthDataSource()
                )
            )
        ).get(AuthViewModel::class.java)
    }

    private val adminViewModel by activityViewModels<RoleUserViewModel> {
        VMFactoryAdmin(
            RoleUserRepository(
                RoleDataSource()
            )
        )
    }

    private val emailUser by lazy { authViewModel.getEmailUser() }

    override fun getLayout(): Int {
        return R.layout.fragment_loading
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        checkUserLogged()

    }

    override fun checkUserLogged() {
        /*
             Método encargado de validar que exista actualmente un usuario loggeado en el sistema.
        */
        if (authViewModel.checkUserLogged()) {
            goToSignIn()
        } else {
            checkCreatedAdminByEmailUserObserver()
        }

    }

    override fun goToSignIn() {
        /*
            Método para navegar hacia el fragment de registro de usuarios.
        */
        /*val intent = Intent(requireContext(), LogInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)*/

        navController.navigate(R.id.signInFragment)

    }

    override fun goToMainActivity() {
        /*
            Método para navegar hacia el menú principal.
        */
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    override fun goToAdminMainActivity() {

        val intent = Intent(requireContext(), AdminMainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    override fun checkCreatedAdminByEmailUserObserver() {

        adminViewModel.checkCreatedAdminByEmailUser(emailUser)
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted) {

                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {

                        if (resultEmitted.data) {
                            goToAdminMainActivity()
                            //goToMainActivity()
                        } else {
                            goToMainActivity()
                        }

                    }

                    is Resource.Failure -> {
                        requireContext().toast(requireContext(), resultEmitted.exception.message.toString())
                    }

                }

            })

    }

}