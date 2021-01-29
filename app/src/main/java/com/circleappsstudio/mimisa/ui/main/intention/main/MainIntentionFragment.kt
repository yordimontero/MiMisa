package com.circleappsstudio.mimisa.ui.main.intention.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.intention.IntentionDataSource
import com.circleappsstudio.mimisa.data.datasource.params.ParamsDataSource
import com.circleappsstudio.mimisa.domain.intention.IntentionRepository
import com.circleappsstudio.mimisa.domain.params.ParamsRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.adapter.IntentionAdapter
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryIntention
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryParams
import com.circleappsstudio.mimisa.ui.viewmodel.intention.IntentionViewModel
import com.circleappsstudio.mimisa.ui.viewmodel.params.ParamsViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_main_intention.*

class MainIntentionFragment : BaseFragment(),
        UI.IntentionMain,
        UI.IsOnlineDialogClickButtonListener {

    private lateinit var navController: NavController

    private val intentionViewModel by activityViewModels<IntentionViewModel> {
        VMFactoryIntention(
                IntentionRepository(
                        IntentionDataSource()
                )
        )
    }

    private val paramsViewModel by activityViewModels<ParamsViewModel> {
        VMFactoryParams(
                ParamsRepository(
                        ParamsDataSource()
                )
        )
    }

    private var isRegisterIntentionAvailable = true

    override fun getLayout(): Int = R.layout.fragment_main_intention

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        setUpRecyclerView()

        fetchData()

        goToIntention()

    }

    override fun fetchData() {

        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
            return
        }

        fetchIsRegisterIntentionAvailable()

        fetchSavedIntentionsByNameUserObserver()

    }

    override fun fetchIsRegisterIntentionAvailable() {
        /*
            Método encargado de escuchar en tiempo real si el registro de intenciones esta habilitado
            o deshabilitado.
        */
        if (isOnline(requireContext())) {

            paramsViewModel.fetchIsRegisterIntentionAvailable()
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                isRegisterIntentionAvailable = resultEmitted.data

                                if (!isRegisterIntentionAvailable) {

                                    showInfoMessage()
                                    changeTextViewToDisabledSeatReservation()
                                    hideButton()

                                } else {

                                    hideInfoMessage()
                                    showButton()

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

    override fun fetchSavedIntentionsByNameUserObserver() {
        /*
            Método encargado de traer todas las intenciones guardadas por el usuario autenticado actual.
        */
        if (isOnline(requireContext())) {

            intentionViewModel.fetchSavedIntentionsByNameUser()
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when (resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                if (resultEmitted.data.isNotEmpty()) {

                                    rv_intentions.adapter = IntentionAdapter(
                                            requireContext(),
                                            resultEmitted.data
                                    )

                                    hideNoRegisteredSeatsYetMessage()
                                    hideProgressBar()
                                    showRecyclerView()

                                } else {

                                    showNoRegisteredSeatsYetMessage()
                                    hideRecyclerView()
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

    override fun goToIntention() {
        /*
            Método encargado de navegar hacia el fragment de registro de intenciones.
        */
        btn_go_to_intention.setOnClickListener {
            navController.navigate(R.id.action_go_to_intention_fragment_from_main_intention_fragment)
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
        progressbar_main_intention.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un ProgressBar.
        */
        progressbar_main_intention.visibility = View.GONE
    }

    override fun setUpRecyclerView() {
        /*
            Método encargado de hacer el setup del RecyclerView.
        */
        rv_intentions.layoutManager = LinearLayoutManager(requireContext())
        rv_intentions.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun showRecyclerView() {
        /*
            Método encargado de mostrar un RecyclerView.
        */
        layout_rv_intentions.visibility = View.VISIBLE
    }

    override fun hideRecyclerView() {
        /*
            Método encargado de ocultar un RecyclerView.
        */
        layout_rv_intentions.visibility = View.GONE
    }

    override fun showNoRegisteredSeatsYetMessage() {
        /*
            Método encargado de mostrar un mensaje cuando aún no hay asientos registrados.
        */
        layout_no_registered_intentions_yet.visibility = View.VISIBLE
    }

    override fun hideNoRegisteredSeatsYetMessage() {
        /*
            Método encargado de ocultar un mensaje cuando aún no hay asientos registrados.
        */
        layout_no_registered_intentions_yet.visibility = View.GONE
    }

    override fun showInfoMessage() {
        /*
             Método encargado de mostrar un mensaje de informción.
        */
        layout_show_info_message_main_intention.visibility = View.VISIBLE
    }

    override fun hideInfoMessage() {
        /*
             Método encargado de ocultar un mensaje de informción.
        */
        layout_show_info_message_main_intention.visibility = View.GONE
    }

    override fun changeTextViewToDisabledSeatReservation() {
        /*
             Método encargado de cambiar el texto de un TextView cuando
             la reservación de asientos está deshabilitada.
        */
        txt_info_message_main_intention.text = getString(R.string.register_intention_is_disabled)
    }

    override fun showButton() {
        /*
             Método encargado de mostrar un Button.
        */
        btn_go_to_intention.visibility = View.VISIBLE
    }

    override fun hideButton() {
        /*
             Método encargado de ocultar un Button.
        */
        btn_go_to_intention.visibility = View.GONE
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

}