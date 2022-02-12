package com.huntergaming.gamedata.dao

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.huntergaming.authentication.saveNewPlayer
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

@Singleton
internal class FirestoreDao @Inject constructor(
    private val db: FirebaseFirestore
) : FirestorePlayerDao,
    FirestoreGameDao {

    // companion objects

    companion object {
        private const val PLAYER_ROOT = "Players/"
        private const val LOG_TAG = "FirestoreDao"
    }

    // initializers

    init {
        CoroutineScope(Dispatchers.IO).launch {
            saveNewPlayer
                .filter { it.size == 2 }
                .collect { create(Firebase.auth.currentUser?.uid!!, it[0], it[1]) }
        }
    }

    // overridden functions

    override suspend fun create(id: String, name: String, email: String): Player? = suspendCancellableCoroutine { cont->
            val player = Player(id, name, email)
            db.collection(PLAYER_ROOT).document(id).set(player)
                .addOnCompleteListener {

                    if (it.exception != null) {
                        Log.e(LOG_TAG, it.exception!!.message, it.exception)
                        cont.resume(null)
                    }

                    cont.resume(if (it.isSuccessful) player else null)
                }
        }

    override suspend fun update(player: Player): Boolean {
        TODO("Update Firebase user profile and save player")
    }

    override suspend fun updateName(id: String, name: String): Boolean = suspendCancellableCoroutine { cont->
        db.collection(PLAYER_ROOT).document(id).update("name", name)
            .addOnCompleteListener {
                cont.resume(it.isSuccessful)
            }
    }

    override suspend fun observePlayer(id: String): StateFlow<Player?> = flow {
        db.collection(PLAYER_ROOT).document(id).addSnapshotListener { player, error ->

            if (error != null) {
                Log.e(LOG_TAG, error.message, error)
                suspend { emit(null) }
            }
            else if (player != null) {
                suspend { emit(player.toObject(Player::class.java)) }
            }
        }
    }.stateIn(CoroutineScope(Dispatchers.IO))

    override suspend fun read(id: String): Player? = suspendCancellableCoroutine { cont->
            db.collection(PLAYER_ROOT).document(id).get()
                .addOnCompleteListener {

                    if (it.exception != null) {
                        Log.e(LOG_TAG, it.exception!!.message, it.exception)
                        cont.resume(null)
                    }

                    cont.resume(if (it.isSuccessful) it.result?.toObject(Player::class.java) else null)
                }
        }

    override suspend fun create(id: String): Game? {
        TODO("Not yet implemented")
    }

    override suspend fun update(game: Game): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun observeGame(id: String): StateFlow<Game?> {
        TODO("Not yet implemented")
    }

    override suspend fun getMostRecentGame(): Game? {
        TODO("Not yet implemented")
    }

    override suspend fun getTopTenGames(): List<Game>? {
        TODO("Not yet implemented")
    }
}

// interfaces

interface FirestorePlayerDao {

    suspend fun create(id: String, name: String, email: String): Player?
    suspend fun update(player: Player): Boolean
    suspend fun updateName(id: String, name: String): Boolean
    suspend fun observePlayer(id: String): StateFlow<Player?>
    suspend fun read(id: String): Player?
}

interface FirestoreGameDao {

    suspend fun create(id: String): Game?
    suspend fun update(game: Game): Boolean
    suspend fun observeGame(id: String): StateFlow<Game?>
    suspend fun getMostRecentGame(): Game?
    suspend fun getTopTenGames(): List<Game>?
}