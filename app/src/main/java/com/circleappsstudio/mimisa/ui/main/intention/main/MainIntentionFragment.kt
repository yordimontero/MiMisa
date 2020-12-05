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
import com.circleappsstudio.mimisa.domain.intention.IntentionRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.adapter.IntentionAdapter
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryIntention
import com.circleappsstudio.mimisa.ui.viewmodel.intention.IntentionViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_main_intention.*

class MainIntentionFragment : BaseFragment(), UI.IntentionMain {

    private lateinit var navController: NavController

    private val intentionViewModel by activityViewModels<IntentionViewModel> {
        VMFactoryIntention(
                IntentionRepository(
                        IntentionDataSource()
                )
        )
    }

    override fun getLayout(): Int {
        return R.layout.fragment_main_intention
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        setUpRecyclerView()

        goToIntention()

        fetchSavedIntentionsByNameUserObserver()

    }

    override fun goToIntention() {

        btn_go_to_intention.setOnClickListener {
            navController.navigate(R.id.intentionFragment)
        }

    }

    override fun setUpRecyclerView() {
        rv_intentions.layoutManager = LinearLayoutManager(requireContext())
        rv_intentions.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun fetchSavedIntentionsByNameUserObserver() {

        intentionViewModel.fetchSavedIntentionsByNameUser().observe(viewLifecycleOwner, Observer { resultEmitted ->

            when (resultEmitted) {

                is Resource.Loading -> { showProgressBar() }

                is Resource.Success -> {

                    if (resultEmitted.data.isNotEmpty()) {

                        rv_intentions.adapter = IntentionAdapter(requireContext(), resultEmitted.data)
                        hideProgressBar()
                        showRecyclerView()

                    } else {

                        hideProgressBar()
                        hideRecyclerView()

                    }

                }

                is Resource.Failure -> {
                    showMessage(resultEmitted.exception.message.toString(), 2)
                    hideProgressBar()
                }

            }

        })

    }

    override fun showMessage(message: String, duration: Int) {
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        progressbar_main_intention.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressbar_main_intention.visibility = View.GONE
    }

    override fun showRecyclerView() {
        layout_rv_intentions.visibility = View.VISIBLE
    }

    override fun hideRecyclerView() {
        layout_rv_intentions.visibility = View.GONE
    }

}