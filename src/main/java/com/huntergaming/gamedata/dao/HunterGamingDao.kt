package com.huntergaming.gamedata.dao

interface HunterGamingDao<T> {
    suspend fun create(data: T)
    suspend fun update(data: T)
    suspend fun read(): T
}

interface HunterGamingMigrateDao {
    suspend fun migrateDataToFirestore()
    suspend fun migrateDataToRoom()
}