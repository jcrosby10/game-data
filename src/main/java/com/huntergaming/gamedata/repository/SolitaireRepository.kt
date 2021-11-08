package com.huntergaming.gamedata.repository

import com.huntergaming.gamedata.data.GameDao
import com.huntergaming.gamedata.data.HunterGamingFirebaseDao
import com.huntergaming.gamedata.data.PlayerDao
import com.huntergaming.gamedata.data.PlayerSettingsDao
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import com.huntergaming.gamedata.model.PlayerSettings
import com.huntergaming.gamedata.preferences.HunterGamingPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * The entry point for interacting with the database.
 */
class SolitaireRepository @Inject constructor(
    private val hunterGamingPreferences: HunterGamingPreferences
) {

    // to avoid errors about exposing internal class in public class
    @Inject
    internal lateinit var hunterGamingFirebaseDao: HunterGamingFirebaseDao

    @Inject
    internal lateinit var playerDao: PlayerDao

    @Inject
    internal lateinit var gameDao: GameDao

    @Inject
    internal lateinit var playerSettingsDao: PlayerSettingsDao

    suspend fun getPlayerSettings(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        runCatching {
            val settings =
                if (hunterGamingPreferences.canUseFirebase()) hunterGamingFirebaseDao.getPlayerSettings()
                else playerSettingsDao.read()

            emit(DataRequestState.Success(settings))
        }
            .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

    suspend fun updatePlayerSettings(playerSettings: PlayerSettings): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)
            runCatching {
                if (hunterGamingPreferences.canUseFirebase()) hunterGamingFirebaseDao.updatePlayerSettings(playerSettings)
                else playerSettingsDao.update(playerSettings)

                emit(DataRequestState.Success(Unit))
            }
                .getOrElse { emit(DataRequestState.Error(it.message)) }
    }

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
            emit(DataRequestState.Success(playerDao.get()))
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
            emit(DataRequestState.Success(gameDao.get(id)))
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