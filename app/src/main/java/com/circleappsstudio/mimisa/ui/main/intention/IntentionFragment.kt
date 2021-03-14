package com.circleappsstudio.mimisa.ui.main.intention

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.auth.AuthDataSource
import com.circleappsstudio.mimisa.data.datasource.intention.IntentionDataSource
import com.circleappsstudio.mimisa.data.datasource.roleuser.RoleUserDataSource
import com.circleappsstudio.mimisa.data.model.IntentionSpinner
import com.circleappsstudio.mimisa.data.model.Intentions
import com.circleappsstudio.mimisa.domain.auth.AuthRepository
import com.circleappsstudio.mimisa.domain.intention.IntentionRepository
import com.circleappsstudio.mimisa.domain.roleuser.RoleUserRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.adapter.IntentionSpinnerAdapter
import com.circleappsstudio.mimisa.ui.viewmodel.auth.AuthViewModel
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAdmin
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAuth
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryIntention
import com.circleappsstudio.mimisa.ui.viewmodel.intention.IntentionViewModel
import com.circleappsstudio.mimisa.ui.viewmodel.roleuser.RoleUserViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_intention.*

class IntentionFragment : BaseFragment(),
        UI.Intentions,
        UI.IsOnlineDialogClickButtonListener,
        UI.ConfirmDialogClickButtonListener {

    private lateinit var navController: NavController

    private val intentionViewModel by activityViewModels<IntentionViewModel> {
        VMFactoryIntention(
                IntentionRepository(
                        IntentionDataSource()
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

    private val adminViewModel by activityViewModels<RoleUserViewModel> {
        VMFactoryAdmin(
                RoleUserRepository(
                        RoleUserDataSource()
                )
        )
    }

    private var isAdmin = false

    private val emailUser by lazy { authViewModel.getEmailUser() }

    private lateinit var intentionCategory: String
    private lateinit var intention: String

    override fun getLayout(): Int = R.layout.fragment_intention

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        checkIfUserIsAdmin()

        setUpSpinner()

        saveIntention()

    }

    override fun checkIfUserIsAdmin() {

        if (isOnline(requireContext())) {

            adminViewModel.checkCreatedAdminByEmailUser(emailUser)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                isAdmin = resultEmitted.data
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


    override fun saveIntention() {

        btn_save_intention.setOnClickListener {

            hideKeyboard()

            intention = txt_intention.text.toString()

            if (!isOnline(requireContext())) {
                showIsOnlineDialog()
                return@setOnClickListener
            }

            if (intentionViewModel.checkValidIntentionCategory(intentionCategory)) {
                showMessage(getString(R.string.select_intention_category), 2)
                return@setOnClickListener
            }

            if (intentionViewModel.checkEmptyIntention(intention)) {
                txt_intention.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            showConfirmDialog()

        }

    }

    override fun saveIntentionObserver() {
        /*
            Método encargado de guardar una intención.
        */
        if (isOnline(requireContext())) {

            intentionViewModel.saveIntention(intentionCategory, intention)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted){

                            is Resource.Loading -> { showProgressBar() }

                            is Resource.Success -> {
                                showMessage(getString(R.string.intention_saved_successfully), 2)
                                gotToSeatReservationMain()
                            }

                            is Resource.Failure -> {
                                requireContext().toast(requireContext(), resultEmitted.exception.message.toString())
                                hideProgressBar()
                            }

                        }

                    })

        }

    }

    override fun setUpSpinner() {
        /*
            Método encargado de hacer el setup de un Spinner.
        */
        val adapter = IntentionSpinnerAdapter(requireContext(), Intentions.list!!)
        spinIntentionCategory.adapter = adapter

        spinIntentionCategory.onItemSelectedListener = object : OnItemSelectedListener {

            override fun onItemSelected(
                item: AdapterView<*>?, arg1: View,
                position: Int, id: Long) {

                val spinnerValue = item!!.selectedItem

                getSelectedCategoryFromSpinner(spinnerValue as IntentionSpinner)

            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}

        }

    }

    override fun getSelectedCategoryFromSpinner(intentionSpinnerAdapter: IntentionSpinner) {
        /*
            Método encargado de obtener el valor del item seleccionado en el Spinner.
        */
        intentionCategory = intentionSpinnerAdapter.name

    }

    override fun gotToSeatReservationMain() {
        /*
            Método encargado de navegar hacia el fragment "MainIntentionFragment".
        */
        navController.navigateUp()
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
        progressbar_intention.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un ProgressBar.
        */
        progressbar_intention.visibility = View.GONE
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
        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
        }

    }

    override fun showConfirmDialog(): AlertDialog? {
        /*
            Método encargado de mostrar el Dialog "confirmDialog".
        */
        return confirmDialog(this, getString(R.string.do_you_want_to_register_intention))
    }

    override fun confirmPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog "confirmDialog".
        */
        saveIntentionObserver()
    }

    override fun confirmNegativeButtonClicked() {
        /*
            Método encargado de controlar el botón negativo del Dialog "confirmDialog".
        */
        showConfirmDialog()!!.dismiss()
    }

}