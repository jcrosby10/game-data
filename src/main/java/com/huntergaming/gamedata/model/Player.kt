package com.huntergaming.gamedata.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.inject.Inject

/**
 * Data class for the player.
 */
@Entity(tableName = "player")
data class Player @Inject constructor(
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "fires_name", typeAffinity = ColumnInfo.TEXT)
    var firstName: String,

    @ColumnInfo(name = "last_name", typeAffinity = ColumnInfo.TEXT)
    var lastName: String,

    @ColumnInfo(name = "games_played", typeAffinity = ColumnInfo.INTEGER)
    var gamesPlayed: Int,

    @ColumnInfo(name = "average_score", typeAffinity = ColumnInfo.INTEGER)
    var averageScore: Int,

    @ColumnInfo(name = "top_score", typeAffinity = ColumnInfo.INTEGER)
    var topScore: Int
) {
    companion object {

        @Volatile
        @JvmStatic
        private var INSTANCE: Player? = null

        @JvmStatic
        @JvmOverloads
        fun getInstance(
            id: Int = INSTANCE?.id!!,
            firstName: String = INSTANCE?.firstName!!,
            lastName: String = INSTANCE?.lastName!!,
            gamesPlayed: Int = INSTANCE?.gamesPlayed!!,
            averageScore: Int = INSTANCE?.averageScore!!,
            topScore: Int = INSTANCE?.topScore!!
        ): Player = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Player(id, firstName, lastName, gamesPlayed, averageScore, topScore).also { INSTANCE = it }
        }
    }
}