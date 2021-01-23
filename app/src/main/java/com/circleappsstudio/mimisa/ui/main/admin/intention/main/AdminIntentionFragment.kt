package com.circleappsstudio.mimisa.ui.main.admin.intention.main

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
import com.circleappsstudio.mimisa.domain.intention.IntentionRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.adapter.IntentionAdapter
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryIntention
import com.circleappsstudio.mimisa.ui.viewmodel.intention.IntentionViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_admin_intention.*

class AdminIntentionFragment : BaseFragment(),
        UI.AdminIntentions,
        UI.IsOnlineDialogClickButtonListener {

    private lateinit var navController: NavController

    private val intentionViewModel by activityViewModels<IntentionViewModel> {
        VMFactoryIntention(
                IntentionRepository(
                        IntentionDataSource()
                )
        )
    }

    override fun getLayout(): Int = R.layout.fragment_admin_intention

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        setupRecyclerView()

        fetchData()

        fetchSavedIntentionsByCategory()

        goToOptionAdminIntention()

    }

    override fun fetchData() {

        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
            return
        }

        fetchAllSavedIntentionsObserver()

    }

    override fun fetchAllSavedIntentionsObserver() {
        /*
            Método encargado de traer todas las intenciones guardadas en la base de datos.
        */
        if (isOnline(requireContext())) {

            intentionViewModel.fetchAllSavedIntentions()
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when (resultEmitted) {

                            is Resource.Loading -> {
                                hideMainLayout()
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                if (resultEmitted.data.isNotEmpty()) {

                                    rv_admin_intentions.adapter = IntentionAdapter(
                                            requireContext(), resultEmitted.data
                                    )

                                    showMainLayout()
                                    hideProgressBar()
                                    hideNotIntentionFoundedMessage()
                                    showRecyclerView()

                                } else {

                                    showMainLayout()
                                    hideProgressBar()
                                    hideRecyclerView()
                                    showNotIntentionFoundedMessage()

                                }

                            }

                            is Resource.Failure -> {

                                showMessage(resultEmitted.exception.message.toString(), 2)
                                showMainLayout()
                                hideProgressBar()

                            }

                        }

                    })

        }

    }

    override fun fetchSavedIntentionsByCategory() {

        btn_search_saved_intentions.setOnClickListener {

            var category = ""

            if (!isOnline(requireContext())) {
                showIsOnlineDialog()
                return@setOnClickListener
            }

            if (
                    !rd_btn_search_thanksgiving_category.isChecked &&
                    !rd_btn_search_deceased_category.isChecked &&
                    !rd_btn_search_birthday_category.isChecked &&
                    !rd_btn_search_all_intentions.isChecked
            ) {
                showMessage(getString(R.string.select_search_filter), 2)
                return@setOnClickListener
            }

            if (rd_btn_search_thanksgiving_category.isChecked) {

                category = "Acción de Gracias"
                fetchSavedIntentionsByCategoryObserver(category)
                return@setOnClickListener

            }

            if (rd_btn_search_deceased_category.isChecked) {

                category = "Difuntos"
                fetchSavedIntentionsByCategoryObserver(category)
                return@setOnClickListener

            }

            if (rd_btn_search_birthday_category.isChecked) {

                category = "Cumpleaños"
                fetchSavedIntentionsByCategoryObserver(category)
                return@setOnClickListener

            }

            if (rd_btn_search_all_intentions.isChecked) {

                fetchAllSavedIntentionsObserver()
                return@setOnClickListener

            }

        }

    }

    override fun fetchSavedIntentionsByCategoryObserver(category: String) {
        /*
            Método encargado de traer las intenciones guardadas por categoría.
        */
        if (isOnline(requireContext())) {

            intentionViewModel.fetchSavedIntentionsByCategory(category)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when (resultEmitted) {

                            is Resource.Loading -> {
                                hideMainLayout()
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                if (resultEmitted.data.isNotEmpty()) {

                                    rv_admin_intentions.adapter = IntentionAdapter(
                                            requireContext(), resultEmitted.data
                                    )

                                    showMainLayout()
                                    hideProgressBar()
                                    hideNotIntentionFoundedMessage()
                                    showRecyclerView()

                                } else {

                                    showMainLayout()
                                    hideProgressBar()
                                    hideRecyclerView()
                                    showNotIntentionFoundedMessage()

                                }

                            }

                            is Resource.Failure -> {

                                showMessage(resultEmitted.exception.message.toString(), 2)
                                showMainLayout()
                                hideProgressBar()

                            }

                        }

                    })

        }

    }

    override fun goToOptionAdminIntention() {
        /*
            Método encargado de navegar hacia el fragment "OptionsAdminSeatReservation".
        */
        btn_go_to_options_admin_intention.setOnClickListener {
            navController.navigate(R.id.optionsAdminIntentionFragment)
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
        progressbar_admin_intention.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un ProgressBar.
        */
        progressbar_admin_intention.visibility = View.GONE
    }

    override fun setupRecyclerView() {
        /*
            Método encargado de hacer el setup del RecyclerView.
        */
        rv_admin_intentions.layoutManager = LinearLayoutManager(requireContext())
        rv_admin_intentions.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

    }

    override fun showRecyclerView() {
        /*
            Método encargado de mostrar un RecyclerView.
        */
        layout_rv_admin_intentions.visibility = View.VISIBLE
    }

    override fun hideRecyclerView() {
        /*
            Método encargado de ocultar un RecyclerView.
        */
        layout_rv_admin_intentions.visibility = View.GONE
    }

    override fun showMainLayout() {
        /*
            Método encargado de mostrar el layout principal.
        */
        layout_admin_intention.visibility = View.VISIBLE
    }

    override fun hideMainLayout() {
        /*
            Método encargado de ocultar el layout principal.
        */
        layout_admin_intention.visibility = View.GONE
    }

    override fun showNotIntentionFoundedMessage() {
        /*
            Método encargado de mostrar un mensaje cuando no se encontraron intenciones.
        */
        layout_not_registered_intention_founded.visibility = View.VISIBLE
    }

    override fun hideNotIntentionFoundedMessage() {
        /*
            Método encargado de ocultar un mensaje cuando no se encontraron intenciones.
        */
        layout_not_registered_intention_founded.visibility = View.GONE
    }

    override fun showIsOnlineDialog() {
        /*
            Método encargado de mostrar el Dialog "IsOnlineDialog".
        */
        isOnlineDialog(this)
    }

    override fun isOnlineDialogPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog "IsOnlineDialog".
        */
        if (isOnline(requireContext())) {
            fetchData()
        } else {
            showIsOnlineDialog()
        }

    }

}