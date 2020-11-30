package com.circleappsstudio.mimisa.ui.main.intention.main

import android.os.Bundle
import android.util.Log
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
import com.circleappsstudio.mimisa.domain.intention.IntentionRepo
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
                IntentionRepo(
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

                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    rv_intentions.adapter = IntentionAdapter(requireContext(), resultEmitted.data)
                }

                is Resource.Failure -> {
                }

            }

        })

    }

}