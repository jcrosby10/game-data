package com.huntergaming.gamedata.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.huntergaming.gamedata.data.GameDao
import com.huntergaming.gamedata.data.PlayerDao
import com.huntergaming.gamedata.data.PlayerSettingsDao
import com.huntergaming.gamedata.data.HunterGamingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class DataModule {

    @Provides
    internal fun provideSolitaireDatabase(@ApplicationContext context: Context): HunterGamingDatabase = Room.databaseBuilder(
        context,
        HunterGamingDatabase::class.java,
        "classic_solitaire_database"
    ).build()

    @Provides
    internal fun provideGameDao(db: HunterGamingDatabase): GameDao = db.getGameDao()

    @Provides
    internal fun providePlayerDao(db: HunterGamingDatabase): PlayerDao = db.getPlayerDao()

    @Provides
    internal fun providePlayerSettingsDao(db: HunterGamingDatabase): PlayerSettingsDao = db.getPlayerSettingsDao()

    @Provides
    internal fun provideFirestoreDb(): FirebaseFirestore = Firebase.firestore
}