package com.huntergaming.gamedata.model

import javax.inject.Inject

/**
 * Data class for the player.
 */
data class Player @Inject constructor(
    val firstName: String,
    val lastName: String,
    val gamesPlayed: Int,
    val averageScore: Int
)