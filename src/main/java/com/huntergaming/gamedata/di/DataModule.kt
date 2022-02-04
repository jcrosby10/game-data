package com.huntergaming.gamedata.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.huntergaming.gamedata.GameRepo
import com.huntergaming.gamedata.HunterGamingRepository
import com.huntergaming.gamedata.PlayerRepo
import com.huntergaming.gamedata.dao.FirestoreGameDao
import com.huntergaming.gamedata.dao.FirestorePlayerDao
import com.huntergaming.gamedata.dao.FirestoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun providePlayerRepo(repository: HunterGamingRepository): PlayerRepo = repository

    @Provides
    fun provideGameRepo(repository: HunterGamingRepository): GameRepo = repository

    @Provides
    internal fun provideFirestoreDb(): FirebaseFirestore = Firebase.firestore

    @Provides
    internal fun providePlayerFirebaseDao(dao: FirestoreDao): FirestorePlayerDao = dao

    @Provides
    internal fun provideGameFirebaseDao(dao: FirestoreDao): FirestoreGameDao = dao
}