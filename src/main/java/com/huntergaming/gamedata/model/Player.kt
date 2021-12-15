package com.huntergaming.gamedata.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class Player constructor(
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
        private lateinit var INSTANCE: Player

        @JvmStatic
        fun getInstance(): Player {
            INSTANCE = Player() //  TODO models should not be singletons
        }

        @JvmStatic
        fun updateInstance(
            id: Int,
            firstName: String,
            lastName: String,
            gamesPlayed: Int,
            averageScore: Int,
            topScore: Int
        ): Player {
            INSTANCE = Player(
                id,
                firstName,
                lastName,
                gamesPlayed,
                averageScore,
                topScore
            )
            return INSTANCE
        }
    }
}