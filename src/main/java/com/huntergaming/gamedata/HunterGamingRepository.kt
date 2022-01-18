package com.huntergaming.gamedata

import android.util.Log
import com.huntergaming.gamedata.dao.FirestoreGameDao
import com.huntergaming.gamedata.dao.FirestorePlayerDao
import com.huntergaming.gamedata.dao.GameDao
import com.huntergaming.gamedata.dao.RoomDao
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import com.huntergaming.gamedata.preferences.FirebasePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HunterGamingRepository @Inject constructor(
    private val playerDao: RoomDao<Player>,
    private val firebasePreferences: FirebasePreferences,
    private val gameDao: RoomDao<Game>,
    private val playerFirebaseDao: FirestorePlayerDao,
    private val gameFirebaseDao: FirestoreGameDao
) : PlayerRepo,
    GameRepo,
    MigrateRepo {

    // COMPANION OBJECTS

    companion object {
        private const val LOG_TAG = "HunterGamingRepository"
    }

    // OVERRIDDEN FUNCTIONS

    override suspend fun create(id: String, name: String, email: String): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            val success =
                if (firebasePreferences.canUseFirebase()) playerFirebaseDao.create(id, name, email) != null
                else playerDao.create(Player(id, name, email)) > 0

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
            val successful =
                if (firebasePreferences.canUseFirebase()) playerFirebaseDao.update(player)
                else playerDao.update(player) > 0

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
            val player =
                if (firebasePreferences.canUseFirebase()) playerFirebaseDao.read(id)
                else playerDao.read()

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
            val successful =
                if (firebasePreferences.canUseFirebase()) gameFirebaseDao.create(id) != null
                else gameDao.create(Game(id)) > 0

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
            val successful =
                if (firebasePreferences.canUseFirebase()) gameFirebaseDao.update(game)
                else gameDao.update(game) > 0

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
            val game =
                if (firebasePreferences.canUseFirebase()) gameFirebaseDao.getMostRecentGame()
                else gameDao.read()

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
            val game =
                if (firebasePreferences.canUseFirebase()) gameFirebaseDao.getHighScoreGame()
                else (gameDao as GameDao).getHighScoreGame()

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
            val games =
                if (firebasePreferences.canUseFirebase()) gameFirebaseDao.getTopTenGames()
                else (gameDao as GameDao).getTopTenGames()

            emit(DataRequestState.Success(games))
        }
            .getOrElse {
                Log.e(LOG_TAG, it.message, it)
                emit(DataRequestState.Error(it.message!!))
            }
    }

    override suspend fun migrateDataToFirestore() {
        TODO("Not yet implemented")
    }

    override suspend fun migrateDataToRoom() {
        TODO("Not yet implemented")
    }
}

// INTERFACES/CLASSES

interface MigrateRepo {
    suspend fun migrateDataToFirestore()
    suspend fun migrateDataToRoom()
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