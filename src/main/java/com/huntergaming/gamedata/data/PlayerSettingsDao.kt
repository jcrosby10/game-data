package com.huntergaming.gamedata.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.huntergaming.gamedata.model.PlayerSettings
import javax.inject.Singleton

@Singleton
@Dao
internal interface PlayerSettingsDao : HunterGamingDao<PlayerSettings>, HunterGamingMigrateDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override suspend fun create(data: PlayerSettings)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun update(data: PlayerSettings)

    override suspend fun migrateData() {
        TODO("Migrate from Room to Firestore")
    }

    @Query("SELECT * FROM player_settings")
    suspend fun read(): PlayerSettings
}