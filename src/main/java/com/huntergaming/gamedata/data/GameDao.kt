package com.huntergaming.gamedata.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.huntergaming.gamedata.model.Game
import javax.inject.Singleton

@Singleton
@Dao
internal interface GameDao : HunterGamingDao<Game> {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override suspend fun create(data: Game)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun update(data: Game)

    @Query("SELECT * FROM games WHERE id = :id")
    suspend fun get(id: Int): Game

    @Query("SELECT * FROM games ORDER BY score DESC LIMIT 1")
    suspend fun getHighScoreGame(): Game

    @Query("SELECT * FROM games ORDER BY score DESC LIMIT 10")
    suspend fun getTopTenGames(): List<Game>
}