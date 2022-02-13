package com.huntergaming.gamedata.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// extensions

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "GameDataStore")

class GameDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

}