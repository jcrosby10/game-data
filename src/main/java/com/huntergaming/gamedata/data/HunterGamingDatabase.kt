package com.huntergaming.gamedata.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import com.huntergaming.gamedata.model.PlayerSettings
import javax.inject.Singleton

@Singleton
@Database(entities = [Player::class, Game::class, PlayerSettings::class], version = 1)
internal abstract class HunterGamingDatabase: RoomDatabase() {
    abstract fun getGameDao(): GameDao
    abstract fun getPlayerDao(): PlayerDao
    abstract fun getPlayerSettingsDao(): PlayerSettingsDao
}