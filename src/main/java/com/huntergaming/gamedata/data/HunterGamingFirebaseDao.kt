package com.huntergaming.gamedata.data

import com.google.firebase.firestore.FirebaseFirestore
import com.huntergaming.gamedata.model.PlayerSettings
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class HunterGamingFirebaseDao @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun getPlayerSettings(): PlayerSettings? = withContext(Dispatchers.IO) {
        var settings: PlayerSettings? = null

        db.collection("").document().get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    settings = it.result?.toObject(PlayerSettings::class.java)
                }
            }

        settings
    }

    suspend fun updatePlayerSettings(playerSettings: PlayerSettings) {

    }
}