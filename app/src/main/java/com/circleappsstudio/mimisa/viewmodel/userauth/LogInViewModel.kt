package com.circleappsstudio.mimisa.viewmodel.userauth

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import com.circleappsstudio.mimisa.domain.Repo
import com.circleappsstudio.mimisa.viewmodel.MainViewModel

class LogInViewModel(private val logInRepo: Repo.LogInUser) : ViewModel(), MainViewModel.LogInUser {

    override suspend fun signInUserViewModel(email: String, password: String) {
        logInRepo.signInUserRepo(email, password)
    }

    override suspend fun updateUserProfileViewModel(fullName: String) {
        logInRepo.updateUserProfileRepo(fullName)
    }

    override fun checkEmptyFieldsForSignInViewModel(
        fullName: String,
        email: String,
        password1: String,
        password2: String
    ): Boolean {
        return email.isEmpty() &&  password1.isEmpty() && password2.isEmpty()
    }

    override fun checkMatchPasswordsForSignInViewModel(password1: String, password2: String): Boolean {
        return password1 != password2
    }

    override suspend fun logInUserViewModel(email: String, password: String) {
        logInRepo.logInUserRepo(email, password)
    }

    override fun checkEmptyFieldsForLogInViewModel(email: String, password: String): Boolean {
        return email.isEmpty() && password.isEmpty()
    }

    override suspend fun resetPasswordUserViewModel(email: String) {
        logInRepo.resetPasswordUserRepo(email)
    }

    override fun checkEmptyFieldsForResetPasswordViewModel(email: String) : Boolean {
        return email.isEmpty()
    }

    override fun checkValidEmailViewModel(email: String): Boolean {
        return !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun checkValidPasswordViewModel(password: String): Boolean {
        return password.length <= 5
    }

    override fun logOutUserViewModel() {
        logInRepo.logOutUserRepo()
    }

}