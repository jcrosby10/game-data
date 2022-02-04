package com.huntergaming.gamedata.model

data class Player constructor(

    var id: String,
    var name: String,
    var email: String,
    var gamesPlayed: Int = 0,
    var averageScore: Int = 0,
    var topScore: Int = 0
)