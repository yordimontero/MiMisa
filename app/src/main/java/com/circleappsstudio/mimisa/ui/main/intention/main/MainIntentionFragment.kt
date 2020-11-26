package com.circleappsstudio.mimisa.ui.main.intention.main

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.ui.UI
import kotlinx.android.synthetic.main.fragment_main_intention.*

class MainIntentionFragment : BaseFragment(), UI.IntentionMain {

    private lateinit var navController: NavController

    override fun getLayout(): Int {
        return R.layout.fragment_main_intention
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        goToIntention()

    }

    override fun goToIntention() {

        btn_go_to_intention.setOnClickListener {
            navController.navigate(R.id.intentionFragment)
        }

    }

}