package com.circleappsstudio.mimisa.domain.userauth.signin

import com.circleappsstudio.mimisa.data.DataSource
import com.circleappsstudio.mimisa.domain.Repo

class SignInRepo(private val signInDataSource: DataSource.SignInUser) : Repo.SignInUser {

    override suspend fun signInUserRepo(email: String, password: String) {
        signInDataSource.signInUserDataSource(email, password)
    }

}