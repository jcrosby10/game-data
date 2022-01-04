package com.huntergaming.gamedata.dao

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

@Singleton
internal class HunterGamingFirebaseDao @Inject constructor(
    private val db: FirebaseFirestore
) : HunterGamingMigrateDao,
    PlayerFirebaseDao,
    GameFirebaseDao {

    companion object {
        private const val PLAYER_ROOT = "Players/"
        private const val LOG_TAG = "HunterGamingFirebaseDao"
    }

    override suspend fun migrateData() {
//        db.collection("").document().get().addOnCompleteListener {  }
        // migrate then delete
        // keep firebase account for logging in
        TODO("Migrate from Firestore to Room")
    }

    override suspend fun create(player: Player): Boolean =
        suspendCancellableCoroutine { cont->
            db.collection(PLAYER_ROOT).document(player.id).set(player)
                .addOnCompleteListener {
                    if (it.exception != null) {
                        Log.e(LOG_TAG, it.exception!!.message, it.exception)
                    }
                    cont.resume(it.isSuccessful)
                }
        }

    override suspend fun update(player: Player): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getPlayer(): Player {
        TODO("Not yet implemented")
    }

    override suspend fun create(game: Game): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun update(game: Game): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getMostRecentGame(): Game {
        TODO("Not yet implemented")
    }

    override suspend fun getHighScoreGame(): Game {
        TODO("Not yet implemented")
    }

    override suspend fun getTopTenGames(): List<Game> {
        TODO("Not yet implemented")
    }
}

interface PlayerFirebaseDao {
    suspend fun create(player: Player): Boolean
    suspend fun update(player: Player): Boolean
    suspend fun getPlayer(): Player
}

interface GameFirebaseDao {
    suspend fun create(game: Game): Boolean
    suspend fun update(game: Game): Boolean
    suspend fun getMostRecentGame(): Game
    suspend fun getHighScoreGame(): Game
    suspend fun getTopTenGames(): List<Game>
}