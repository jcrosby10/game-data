package com.huntergaming.gamedata.authentication

import android.text.TextUtils
import android.util.Patterns.EMAIL_ADDRESS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.huntergaming.gamedata.RequestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
internal class FirebaseAuthentication @Inject constructor(
    private val auth: FirebaseAuth
): Authentication {

    override val emailVerifiedFlow: Flow<Boolean?> = callbackFlow {
        auth.addAuthStateListener {
            if (it.currentUser != null) {
                trySend(it.currentUser?.isEmailVerified)
            }
        }

        awaitClose { cancel() }
    }

    override val isLoggedIn: Boolean
        get() = auth.currentUser != null

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override fun validateStrongPassword(password: String): Boolean =
        """^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@${'$'}!%*?&])[A-Za-z\d@${'$'}!%*?&]{8,}${'$'}""".toRegex().matches(password)

    override fun validateEmailAddress(email: String): Boolean =
        if (TextUtils.isEmpty(email)) false else EMAIL_ADDRESS.matcher(email).matches()

    override fun resetPassword(email: String): Flow<RequestState> = callbackFlow {
        send(RequestState.InProgress)

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { trySend(RequestState.Success) }
            .addOnFailureListener {
                Timber.e(it)
                trySend(RequestState.Error(it.message))
            }

        awaitClose { cancel() }
    }

    override suspend fun createAccount(email: String, password: String): Flow<RequestState> = callbackFlow {
        send(RequestState.InProgress)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                auth.currentUser?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        signOut()
                        trySend(RequestState.Success)
                    }
                    ?.addOnFailureListener {
                        signOut()
                        Timber.e(it)
                        trySend(RequestState.Error(it.message))
                    }
            }
            .addOnFailureListener {
                signOut()
                Timber.e(it)
                trySend(RequestState.Error(it.message))
            }

        awaitClose { cancel() }
    }

    override fun deleteAccount(): Flow<RequestState> {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(email: String, password: String): Flow<RequestState> = callbackFlow {
        send(RequestState.InProgress)

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                trySend(RequestState.Success)
            }
            .addOnFailureListener {
                Timber.e(it)
                trySend(RequestState.Error(it.message))
            }

        awaitClose { cancel() }
    }

    override fun signOut() {
        auth.signOut()
    }
}