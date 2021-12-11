package com.huntergaming.gamedata.preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val PREFS_DATA_CONSENT = "dataConsent"
private const val PREFS_USE_FIREBASE = "firebaseOrRoom"

class HunterGamingPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) : DataConsentPreferences,
    FirebasePreferences {

    private val playerSettingsPreferences = context.getSharedPreferences("HunterGaming", Context.MODE_PRIVATE)

    override fun shownDataConsent(): Boolean = !playerSettingsPreferences.contains(PREFS_DATA_CONSENT)
    override fun updateDataConsent() = playerSettingsPreferences.edit().putBoolean(PREFS_DATA_CONSENT, false).apply()

    override fun setCanUseFirebase(consent: Boolean) = playerSettingsPreferences.edit().putBoolean(PREFS_USE_FIREBASE, consent).apply()
    override fun canUseFirebase(): Boolean = playerSettingsPreferences.getBoolean(PREFS_USE_FIREBASE, false)
}

interface DataConsentPreferences {
    fun shownDataConsent(): Boolean
    fun updateDataConsent()
}

interface FirebasePreferences {
    fun setCanUseFirebase(consent: Boolean)
    fun canUseFirebase(): Boolean
}