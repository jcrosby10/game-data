package com.huntergaming.gamedata.di

import android.content.Context
import androidx.room.Room
import com.huntergaming.gamedata.data.GameDao
import com.huntergaming.gamedata.data.PlayerDao
import com.huntergaming.gamedata.data.SolitaireDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class DataModule {

    @Provides
    internal fun provideSolitaireDatabase(@ApplicationContext context: Context): SolitaireDatabase = Room.databaseBuilder(
        context,
        SolitaireDatabase::class.java,
        "classic_solitaire_database"
    ).build()

    @Provides
    internal fun provideGameDao(db: SolitaireDatabase): GameDao = db.getGameDao()

    @Provides
    internal fun providePlayerDao(db: SolitaireDatabase): PlayerDao = db.getPlayerDao()
}