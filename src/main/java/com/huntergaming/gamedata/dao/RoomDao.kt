package com.huntergaming.gamedata.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import javax.inject.Singleton

@Singleton
@Dao
internal interface GameDao: RoomDao<Game> {

    // overridden functions

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override suspend fun create(data: Game): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun update(data: Game): Int

    @Query("SELECT * FROM games ORDER BY id DESC LIMIT 1")
    override suspend fun read(): Game

    @Query("SELECT * FROM games WHERE id = :id")
    override fun observe(id: String): LiveData<Game>

    // functions

    @Query("SELECT * FROM games ORDER BY time DESC LIMIT 1")
    suspend fun getMostRecentGame(): Game

    @Query("SELECT * FROM games ORDER BY score DESC LIMIT 10")
    suspend fun getTopTenGames(): List<Game>
}

@Singleton
@Dao
internal interface GameMigrateDao: HunterGamingMigrateDao<Game> {

    // overridden functions

    @Query("SELECT * FROM games")
    override fun getAll(): List<Game>

    @Query("DELETE FROM games")
    override fun deleteAll()
}

@Singleton
@Dao
internal interface PlayerDao: RoomDao<Player> {

    // overridden functions

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override suspend fun create(data: Player): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun update(data: Player): Int

    @Query("SELECT * FROM player ORDER BY id DESC LIMIT 1")
    override suspend fun read(): Player

    @Query("SELECT * FROM player WHERE id = :id")
    override fun observe(id: String): LiveData<Player>

    // functions

    @Query("UPDATE player SET name = :name WHERE id = :id")
    suspend fun updateName(id: String, name: String): Int
}

@Singleton
@Dao
internal interface PlayerMigrateDao: HunterGamingMigrateDao<Player> {

    // overridden functions

    @Query("SELECT * FROM player")
    override fun getAll(): List<Player>

    @Query("DELETE FROM player")
    override fun deleteAll()
}

interface HunterGamingMigrateDao<T> {
    fun getAll(): List<T>
    fun deleteAll()
}

interface RoomDao<T> {
    suspend fun create(data: T): Long
    suspend fun update(data: T): Int
    suspend fun read(): T
    fun observe(id: String): LiveData<T>
}