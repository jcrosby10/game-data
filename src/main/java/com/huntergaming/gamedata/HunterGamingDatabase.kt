package com.huntergaming.gamedata

import androidx.room.Database
import androidx.room.RoomDatabase
import com.huntergaming.gamedata.dao.GameDao
import com.huntergaming.gamedata.dao.PlayerDao
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import javax.inject.Singleton

@Singleton
@Database(entities = [Player::class, Game::class], version = 1)
internal abstract class HunterGamingDatabase: RoomDatabase() {
    abstract fun getGameDao(): GameDao
    abstract fun getPlayerDao(): PlayerDao
}