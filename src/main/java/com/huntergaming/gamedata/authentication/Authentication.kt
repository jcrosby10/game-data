package com.huntergaming.gamedata.authentication

import com.google.firebase.auth.FirebaseUser
import com.huntergaming.gamedata.RequestState
import kotlinx.coroutines.flow.Flow

interface Authentication {

    val isLoggedIn: Boolean
    val emailVerifiedFlow: Flow<Boolean?>
    val currentUser: FirebaseUser?

    /**
     * Validates a strong password.
     * Requirements are minimum of 8 characters, at least 1 upper case letter, at least one lower case letter, at least 1 number and one special character.
     *
     * @param password The password to check.
     * @return True if the password meets the minimum requirements.
     */
    fun validateStrongPassword(password: String): Boolean
    fun validateEmailAddress(email: String): Boolean

    fun resetPassword(email: String): Flow<RequestState>

    suspend fun createAccount(email: String, password: String): Flow<RequestState>
    fun deleteAccount(): Flow<RequestState>

    suspend fun signIn(email: String, password: String): Flow<RequestState>
    fun signOut()
}