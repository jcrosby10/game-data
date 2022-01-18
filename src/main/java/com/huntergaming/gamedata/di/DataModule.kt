package com.huntergaming.gamedata.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.huntergaming.gamedata.GameRepo
import com.huntergaming.gamedata.HunterGamingDatabase
import com.huntergaming.gamedata.HunterGamingRepository
import com.huntergaming.gamedata.PlayerRepo
import com.huntergaming.gamedata.dao.FirestoreGameDao
import com.huntergaming.gamedata.dao.FirestorePlayerDao
import com.huntergaming.gamedata.dao.FirestoreDao
import com.huntergaming.gamedata.dao.RoomDao
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import com.huntergaming.gamedata.preferences.DataConsentPreferences
import com.huntergaming.gamedata.preferences.FirebasePreferences
import com.huntergaming.gamedata.preferences.HunterGamingPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class DataModule {

    @Provides
    fun providePlayerRepo(repository: HunterGamingRepository): PlayerRepo = repository

    @Provides
    fun provideGameRepo(repository: HunterGamingRepository): GameRepo = repository

    @Provides
    fun provideDataConsentPreferences(preferences: HunterGamingPreferences): DataConsentPreferences = preferences

    @Provides
    fun provideFirebasePreferences(preferences: HunterGamingPreferences): FirebasePreferences = preferences

    @Provides
    internal fun provideSolitaireDatabase(@ApplicationContext context: Context): HunterGamingDatabase = Room.databaseBuilder(
        context,
        HunterGamingDatabase::class.java,
        "classic_solitaire_database"
    ).build()

    @Provides
    internal fun provideGameDao(db: HunterGamingDatabase): RoomDao<Game> = db.getGameDao()

    @Provides
    internal fun providePlayerDao(db: HunterGamingDatabase): RoomDao<Player> = db.getPlayerDao()

    @Provides
    internal fun provideFirestoreDb(): FirebaseFirestore = Firebase.firestore

    @Provides
    internal fun providePlayerFirebaseDao(dao: FirestoreDao): FirestorePlayerDao = dao

    @Provides
    internal fun provideGameFirebaseDao(dao: FirestoreDao): FirestoreGameDao = dao
}