package com.circleappsstudio.mimisa.ui.main.intention

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
import com.circleappsstudio.mimisa.base.OnDialogClickButtonListener
import com.circleappsstudio.mimisa.data.datasource.intention.IntentionDataSource
import com.circleappsstudio.mimisa.data.model.IntentionSpinner
import com.circleappsstudio.mimisa.data.model.Intentions
import com.circleappsstudio.mimisa.domain.intention.IntentionRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.adapter.IntentionSpinnerAdapter
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryIntention
import com.circleappsstudio.mimisa.ui.viewmodel.intention.IntentionViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_intention.*

class IntentionFragment : BaseFragment(), UI.Intentions, OnDialogClickButtonListener {

    private lateinit var navController: NavController

    private val intentionViewModel by activityViewModels<IntentionViewModel> {
        VMFactoryIntention(
            IntentionRepository(
                IntentionDataSource()
            )
        )
    }

    private lateinit var intentionCategory: String
    private lateinit var intention: String

    override fun getLayout(): Int {
        return R.layout.fragment_intention
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        setUpSpinner()

        btn_save_intention.setOnClickListener {
            saveIntentionObserver()
        }

    }

    override fun setUpSpinner() {

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

        intentionCategory = intentionSpinnerAdapter.name

    }

    override fun saveIntentionObserver() {

        intention = txt_intention.text.toString()

        if (!isOnline(requireContext())) {
            showDialog()
            return
        }

        if (intentionViewModel.checkEmptyIntentionCategory(intentionCategory)){
            showMessage("Seleccione la categoría de su intención.", 2)
            return
        }

        if (intentionViewModel.checkEmptyIntention(intention)){
            txt_intention.error = "Complete los campos."
            return
        }

        if (isOnline(requireContext())) {

            intentionViewModel.saveIntention(
                    intentionCategory,
                    intention
            ).observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted){

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        showMessage("Intención guardada con éxito.", 1)
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

    override fun showMessage(message: String, duration: Int) {
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        progressbar_intention.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressbar_intention.visibility = View.GONE
    }

    override fun gotToSeatReservationMain() {
        navController.navigateUp()
    }

    override fun showDialog() {

        dialog(this,
                "¡No hay conexión a Internet!",
                "Verifique su conexión e inténtelo de nuevo.",
                R.drawable.ic_wifi_off,
                "Intentar de nuevo",
                ""
        )

    }

    override fun onPositiveButtonClicked() {

        if (isOnline(requireContext())){

            saveIntentionObserver()

        } else {

            dialog(this,
                    "¡No hay conexión a Internet!",
                    "Verifique su conexión e inténtelo de nuevo.",
                    R.drawable.ic_wifi_off,
                    "Intentar de nuevo",
                    ""
            )

        }

    }

    override fun onNegativeButtonClicked() {
        TODO("Not yet implemented")
    }

}