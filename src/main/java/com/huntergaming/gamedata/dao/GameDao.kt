package com.huntergaming.gamedata.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.huntergaming.gamedata.model.Game
import javax.inject.Singleton

@Singleton
@Dao
internal interface GameDao : RoomDao<Game>, HunterGamingMigrateDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override suspend fun create(data: Game): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun update(data: Game): Int

    @Query("SELECT * FROM games ORDER BY id DESC LIMIT 1")
    override suspend fun read(): Game

    @Query("SELECT * FROM games ORDER BY score DESC LIMIT 1")
    suspend fun getHighScoreGame(): Game

    @Query("SELECT * FROM games ORDER BY score DESC LIMIT 10")
    suspend fun getTopTenGames(): List<Game>

    override suspend fun migrateData() {
        // migrate then delete
        TODO("Migrate from Room to Firestore")
    }
}