package com.huntergaming.gamedata

import android.util.Log
import com.huntergaming.gamedata.dao.FirestoreGameDao
import com.huntergaming.gamedata.dao.FirestorePlayerDao
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HunterGamingRepository @Inject constructor(
    private val playerFirebaseDao: FirestorePlayerDao,
    private val gameFirebaseDao: FirestoreGameDao
) : PlayerRepo,
    GameRepo {

    // companion objects

    companion object {
        private const val LOG_TAG = "HunterGamingRepository"
    }

    // overridden functions

    override suspend fun create(id: String, name: String, email: String): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {

            val success = playerFirebaseDao.create(id, name, email) != null
            emit(DataRequestState.Success(success))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message!!))
            }
    }

    override suspend fun update(player: Player): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {

            val successful = playerFirebaseDao.update(player)
            emit(DataRequestState.Success(successful))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message!!))
            }
    }

    override suspend fun getPlayer(id: String): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {

            val player = playerFirebaseDao.read(id)
            emit(DataRequestState.Success(player))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message!!))
            }
    }

    override suspend fun create(id: String): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {

            val successful = gameFirebaseDao.create(id) != null
            emit(DataRequestState.Success(successful))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message!!))
            }
    }

    override suspend fun update(game: Game): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {

            val successful = gameFirebaseDao.update(game)
            emit(DataRequestState.Success(successful))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message!!))
            }
    }

    override suspend fun getMostRecentGame(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {

            val game = gameFirebaseDao.getMostRecentGame()
            emit(DataRequestState.Success(game))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message!!))
            }
    }

    override suspend fun getHighScoreGame(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {

            val game = gameFirebaseDao.getHighScoreGame()
            emit(DataRequestState.Success(game))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message!!))
            }
    }

    override suspend fun getTopTenGames(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {

            val games = gameFirebaseDao.getTopTenGames()
            emit(DataRequestState.Success(games))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message!!))
            }
    }
}

// interfaces/classes

enum class MigrateState {
    STARTED, COMPLETED
}

interface PlayerRepo {
    suspend fun create(id: String, name: String, email: String): Flow<DataRequestState>
    suspend fun update(player: Player): Flow<DataRequestState>
    suspend fun getPlayer(id: String): Flow<DataRequestState>
}

interface GameRepo {
    suspend fun create(id: String): Flow<DataRequestState>
    suspend fun update(game: Game): Flow<DataRequestState>
    suspend fun getMostRecentGame(): Flow<DataRequestState>
    suspend fun getHighScoreGame(): Flow<DataRequestState>
    suspend fun getTopTenGames(): Flow<DataRequestState>
}

sealed class DataRequestState {
    object NoInternet: DataRequestState()
    object InProgress: DataRequestState()
    data class Success<T>(val data: T): DataRequestState()
    class Error (val message: String): DataRequestState()
}