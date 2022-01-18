package com.huntergaming.gamedata.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.huntergaming.gamedata.model.Player
import javax.inject.Singleton

@Singleton
@Dao
internal interface PlayerDao : RoomDao<Player>, HunterGamingMigrateDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override suspend fun create(data: Player): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun update(data: Player): Int

    @Query("SELECT * FROM player")
    override suspend fun read(): Player

    override suspend fun migrateData() {
        // migrate then delete
        TODO("Migrate from Room to Firestore")
    }
}