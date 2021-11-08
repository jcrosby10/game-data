package com.huntergaming.gamedata.preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val PREFS_DATA_CONSENT = "dataConsent"
private const val PREFS_USE_FIREBASE = "firebaseOrRoom"

class HunterGamingPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val playerSettingsPreferences = context.getSharedPreferences("HunterGamingPlayerSettings", Context.MODE_PRIVATE)

    fun shownDataConsent(): Boolean = !playerSettingsPreferences.contains(PREFS_DATA_CONSENT)
    fun updateDataConsent() = playerSettingsPreferences.edit().putBoolean(PREFS_DATA_CONSENT, false).apply()

    fun setCanUseFirebase(consent: Boolean) = playerSettingsPreferences.edit().putBoolean(PREFS_USE_FIREBASE, consent).apply()
    fun canUseFirebase(): Boolean = playerSettingsPreferences.getBoolean(PREFS_USE_FIREBASE, false)
}