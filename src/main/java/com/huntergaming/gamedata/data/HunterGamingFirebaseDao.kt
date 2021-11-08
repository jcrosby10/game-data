package com.huntergaming.gamedata.data

import com.google.firebase.firestore.FirebaseFirestore
import com.huntergaming.gamedata.model.PlayerSettings
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class HunterGamingFirebaseDao @Inject constructor(
    private val db: FirebaseFirestore
) : HunterGamingMigrateDao {

    override suspend fun migrateData() {
        TODO("Migrate from Firestore to Room")
    }

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

    suspend fun updatePlayerSettings(playerSettings: PlayerSettings) = withContext(Dispatchers.IO) {
        db.collection("").document().set(playerSettings)
            .addOnFailureListener {
                throw IOException(it)
            }
    }
}