package com.huntergaming.gamedata.dao

import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player

interface FirestorePlayerDao {

    suspend fun create(id: String, name: String, email: String): Player?
    suspend fun update(player: Player): Boolean
    suspend fun read(id: String): Player?
}

interface FirestoreGameDao {

    suspend fun create(id: String): Game?
    suspend fun update(game: Game): Boolean
    suspend fun getMostRecentGame(): Game?
    suspend fun getHighScoreGame(): Game?
    suspend fun getTopTenGames(): List<Game>?
}