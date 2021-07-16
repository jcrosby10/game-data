package com.huntergaming.gamedata.data

import com.google.firebase.firestore.FirebaseFirestore
import com.huntergaming.gamedata.RequestState
import com.huntergaming.gamedata.model.Player
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
internal class PlayerDao @Inject constructor(
    private val firestore: FirebaseFirestore
): Dao<Player> {

    override suspend fun create(data: Player): Flow<RequestState> = callbackFlow {
        send(RequestState.InProgress)

        firestore.collection("ClassicSolitaire").document("PlayerData").set(data)
            .addOnSuccessListener {
                trySend(RequestState.Success)
            }.addOnFailureListener {
                Timber.e(it,"Failed to create the player.")
                trySend(RequestState.Error(it.message))
            }

        awaitClose { cancel() }
    }

    override fun update(data: Player) {

    }

    override fun get(): Player {
        TODO("Not yet implemented")
    }

    override fun delete() {

    }
}