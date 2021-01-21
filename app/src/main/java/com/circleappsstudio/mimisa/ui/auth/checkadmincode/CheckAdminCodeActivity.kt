package com.circleappsstudio.mimisa.ui.auth.checkadmincode

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseActivity
import com.circleappsstudio.mimisa.data.datasource.roleuser.RoleUserDataSource
import com.circleappsstudio.mimisa.domain.roleuser.RoleUserRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.main.admin.AdminMainActivity
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAdmin
import com.circleappsstudio.mimisa.ui.viewmodel.roleuser.RoleUserViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.activity_check_admin_code.*

class CheckAdminCodeActivity : BaseActivity(),
    UI.CheckAdminCode,
    UI.IsOnlineDialogClickButtonListener {

    private val roleUserViewModel by lazy {
        ViewModelProvider(
            this,
            VMFactoryAdmin(
                RoleUserRepository(
                    RoleUserDataSource()
                )
            )
        ).get(RoleUserViewModel::class.java)
    }

    private lateinit var fetchedAdminCode: String
    private var adminCode: String = ""

    override fun getLayout(): Int = R.layout.activity_check_admin_code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchData()

        verifyAdminCode()

    }

    override fun fetchData() {

        if (!isOnline(this)) {
            showIsOnlineDialog()
            return
        }

        fetchAdminCodeObserver()

    }

    override fun fetchAdminCodeObserver() {
        /*
            Método encargado de traer el código de seguridad de rol Administrador al ingresar al sistema.
        */
        if (isOnline(this)) {

            roleUserViewModel.fetchAdminCode()
                .observe(this, Observer { resultEmitted ->

                    when(resultEmitted) {

                        is Resource.Loading -> {
                            showProgressBar()
                        }

                        is Resource.Success -> {
                            fetchedAdminCode = resultEmitted.data!!
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

    override fun verifyAdminCode() {

        btn_verify_admin_code.setOnClickListener {

            hideKeyboard()

            adminCode = txt_admin_code_check_dev_code.text.toString().trim()

            if (!isOnline(this)) {
                showIsOnlineDialog()
                return@setOnClickListener
            }

            if (roleUserViewModel.checkEmptyAdminCode(adminCode)) {
                txt_admin_code_check_dev_code.error = this.getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (roleUserViewModel.validateAdminCode(fetchedAdminCode, adminCode)) {
                txt_admin_code_check_dev_code.error = this.getString(R.string.wrong_admin_code)
                return@setOnClickListener
            }

            goToAdminMainActivity()

        }

    }

    override fun goToAdminMainActivity() {
        /*
            Método para navegar hacia el menú principal en rol de Administrador.
        */
        val intent = Intent(this, AdminMainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    override fun showMessage(message: String, duration: Int) {
        /*
            Método encargado de mostrar un Toast.
        */
        this.toast(this, message, duration)
    }

    override fun showProgressBar() {
        /*
            Método encargado de mostrar un progress bar.
        */
        progressbar_check_admin_code.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un progress bar.
        */
        progressbar_check_admin_code.visibility = View.GONE
    }

    override fun showIsOnlineDialog() {
        /*
            Método encargado de mostrar un Dialog.
        */
        isOnlineDialog(this, this)
    }

    override fun isOnlineDialogPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog IsOnlineDialog.
        */

        if (isOnline(this)) {
            fetchAdminCodeObserver()
        } else {
            showIsOnlineDialog()
        }

    }

}