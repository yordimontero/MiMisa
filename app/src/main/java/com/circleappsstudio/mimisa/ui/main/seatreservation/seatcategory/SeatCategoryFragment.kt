package com.circleappsstudio.mimisa.ui.main.seatreservation.seatcategory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.auth.AuthDataSource
import com.circleappsstudio.mimisa.data.datasource.roleuser.RoleUserDataSource
import com.circleappsstudio.mimisa.domain.auth.AuthRepository
import com.circleappsstudio.mimisa.domain.roleuser.RoleUserRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.viewmodel.auth.AuthViewModel
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAdmin
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAuth
import com.circleappsstudio.mimisa.ui.viewmodel.roleuser.RoleUserViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_seat_category.*

class SeatCategoryFragment : BaseFragment(),
    UI.SeatCategory {

    private lateinit var navController: NavController

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

    override fun getLayout(): Int = R.layout.fragment_seat_category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        checkIfUserIsAdmin()

        goToCoupleSeat()

        goToThreesomeSeat()

        goToBubbleSeat()

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

    override fun goToCoupleSeat() {

        btn_couple_seat_category.setOnClickListener {

            if (isAdmin) {
                //navController.navigate(R.id.action_go_to_seat_category_from_seat_reservation_main_fragment)
                navController.navigate(R.id.allSeatCouplesFragment2)
            } else {
                navController.navigate(R.id.allSeatCouplesFragment)
            }

            //navController.navigate(R.id.allSeatCouplesFragment)

        }

    }

    override fun goToThreesomeSeat() {

        btn_threesome_seat_category.setOnClickListener {

            if (isAdmin) {
                navController.navigate(R.id.allSeatThreesomesFragment2)
            } else {
                navController.navigate(R.id.action_go_to_all_threesome_seats_from_seat_category_fragment)
            }

            //navController.navigate(R.id.action_go_to_all_threesome_seats_from_seat_category_fragment)

        }

    }

    override fun goToBubbleSeat() {

        btn_bubble_seat_category.setOnClickListener {

            //navController.navigate(R.id.action_go_to_all_bubble_seats_from_seat_category_fragment)

            if (isAdmin) {
                navController.navigate(R.id.allSeatBubblesFragment2)
            } else {
                navController.navigate(R.id.action_go_to_all_bubble_seats_from_seat_category_fragment)
            }

        }

    }

    override fun showMessage(message: String, duration: Int) {
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        progressbar_seat_category.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressbar_seat_category.visibility = View.GONE
    }

}