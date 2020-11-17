package com.circleappsstudio.mimisa.ui.userauth.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.data.userauth.signin.SignInDataSource
import com.circleappsstudio.mimisa.domain.userauth.signin.SignInRepo
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.viewmodel.factory.VMFactory
import com.circleappsstudio.mimisa.viewmodel.userauth.signin.SignInViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.*
import com.google.firebase.FirebaseException

class SignInFragment : Fragment(), UI.SignInUI {

    private lateinit var navController: NavController

    private lateinit var email: String
    private lateinit var password1: String
    private lateinit var password2: String

    private val signInViewModel by activityViewModels<SignInViewModel> {
        VMFactory(
            SignInRepo(
                SignInDataSource()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        signInUserUI()

    }

    override fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun showProgressBar() {
        progressbar_sign_in_user.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressbar_sign_in_user.visibility = View.GONE
    }

    override fun signInUserUI() {

        /*btn_sign_in_user.setOnClickListener {

            showProgressBar()

            email = txt_email_sign_in_user.text.toString()
            password1 = txt_password1_sign_in_user.text.toString()
            password2 = txt_password2_sign_in_user.text.toString()

            try {

                signInViewModel.signInUserViewModel(email, password1)
                showMessage("Usuario registrado con éxito.")
                hideProgressBar()

            } catch (e: FirebaseException){
                showMessage(e.message.toString())
                hideProgressBar()
            }

        }*/

        btn_sign_in_user.setOnClickListener {

            showProgressBar()

            email = txt_email_sign_in_user.text.toString()
            password1 = txt_password1_sign_in_user.text.toString()
            password2 = txt_password2_sign_in_user.text.toString()

            signInViewModel.signInUserViewModel(requireContext(), email, password1)

            showMessage("Usuario registrado con éxito.")

            hideProgressBar()

        }

    }

}