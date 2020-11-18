package com.circleappsstudio.mimisa.data.login

import com.circleappsstudio.mimisa.data.DataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class LogInDataSource : DataSource.LogInUser {

    private var user: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun signInUserDataSource(email: String, password: String) {
        user.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun updateUserProfileDataSource(fullName: String) {

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(fullName)
            .build()

        user.currentUser?.updateProfile(profileUpdates)?.await()

    }

    override suspend fun logInUserDataSource(email: String, password: String) {
        user.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun resetPasswordUserDataSource(email: String) {
        user.sendPasswordResetEmail(email).await()
    }

    override fun logOutUser() {
        user.signOut()
    }

}