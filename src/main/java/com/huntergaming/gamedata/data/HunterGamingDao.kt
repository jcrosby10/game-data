package com.huntergaming.gamedata.data

internal interface HunterGamingDao<T> {
    suspend fun create(data: T)
    suspend fun update(data: T)
}

internal interface HunterGamingMigrateDao {
    suspend fun migrateData()
}