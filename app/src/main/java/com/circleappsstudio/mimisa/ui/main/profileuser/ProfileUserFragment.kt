package com.circleappsstudio.mimisa.ui.main.profileuser

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.auth.AuthDataSource
import com.circleappsstudio.mimisa.data.datasource.user.admin.AdminDataSource
import com.circleappsstudio.mimisa.domain.auth.AuthRepository
import com.circleappsstudio.mimisa.domain.user.admin.AdminRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.viewmodel.auth.AuthViewModel
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAdmin
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAuth
import com.circleappsstudio.mimisa.ui.viewmodel.user.admin.AdminViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_profile_user.*

class ProfileUserFragment : BaseFragment(), UI.UserProfile, UI.IsOnlineDialogClickButtonListener {

    private lateinit var navController: NavController

    private val adminViewModel by activityViewModels<AdminViewModel> {
        VMFactoryAdmin(
            AdminRepository(
                AdminDataSource()
            )
        )
    }

    private val authViewModel by activityViewModels<AuthViewModel>{
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

    override fun getLayout(): Int {
        return R.layout.fragment_profile_user
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        showUserInfo()

        fetchAdminCodeObserver()

        addListenerRadioButtons()

        btn_show_change_role_layout_profile_user.setOnClickListener {
            hideProfileLayout()
            showChangeRoleLayout()
        }

        btn_change_role.setOnClickListener {
            changeRole()
        }

        btn_cancel_change_role.setOnClickListener {
            showProfileLayout()
            hideChangeRoleLayout()
        }

    }

    override fun addListenerRadioButtons() {

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

        adminViewModel.fetchAdminCode().observe(viewLifecycleOwner, Observer { resultEmitted ->

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

    override fun changeRole() {

        adminCode = txt_admin_code.text.toString()

        if (!isOnline(requireContext())) {
            showDialog()
            return
        }

        if (rd_btn_user_role.isChecked) {
            deleteAdminObserver()
            return
        }

        if (adminViewModel.checkEmptyAdminCode(adminCode)) {
            txt_admin_code.error = "Complete los campos"
            return
        }

        if (adminViewModel.validateAdminCode(fetchedAdminCode, adminCode)) {
            txt_admin_code.error = "El código ingresado es incorrecto."
            return
        }

        if (rd_btn_admin_role.isChecked){
            checkCreatedAdminByEmailUserObserver()
            return
        }

    }

    override fun createAdminObserver() {

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
                        }

                        is Resource.Failure -> {
                            showMessage(resultEmitted.exception.message.toString(), 2)
                            hideProgressBar()
                        }

                    }

                })

        }

    }

    override fun showUserInfo() {
        txt_name_user_profile_user.text = nameUser
        txt_email_user_profile_user.text = emailUser
    }

    override fun showMessage(message: String, duration: Int) {
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        progressbar_change_role.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressbar_change_role.visibility = View.GONE
    }

    override fun showEditText() {
        tetx_imput_txt_admin_code.visibility = View.VISIBLE
    }

    override fun hideEditText() {
        tetx_imput_txt_admin_code.visibility = View.GONE
    }

    override fun showDialog() {
        /*
            Método encargado de mostrar un Dialog.
        */
        isOnlineDialog(this,
            "¡No hay conexión a Internet!",
            "Verifique su conexión e inténtelo de nuevo.",
            R.drawable.ic_wifi_off,
            "Intentar de nuevo"
        )
    }

    override fun showProfileLayout() {
        layout_profile_user.visibility = View.VISIBLE
    }

    override fun hideProfileLayout() {
        layout_profile_user.visibility = View.GONE
    }

    override fun showChangeRoleLayout() {
        layout_change_role.visibility = View.VISIBLE
    }

    override fun hideChangeRoleLayout() {
        layout_change_role.visibility = View.GONE
    }

    override fun checkCreatedAdminByEmailUserObserver() {

        if (isOnline(requireContext())) {

            adminViewModel.checkCreatedAdminByEmailUser(emailUser)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                if (resultEmitted.data) {
                                    showMessage("Ya eres administrador.", 1)
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

    override fun deleteAdminObserver() {

        if (isOnline(requireContext())) {

            adminViewModel.deleteAdmin(emailUser)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {
                                showMessage("Ahora eres Usuario.", 2)
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

    override fun onPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog.
        */
        if (isOnline(requireContext())) {
            changeRole()
        } else {
            showDialog()
        }

    }

}