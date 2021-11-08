package com.huntergaming.gamedata.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.huntergaming.gamedata.model.Player
import javax.inject.Singleton

@Singleton
@Dao
internal interface PlayerDao : HunterGamingDao<Player>, HunterGamingMigrateDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override suspend fun create(data: Player)

    override suspend fun migrateData() {
        TODO("Migrate from Room to Firestore")
    }

    @Update(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun update(data: Player)

    @Query("SELECT * FROM player")
    suspend fun get(): Player
}