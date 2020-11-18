package com.circleappsstudio.mimisa.domain.userauth

import com.circleappsstudio.mimisa.data.DataSource
import com.circleappsstudio.mimisa.domain.Repo

class LogInRepo(private val logInDataSource: DataSource.LogInUser) : Repo.LogInUser {

    override suspend fun signInUserRepo(email: String, password: String) {
        logInDataSource.signInUserDataSource(email, password)
    }

    override suspend fun updateUserProfileRepo(fullName: String) {
        logInDataSource.updateUserProfileDataSource(fullName)
    }

    override suspend fun logInUserRepo(email: String, password: String) {
        logInDataSource.logInUserDataSource(email, password)
    }

    override suspend fun resetPasswordUserRepo(email: String) {
        logInDataSource.resetPasswordUserDataSource(email)
    }

    override fun logOutUserRepo() {
        logInDataSource.logOutUser()
    }

}