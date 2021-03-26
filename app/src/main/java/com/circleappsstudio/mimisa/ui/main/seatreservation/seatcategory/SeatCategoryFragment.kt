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

class SeatCategoryFragment : BaseFragment(), UI.SeatCategory {

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
        /*
            Método encargado de verificar si el usuario actual es un administrador.
        */
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
        /*
            Método encargado de navegar hacia el fragment AllSeatCouplesFragment.
        */
        btn_couple_seat_category.setOnClickListener {

            if (isAdmin) {
                navController.navigate(R.id.admin_all_seat_couples_fragment)
            } else {
                navController.navigate(R.id.all_seat_couples_fragment)
            }

        }

    }

    override fun goToThreesomeSeat() {
        /*
            Método encargado de navegar hacia el fragment AllSeatThreesomesFragment.
        */
        btn_threesome_seat_category.setOnClickListener {

            if (isAdmin) {
                navController.navigate(R.id.admin_all_seat_threesomes_fragment)
            } else {
                navController.navigate(R.id.all_seat_threesomes_fragment)
            }

        }

    }

    override fun goToBubbleSeat() {
        /*
            Método encargado de navegar hacia el fragment AllSeatBubblesFragment.
        */
        btn_bubble_seat_category.setOnClickListener {

            if (isAdmin) {
                navController.navigate(R.id.admin_all_seat_bubbles_fragment)
            } else {
                navController.navigate(R.id.all_seat_bubbles_fragment)
            }

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
        progressbar_seat_category.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
             Método encargado de ocultar un ProgressBar.
        */
        progressbar_seat_category.visibility = View.GONE
    }

}