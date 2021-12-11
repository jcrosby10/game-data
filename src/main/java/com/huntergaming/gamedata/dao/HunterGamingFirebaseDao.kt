package com.huntergaming.gamedata.dao

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

internal class HunterGamingFirebaseDao @Inject constructor(
    private val db: FirebaseFirestore
) : HunterGamingMigrateDao {

    override suspend fun migrateDataToFirestore() {
        TODO("Migrate from Firestore to Room")
    }

    override suspend fun migrateDataToRoom() {
        TODO("Migrate data from Firestore to Room")
    }
}