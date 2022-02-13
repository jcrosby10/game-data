package com.huntergaming.gamedata.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Game constructor(

    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    @PrimaryKey(autoGenerate = false)
    var id: String,

    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    var score: Int = 0,

    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    var time: Long = System.currentTimeMillis()
)