package com.huntergaming.gamedata

import com.huntergaming.gamedata.dao.GameDao
import com.huntergaming.gamedata.dao.HunterGamingDao
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HunterGamingRepository @Inject constructor(
    private val playerDao: HunterGamingDao<Player>,
    private val gameDao: HunterGamingDao<Game>
) : PlayerRepo,
    GameRepo {

    override suspend fun create(player: Player): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            playerDao.create(player)
            emit(DataRequestState.Success(Unit))
        }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

    override suspend fun update(player: Player): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            playerDao.update(player)
            emit(DataRequestState.Success(Unit))
        }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

    override suspend fun getPlayer(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            emit(DataRequestState.Success(playerDao.read()))
        }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

    override suspend fun create(game: Game): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            gameDao.create(game)
            emit(DataRequestState.Success(Unit))
        }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

    override suspend fun update(game: Game): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            gameDao.create(game)
            emit(DataRequestState.Success(Unit))
        }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

    override suspend fun getMostRecentGame(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            emit(DataRequestState.Success(gameDao.read()))
        }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

    override suspend fun getHighScoreGame(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching { emit(DataRequestState.Success((gameDao as GameDao).getHighScoreGame())) }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

    override suspend fun getTopTenGames(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching { emit(DataRequestState.Success((gameDao as GameDao).getTopTenGames())) }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }
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