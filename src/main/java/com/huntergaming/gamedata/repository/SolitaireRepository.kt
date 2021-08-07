package com.huntergaming.gamedata.repository

import com.huntergaming.gamedata.data.ClassicSolitaireCache
import com.huntergaming.gamedata.data.GameDao
import com.huntergaming.gamedata.data.PlayerDao
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * The entry point for interacting with the database.
 */
class SolitaireRepository @Inject constructor(
    private val playerDao: PlayerDao,
    private val gameDao: GameDao,
    private val playerCache: ClassicSolitaireCache<String, Player>,
    private val gameCache: ClassicSolitaireCache<String, Game>
) {

    private val playerKey = "player"
    private val gameKey = "game"

    /**
     * Create a new player.
     *
     * @param player the player to save to the database.
     * @return A flow of RequestState indicating success/error.
     */
    suspend fun create(player: Player): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            playerDao.create(player)
            playerCache.add(playerKey, player)
            emit(DataRequestState.Success(Unit))
        }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

    /**
     * Update a player in the database with new data.
     *
     * @param player the new data to save.
     * @return A flow of RequestState indicating success/error.
     */
    suspend fun update(player: Player): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            playerDao.update(player)
            playerCache.add(playerKey, player)
            emit(DataRequestState.Success(Unit))
        }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

    /**
     * Get the player from the database.
     * @return A flow of RequestState indicating success/error.
     */
    suspend fun getPlayer(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            if (playerCache.contains(playerKey)) emit(DataRequestState.Success(playerCache.get(playerKey)))
            else playerCache.add(playerKey, playerDao.get())

            emit(DataRequestState.Success(playerCache.get(playerKey)))
        }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

    /**
     * Create a new game.
     *
     * @param game the game to save to the database.
     * @return A flow of RequestState indicating success/error.
     */
    suspend fun create(game: Game): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            gameDao.create(game)
            gameCache.add(gameKey, game)
            emit(DataRequestState.Success(Unit))
        }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

    /**
     * Update a game in the database with new data.
     *
     * @param game the new data to save.
     * @return A flow of RequestState indicating success/error.
     */
    suspend fun update(game: Game): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            gameDao.create(game)
            gameCache.add(gameKey, game)
            emit(DataRequestState.Success(Unit))
        }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

    /**
     * Get a game from the database.
     * @return A flow of RequestState indicating success/error.
     */
    suspend fun getGame(id: Int): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            if (gameCache.contains(gameKey)) emit(DataRequestState.Success(gameCache.get(gameKey)))
            else gameCache.add(gameKey, gameDao.get(id))

            emit(DataRequestState.Success(gameCache.get(gameKey)))
        }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

    /**
     * Get the players highest scoring game from the database.
     * @return A flow of RequestState indicating success/error.
     */
    suspend fun getHighScoreGame(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching { emit(DataRequestState.Success(gameDao.getHighScoreGame())) }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

    /**
     * Get the players top ten highest scoring games from the database.
     * @return A flow of RequestState indicating success/error.
     */
    suspend fun getTopTenGames(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching { emit(DataRequestState.Success(gameDao.getTopTenGames())) }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }
}

sealed class DataRequestState {
    object NoInternet: DataRequestState()
    object InProgress: DataRequestState()
    data class Success<T>(val data: T): DataRequestState()
    class Error(val message: String?): DataRequestState()
}