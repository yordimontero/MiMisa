package com.circleappsstudio.mimisa.ui.main.profileuser

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.auth.AuthDataSource
import com.circleappsstudio.mimisa.data.datasource.roleuser.RoleUserDataSource
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
import kotlinx.android.synthetic.main.fragment_profile_user.*

class ProfileUserFragment : BaseFragment(),
        UI.UserProfile,
        UI.IsOnlineDialogClickButtonListener,
        UI.ConfirmDialogClickButtonListener {

    private lateinit var navController: NavController

    private val adminViewModel by activityViewModels<RoleUserViewModel> {
        VMFactoryAdmin(
            RoleUserRepository(
                RoleUserDataSource()
            )
        )
    }

    private val authViewModel by activityViewModels<AuthViewModel> {
        VMFactoryAuth(
            AuthRepository(
                AuthDataSource()
            )
        )
    }

    private val emailUser by lazy { authViewModel.getEmailUser() }
    private val nameUser by lazy { authViewModel.getUserName() }

    private lateinit var fetchedAdminCode: String
    private lateinit var adminCode: String

    private var isAdmin = false

    override fun getLayout(): Int = R.layout.fragment_profile_user

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        fetchData()

        addListenerRadioButtons()

        showChangeRoleLayout()

        hideChangeRoleLayout()

        changeRole()

        logOut()

    }

    override fun fetchData() {

        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
            return
        }

        setCheckedRadioButton()

        fetchAdminCodeObserver()

    }

    override fun setCheckedRadioButton() {
        /*
            Método encargado de verificar el rol del usuario actual registrado,
            y con base en su rol, marcar el RadioButton correspondiente.
        */
        if (isOnline(requireContext())) {

            adminViewModel.checkCreatedAdminByEmailUser(emailUser)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                isAdmin = resultEmitted.data

                                showUserInfo(isAdmin)

                                if (isAdmin) {
                                    rd_btn_admin_role.isChecked = true
                                    hideProgressBar()
                                } else {
                                    rd_btn_user_role.isChecked = true
                                    hideProgressBar()
                                }

                            }

                            is Resource.Failure -> {
                                showMessage(resultEmitted.exception.message.toString(), 2)
                                hideProgressBar()
                            }

                        }

                    })

        }

    }

    override fun showUserInfo(resultEmitted: Boolean) {
        /*
            Método encargado de traer el nombre, el e-mail y el rol del usuario actual registrado.
        */
        txt_name_user_profile_user.text = nameUser
        txt_email_user_profile_user.text = emailUser

        if (resultEmitted) {
            txt_role_user_profile_user.text = getString(R.string.admin)
        } else {
            txt_role_user_profile_user.text = getString(R.string.user)
        }

    }

    override fun fetchAdminCodeObserver() {
        /*
            Método encargado de traer el código de verificación para crear el rol de Administrador.
        */
        if (isOnline(requireContext())) {

            adminViewModel.fetchAdminCode()
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {
                                fetchedAdminCode = resultEmitted.data.toString()
                                hideProgressBar()
                            }

                            is Resource.Failure -> {
                                showMessage(resultEmitted.exception.message.toString(), 2)
                                hideProgressBar()
                            }

                        }

                    })

        }

    }

    override fun changeRole() {
        /*
            Método encargado de cambiar el rol del usuario en función de cúal RadioButton está
            seleccionado.
        */
        btn_change_role.setOnClickListener {

            hideKeyboard()

            adminCode = txt_admin_code.text.toString()

            if (!isOnline(requireContext())) {
                showIsOnlineDialog()
                return@setOnClickListener
            }

            if (rd_btn_user_role.isChecked) {
                showConfirmDialog()
            }

            if (adminViewModel.checkEmptyAdminCode(adminCode)) {
                txt_admin_code.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (adminViewModel.validateAdminCode(fetchedAdminCode, adminCode)) {
                txt_admin_code.error = getString(R.string.wrong_admin_code)
                return@setOnClickListener
            }

            if (rd_btn_admin_role.isChecked){
                showConfirmDialog()
            }

        }

    }

    override fun checkCreatedAdminByEmailUserObserver() {
        /*
            Método encargado de verificar si el usuario ya es un Administrador.
        */
        if (isOnline(requireContext())) {

            adminViewModel.checkCreatedAdminByEmailUser(emailUser)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                if (resultEmitted.data) {
                                    showMessage(getString(R.string.you_are_an_admin), 2)
                                    hideProgressBar()
                                } else {
                                    createAdminObserver()
                                }

                            }

                            is Resource.Failure -> {
                                showMessage(resultEmitted.exception.message.toString(), 2)
                                hideProgressBar()
                            }

                        }

                    })

        }

    }

    override fun createAdminObserver() {
        /*
            Método encargado de crear el rol de Administrador.
        */
        if (isOnline(requireContext())) {

            adminViewModel.createAdmin(emailUser, nameUser)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted){

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {
                                showMessage(getString(R.string.now_you_are_an_admin), 2)
                                hideProgressBar()
                                goToAdminMainActivity()
                            }

                            is Resource.Failure -> {
                                showMessage(resultEmitted.exception.message.toString(), 2)
                                hideProgressBar()
                            }

                        }

                    })

        }

    }

    override fun deleteAdminObserver() {
        /*
            Método encargado de eliminar el rol de Administrador. (Crear rol de Usuario).
        */
        if (isOnline(requireContext())) {

            if (isAdmin) {

                adminViewModel.deleteAdmin(emailUser)
                        .observe(viewLifecycleOwner, Observer { resultEmitted ->

                            when(resultEmitted) {

                                is Resource.Loading -> {
                                    showProgressBar()
                                }

                                is Resource.Success -> {
                                    showMessage(getString(R.string.now_you_are_an_user), 2)
                                    hideProgressBar()
                                    goToMainActivity()
                                }

                                is Resource.Failure -> {
                                    showMessage(resultEmitted.exception.message.toString(), 2)
                                    hideProgressBar()
                                }

                            }

                        })

            } else {
                showMessage(getString(R.string.you_are_an_user), 2)
            }

        }

    }

    override fun addListenerRadioButtons() {
        /*
            Método encargado de escuchar cuál RadioButton está seleccionado.
        */
        radiogroup_role.setOnCheckedChangeListener { radioGroup, i ->

            when(i){

                R.id.rd_btn_user_role -> {
                    hideEditText()
                }

                R.id.rd_btn_admin_role -> {
                    showEditText()
                }

            }

        }

    }

    override fun logOut() {
        /*
             Método encargado de cerrar la sesión de un usuario existente en el sistema.
        */
        btn_log_out_profile_user.setOnClickListener {

            authViewModel.logOutUser()
            goToSignIn()

        }

    }

    override fun goToMainActivity() {
        /*
            Método para navegar hacia el menú principal del rol de Usuario.
        */
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    override fun goToAdminMainActivity() {
        /*
            Método para navegar hacia el menú principal del rol de Administrador.
        */
        val intent = Intent(requireContext(), AdminMainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    override fun goToSignIn() {
        /*
            Método para navegar hacia la Activity "LogInActivity".
        */
        val intent = Intent(requireContext(), LogInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    override fun showMessage(message: String, duration: Int) {
        /*
            Método encargado de mostrar un Toast.
        */
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        /*
            Método encargado de mostrar un ProgressBar.
        */
        progressbar_change_role.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un ProgressBar.
        */
        progressbar_change_role.visibility = View.GONE
    }

    override fun showEditText() {
        /*
            Método encargado de mostrar el EditText del código de
            verificación de creación de rol Administrador.
        */
        tetx_imput_txt_admin_code.visibility = View.VISIBLE
    }

    override fun hideEditText() {
        /*
            Método encargado de ocultar el EditText del código de
            verificación de creación de rol Administrador.
        */
        tetx_imput_txt_admin_code.visibility = View.GONE
    }

    override fun showChangeRoleLayout() {
        /*
            Método encargado de mostrar el layout de cambiar rol de usuario.
        */
        btn_show_change_role_layout_profile_user.setOnClickListener {

            hideKeyboard()

            layout_change_role.visibility = View.VISIBLE
            layout_profile_user.visibility = View.GONE

        }

    }

    override fun hideChangeRoleLayout() {
        /*
            Método encargado de ocultar el layout de cambiar rol de usuario.
        */
        btn_cancel_change_role.setOnClickListener {

            hideKeyboard()

            layout_change_role.visibility = View.GONE
            layout_profile_user.visibility = View.VISIBLE

        }

    }

    override fun showIsOnlineDialog() {
        /*
            Método encargado de mostrar el Dialog "isOnlineDialog".
        */
        isOnlineDialog(this)
    }

    override fun isOnlineDialogPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog "isOnlineDialog".
        */
        if (isOnline(requireContext())) {
            fetchData()
        } else {
            showIsOnlineDialog()
        }

    }

    override fun showConfirmDialog(): AlertDialog? {
        /*
            Método encargado de mostrar el Dialog "confirmDialog".
        */
        return confirmDialog(this, "¿Desea cambiar su rol?")
    }

    override fun confirmPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog "confirmDialog".
        */
        if (rd_btn_user_role.isChecked) {
            deleteAdminObserver()
            return
        }

        if (rd_btn_admin_role.isChecked){
            checkCreatedAdminByEmailUserObserver()
            return
        }

    }

    override fun confirmNegativeButtonClicked() {
        /*
            Método encargado de controlar el botón negativo del Dialog "confirmDialog".
        */
        showConfirmDialog()!!.dismiss()
    }

}