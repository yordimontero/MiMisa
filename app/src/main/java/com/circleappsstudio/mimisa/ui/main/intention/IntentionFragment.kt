package com.circleappsstudio.mimisa.ui.main.intention

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.intention.IntentionDataSource
import com.circleappsstudio.mimisa.domain.intention.IntentionRepo
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryIntention
import com.circleappsstudio.mimisa.ui.viewmodel.intention.IntentionViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_intention.*

class IntentionFragment : BaseFragment(), UI.Intention {

    private lateinit var navController: NavController

    private val intentionViewModel by activityViewModels<IntentionViewModel> {
        VMFactoryIntention(
            IntentionRepo(
                IntentionDataSource()
            )
        )
    }

    private lateinit var spinnerIntentionCategory: String
    private lateinit var renamedCategory: String
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

        spinIntentionCategory.adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.intention_category_array)
        )

        spinIntentionCategory.onItemSelectedListener = object : OnItemSelectedListener {

            override fun onItemSelected(
                item: AdapterView<*>?, arg1: View,
                position: Int, id: Long) {

                spinnerIntentionCategory = item!!.getItemAtPosition(position).toString()

                renamedCategory = intentionViewModel
                    .renameCategoryResource(requireContext(), spinnerIntentionCategory)

                showMessage(renamedCategory, 1)

            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}

        }

    }

    override fun saveIntentionObserver() {

        intention = txt_intention.text.toString()

        if (intentionViewModel.checkEmptyIntentionCategory(renamedCategory)){
            showMessage("Seleccione la categoría de su Intención.", 2)
            return
        }

        if (intentionViewModel.checkEmptyIntention(intention)){
            txt_intention.error = "Complete los campos."
            return
        }

        intentionViewModel.saveIntention(
            renamedCategory,
            intention
        ).observe(viewLifecycleOwner, Observer { resultEmitted ->

            when(resultEmitted){

                is Resource.Loading -> {
                    showProgressBar()
                }

                is Resource.Success -> {
                    showMessage("Intención guardada con éxito.", 1)
                    hideProgressBar()
                }

                is Resource.Failure -> {
                    requireContext().toast(requireContext(), resultEmitted.exception.message.toString())
                    hideProgressBar()
                }

            }

        })

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

}