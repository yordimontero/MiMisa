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
import kotlinx.android.synthetic.main.fragment_profile_user.*

class ProfileUserFragment : BaseFragment(),
        UI.UserProfile,
        UI.IsOnlineDialogClickButtonListener,
        UI.ConfirmDialogClickButtonListener {

    private lateinit var navController: NavController

    private val adminViewModel by activityViewModels<RoleUserViewModel> {
        VMFactoryAdmin(
            RoleUserRepository(
                RoleDataSource()
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

    override fun getLayout(): Int {
        return R.layout.fragment_profile_user
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        addListenerRadioButtons()

        fetchData()

        showChangeRoleLayout()

        hideChangeRoleLayout()

        changeRole()

        logOut()

    }

    override fun setCheckedRadioButton() {

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

    override fun fetchData() {

        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
            showProgressBar()
            return
        }

        setCheckedRadioButton()

        fetchAdminCodeObserver()

        //showUserInfo()

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

    override fun showUserInfo(resultEmitted: Boolean) {
        /*
            Método encargado de traer el nombre y el e-mail del usuario autenticado.
        */
        txt_name_user_profile_user.text = nameUser
        txt_email_user_profile_user.text = emailUser

        if (resultEmitted) {
            txt_role_user_profile_user.text = "Administrador"
        } else {
            txt_role_user_profile_user.text = "Usuario"
        }

    }

    override fun changeRole() {
        /*
            Método encargado de cambiar el rol del usuario en función de cúal RadioButton está
            seleccionado.
        */
        btn_change_role.setOnClickListener {

            /*adminCode = txt_admin_code.text.toString()

            if (!isOnline(requireContext())) {
                showDialog()
                return@setOnClickListener
            }

            if (rd_btn_user_role.isChecked) {
                deleteAdminObserver()
                return@setOnClickListener
            }

            if (adminViewModel.checkEmptyAdminCode(adminCode)) {
                txt_admin_code.error = "Complete los campos"
                return@setOnClickListener
            }

            if (adminViewModel.validateAdminCode(fetchedAdminCode, adminCode)) {
                txt_admin_code.error = "El código ingresado es incorrecto."
                return@setOnClickListener
            }

            if (rd_btn_admin_role.isChecked){
                checkCreatedAdminByEmailUserObserver()
                return@setOnClickListener
            }*/

            adminCode = txt_admin_code.text.toString()

            if (!isOnline(requireContext())) {
                showIsOnlineDialog()
                return@setOnClickListener
            }

            if (rd_btn_user_role.isChecked) {
                //deleteAdminObserver()
                //return@setOnClickListener
                showConfirmDialog()
            }

            if (adminViewModel.checkEmptyAdminCode(adminCode)) {
                txt_admin_code.error = "Complete los campos"
                return@setOnClickListener
            }

            if (adminViewModel.validateAdminCode(fetchedAdminCode, adminCode)) {
                txt_admin_code.error = "El código ingresado es incorrecto."
                return@setOnClickListener
            }

            if (rd_btn_admin_role.isChecked){
                //checkCreatedAdminByEmailUserObserver()
                //return@setOnClickListener
                showConfirmDialog()
            }

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
                                    showMessage("Ahora eres Usuario.", 2)
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
                showMessage("Ya eres un Usuario", 1)
            }

        }

    }

    override fun checkCreatedAdminByEmailUserObserver() {
        /*
            Método encargado de verificar que el código de verificación
            para crear el rol de Administrador sea correcto.
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
                                showMessage("Ya eres un Administrador.", 1)
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
                            showMessage("Ahora eres Administrador.", 2)
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
            layout_change_role.visibility = View.VISIBLE
            layout_profile_user.visibility = View.GONE
        }

    }

    override fun hideChangeRoleLayout() {
        /*
            Método encargado de ocultar el layout de cambiar rol de usuario.
        */
        btn_cancel_change_role.setOnClickListener {
            layout_change_role.visibility = View.GONE
            layout_profile_user.visibility = View.VISIBLE
        }

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

    override fun logOut() {
        btn_log_out_profile_user.setOnClickListener {
            authViewModel.logOutUser()
            goToSignIn()
        }
    }

    override fun goToSignIn() {

        val intent = Intent(requireContext(), LogInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    override fun showIsOnlineDialog() {
        /*
            Método encargado de mostrar un Dialog.
        */
        isOnlineDialog(this)
    }

    override fun showConfirmDialog(): AlertDialog? {
        return confirmDialog(this, "¿Desea cambiar su rol?")
    }

    override fun isOnlineDialogPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog.
        */
        if (isOnline(requireContext())) {
            fetchData()
        } else {
            showIsOnlineDialog()
        }

    }

    override fun confirmPositiveButtonClicked() {

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
        showConfirmDialog()!!.dismiss()
    }

}