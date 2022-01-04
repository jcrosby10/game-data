package com.huntergaming.gamedata.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class Player constructor(

    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    @PrimaryKey(autoGenerate = false)
    var id: String,

    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    var name: String,

    @ColumnInfo(name = "email", typeAffinity = ColumnInfo.TEXT)
    var email: String,

    @ColumnInfo(name = "games_played", typeAffinity = ColumnInfo.INTEGER)
    var gamesPlayed: Int = 0,

    @ColumnInfo(name = "average_score", typeAffinity = ColumnInfo.INTEGER)
    var averageScore: Int = 0,

    @ColumnInfo(name = "top_score", typeAffinity = ColumnInfo.INTEGER)
    var topScore: Int = 0
)