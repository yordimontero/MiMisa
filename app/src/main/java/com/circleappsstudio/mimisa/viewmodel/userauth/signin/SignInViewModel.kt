package com.circleappsstudio.mimisa.viewmodel.userauth.signin

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.circleappsstudio.mimisa.domain.Repo
import com.google.firebase.FirebaseException
import kotlinx.coroutines.launch

class SignInViewModel(private val signInRepo: Repo.SignInUser) : ViewModel(), com.circleappsstudio.mimisa.viewmodel.ViewModel.SignInUser {

    override fun signInUserViewModel(context: Context, email: String, password: String) {

        viewModelScope.launch {

            try {

                signInRepo.signInUserRepo(email, password)

            } catch (e: FirebaseException){

                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()

            }


        }
    }

}