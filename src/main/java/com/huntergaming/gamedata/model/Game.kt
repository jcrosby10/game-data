package com.huntergaming.gamedata.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.inject.Inject

/**
 * Data class for a game.
 */
@Entity(tableName = "games")
data class Game @Inject constructor(
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    var score: Int
) {
    companion object {

        @Volatile
        @JvmStatic
        private var INSTANCE: Game? = null

        @JvmStatic
        @JvmOverloads
        fun getInstance(id: Int = INSTANCE?.id!!, score: Int = INSTANCE?.score!!): Game = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Game(id, score).also { INSTANCE = it }
        }
    }
}