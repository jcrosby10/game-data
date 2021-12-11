package com.huntergaming.gamedata

import android.util.Log
import com.huntergaming.gamedata.dao.GameDao
import com.huntergaming.gamedata.dao.GameFirebaseDao
import com.huntergaming.gamedata.dao.HunterGamingDao
import com.huntergaming.gamedata.dao.PlayerFirebaseDao
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import com.huntergaming.gamedata.preferences.FirebasePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HunterGamingRepository @Inject constructor(
    private val playerDao: HunterGamingDao<Player>,
    private val firebasePreferences: FirebasePreferences,
    private val gameDao: HunterGamingDao<Game>,
    private val playerFirebaseDao: PlayerFirebaseDao,
    private val gameFirebaseDao: GameFirebaseDao
) : PlayerRepo,
    GameRepo,
    MigrateRepo {

    companion object {
        private const val LOG_TAG = "HunterGamingRepository"
    }

    override suspend fun create(player: Player): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            val successful =
                if (firebasePreferences.canUseFirebase()) playerFirebaseDao.create(player)
                else playerDao.create(player) > 0

            emit(DataRequestState.Success(successful))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message))
            }
    }

    override suspend fun update(player: Player): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            val successful =
                if (firebasePreferences.canUseFirebase()) playerFirebaseDao.update(player)
                else playerDao.update(player) > 0

            emit(DataRequestState.Success(successful))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message))
            }
    }

    override suspend fun getPlayer(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            val player =
                if (firebasePreferences.canUseFirebase()) playerFirebaseDao.getPlayer()
                else playerDao.read()

            emit(DataRequestState.Success(player))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message))
            }
    }

    override suspend fun create(game: Game): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            val successful =
                if (firebasePreferences.canUseFirebase()) gameFirebaseDao.create(game)
                else gameDao.create(game) > 0

            emit(DataRequestState.Success(successful))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message))
            }
    }

    override suspend fun update(game: Game): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            val successful =
                if (firebasePreferences.canUseFirebase()) gameFirebaseDao.update(game)
                else gameDao.update(game) > 0

            emit(DataRequestState.Success(successful))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message))
            }
    }

    override suspend fun getMostRecentGame(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            val game =
                if (firebasePreferences.canUseFirebase()) gameFirebaseDao.getMostRecentGame()
                else gameDao.read()

            emit(DataRequestState.Success(game))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message))
            }
    }

    override suspend fun getHighScoreGame(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            val game =
                if (firebasePreferences.canUseFirebase()) gameFirebaseDao.getHighScoreGame()
                else (gameDao as GameDao).getHighScoreGame()

            emit(DataRequestState.Success(game))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message))
            }
    }

    override suspend fun getTopTenGames(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            val games =
                if (firebasePreferences.canUseFirebase()) gameFirebaseDao.getTopTenGames()
                else (gameDao as GameDao).getTopTenGames()

            emit(DataRequestState.Success(games))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message))
            }
    }

    override suspend fun migrateDataToFirestore() {
        TODO("Not yet implemented")
    }

    override suspend fun migrateDataToRoom() {
        TODO("Not yet implemented")
    }
}

interface MigrateRepo {
    suspend fun migrateDataToFirestore()
    suspend fun migrateDataToRoom()
}

interface PlayerRepo {
    suspend fun create(player: Player): Flow<DataRequestState>
    suspend fun update(player: Player): Flow<DataRequestState>
    suspend fun getPlayer(): Flow<DataRequestState>
}

interface GameRepo {
    suspend fun create(game: Game): Flow<DataRequestState>
    suspend fun update(game: Game): Flow<DataRequestState>
    suspend fun getMostRecentGame(): Flow<DataRequestState>
    suspend fun getHighScoreGame(): Flow<DataRequestState>
    suspend fun getTopTenGames(): Flow<DataRequestState>
}

sealed class DataRequestState {
    object NoInternet: DataRequestState()
    object InProgress: DataRequestState()
    data class Success<T>(val data: T): DataRequestState()
    class Error(val message: String?): DataRequestState()
}