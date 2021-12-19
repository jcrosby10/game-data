package com.huntergaming.gamedata.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class Player constructor(

    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,

    @ColumnInfo(name = "fires_name", typeAffinity = ColumnInfo.TEXT)
    var firstName: String,

    @ColumnInfo(name = "last_name", typeAffinity = ColumnInfo.TEXT)
    var lastName: String,

    @ColumnInfo(name = "games_played", typeAffinity = ColumnInfo.INTEGER)
    var gamesPlayed: Int = 0,

    @ColumnInfo(name = "average_score", typeAffinity = ColumnInfo.INTEGER)
    var averageScore: Int = 0,

    @ColumnInfo(name = "top_score", typeAffinity = ColumnInfo.INTEGER)
    var topScore: Int = 0
)