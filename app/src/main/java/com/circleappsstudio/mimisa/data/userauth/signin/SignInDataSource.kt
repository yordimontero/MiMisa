package com.circleappsstudio.mimisa.data.userauth.signin

import com.circleappsstudio.mimisa.data.DataSource
import com.google.firebase.auth.FirebaseAuth

class SignInDataSource :
    DataSource.SignInUser {

    private var user: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun signInUserDataSource(email: String, password: String) {
        user.createUserWithEmailAndPassword(email, password)
    }

}