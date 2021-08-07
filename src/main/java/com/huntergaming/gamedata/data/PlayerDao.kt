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
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(player: Player)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(player: Player)

    @Query("SELECT * FROM player")
    suspend fun get(): Player
}