package com.huntergaming.gamedata.dao

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.huntergaming.gamedata.DataRequestState
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine

@Singleton
internal class FirestoreDao @Inject constructor(
    private val db: FirebaseFirestore
) : HunterGamingMigrateDao,
    FirestorePlayerDao,
    FirestoreGameDao {

    // COMPANION OBJECTS

    companion object {
        private const val PLAYER_ROOT = "Players/"
        private const val LOG_TAG = "FirestoreDao"
    }

    // OVERRIDDEN FUNCTIONS

    override suspend fun migrateData() {
        // migrate then delete
        // keep firebase account for logging in
        TODO("Migrate from Firestore to Room")
    }

    override suspend fun create(id: String, name: String, email: String): Player? =

        suspendCancellableCoroutine { cont->
            val player = Player(id, name, email)
            db.collection(PLAYER_ROOT).document(id).set(player)
                .addOnCompleteListener {

                    if (it.exception != null) {
                        Log.e(LOG_TAG, it.exception!!.message, it.exception)
                    }

                    cont.resume(if (it.isSuccessful) player else null)
                }
        }

    override suspend fun update(player: Player): Boolean {
        TODO("Update Firebase user profile and save player")
    }

    override suspend fun read(id: String): Player? {
        TODO("Not yet implemented")
    }

    override suspend fun create(id: String): Game? {
        TODO("Not yet implemented")
    }

    override suspend fun update(game: Game): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getMostRecentGame(): Game? {
        TODO("Not yet implemented")
    }

    override suspend fun getHighScoreGame(): Game? {
        TODO("Not yet implemented")
    }

    override suspend fun getTopTenGames(): List<Game>? {
        TODO("Not yet implemented")
    }
}