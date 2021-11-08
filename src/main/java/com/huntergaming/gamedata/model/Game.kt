package com.huntergaming.gamedata.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class for a game.
 */
@Entity(tableName = "games")
data class Game constructor(
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    var score: Int
) {
    companion object {

        @Volatile
        @JvmStatic
        private lateinit var INSTANCE: Game

        @JvmStatic
        fun getInstance(): Game = INSTANCE

        @JvmStatic
        fun updateInstance(id: Int, score: Int): Game {
            INSTANCE = Game(id, score)
            return INSTANCE
        }
    }
}