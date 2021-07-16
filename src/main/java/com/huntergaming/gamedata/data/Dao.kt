package com.huntergaming.gamedata.data

import com.huntergaming.gamedata.RequestState
import kotlinx.coroutines.flow.Flow

interface Dao<T> {
    suspend fun create(data: T): Flow<RequestState>
    fun update(data: T)
    fun get(): T
    fun delete()
}