package com.huntergaming.gamedata.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Game constructor(

    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    var score: Int
)