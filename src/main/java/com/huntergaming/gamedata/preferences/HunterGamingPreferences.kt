package com.huntergaming.gamedata.preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HunterGamingPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // companion objects

    companion object {
        private const val PREFS_DATA_CONSENT = "dataConsent"
        private const val PREFS_USE_FIREBASE = "firebaseOrRoom"
    }

    // properties

    // todo change to data store type safety version
    private val playerSettingsPreferences = context.getSharedPreferences("HunterGaming", Context.MODE_PRIVATE)
}