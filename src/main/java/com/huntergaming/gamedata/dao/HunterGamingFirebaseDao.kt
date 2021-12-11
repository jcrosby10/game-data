package com.huntergaming.gamedata.dao

import com.google.firebase.firestore.FirebaseFirestore
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import javax.inject.Inject

internal class HunterGamingFirebaseDao @Inject constructor(
    private val db: FirebaseFirestore
) : HunterGamingMigrateDao,
    PlayerFirebaseDao,
    GameFirebaseDao {

    override suspend fun migrateData() {
        // migrate then delete
        TODO("Migrate from Firestore to Room")
    }

    override suspend fun create(player: Player): Boolean {
        TODO("Not yet implemented")
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