package com.huntergaming.gamedata.dao

interface HunterGamingDao<T> {
    suspend fun create(data: T): Long
    suspend fun update(data: T): Int
    suspend fun read(): T
}

interface HunterGamingMigrateDao {
    suspend fun migrateData()
}