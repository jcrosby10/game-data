package com.huntergaming.gamedata

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseException
import com.huntergaming.gamedata.dao.FirestoreGameDao
import com.huntergaming.gamedata.dao.FirestorePlayerDao
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import com.huntergaming.ui.uitl.DataRequestState
import com.huntergaming.ui.uitl.CommunicationAdapter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HunterGamingRepository @Inject constructor(
    private val playerFirebaseDao: FirestorePlayerDao,
    private val gameFirebaseDao: FirestoreGameDao,
    private val communicationAdapter: CommunicationAdapter,
    @ApplicationContext private val context: Context
) : PlayerRepo,
    GameRepo {

    // companion objects

    companion object {
        private const val LOG_TAG = "HunterGamingRepository"
    }

    // overridden player functions

    override suspend fun create(id: String, name: String, email: String): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        try {
            val success = playerFirebaseDao.create(id, name, email) != null
            emit(DataRequestState.Success(success))
        }
        catch (e: FirebaseException) {
            Log.e(LOG_TAG, e.message, e)
            communicationAdapter.message.value?.add(context.getString(R.string.error_create_account))
        }
    }

    override suspend fun update(player: Player): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        try {
            val successful = playerFirebaseDao.update(player)
            emit(DataRequestState.Success(successful))
        }
        catch (e: FirebaseException) {
            Log.e(LOG_TAG, e.message, e)
            communicationAdapter.message.value?.add(context.getString(R.string.error_update_player))
        }
    }

    override suspend fun updateName(id: String, name: String): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        try {
            playerFirebaseDao.updateName(id, name)
        }
        catch (e: FirebaseException) {
            Log.e(LOG_TAG, e.message, e)
            communicationAdapter.message.value?.add(context.getString(R.string.error_update_name))
        }
    }

    override suspend fun getPlayer(id: String): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        try {
            val player = playerFirebaseDao.read(id)
            emit(DataRequestState.Success(player))
        }
        catch (e: FirebaseException) {
            Log.e(LOG_TAG, e.message, e)
            communicationAdapter.message.value?.add(context.getString(R.string.error_get_player))
        }
    }

    override suspend fun observePlayer(id: String): StateFlow<Player?> {
        return try {
            playerFirebaseDao.observePlayer(id)
        }
        catch (e: FirebaseException) {
            Log.e(LOG_TAG, e.message, e)

            communicationAdapter.message.value?.add(context.getString(R.string.error_get_player))
            MutableStateFlow(null)
        }
    }

    // overridden game functions

    override suspend fun create(id: String): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        try {
            val successful = gameFirebaseDao.create(id) != null
            emit(DataRequestState.Success(successful))
        }
        catch (e: FirebaseException) {
            Log.e(LOG_TAG, e.message, e)
            communicationAdapter.message.value?.add(context.getString(R.string.error_create_game))
        }
    }

    override suspend fun update(game: Game): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        try {
            val successful = gameFirebaseDao.update(game)
            emit(DataRequestState.Success(successful))
        }
        catch (e: FirebaseException) {
            Log.e(LOG_TAG, e.message, e)
            communicationAdapter.message.value?.add(context.getString(R.string.error_update_game))
        }
    }

    override suspend fun getMostRecentGame(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        try {
            val game = gameFirebaseDao.getMostRecentGame()
            emit(DataRequestState.Success(game))
        }
        catch (e: FirebaseException) {
            Log.e(LOG_TAG, e.message, e)
            communicationAdapter.message.value?.add(context.getString(R.string.error_load_game))
        }
    }

    override suspend fun getTopTenGames(): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        try {
            val games = gameFirebaseDao.getTopTenGames()
            emit(DataRequestState.Success(games))
        }
        catch (e: FirebaseException) {
            Log.e(LOG_TAG, e.message, e)
            communicationAdapter.message.value?.add(context.getString(R.string.error_high_score))
        }
    }

    override suspend fun observeGame(id: String): StateFlow<Game?> = gameFirebaseDao.observeGame(id)
}

// interfaces/classes

interface PlayerRepo {
    suspend fun create(id: String, name: String, email: String): Flow<DataRequestState>
    suspend fun update(player: Player): Flow<DataRequestState>
    suspend fun updateName(id: String, name: String): Flow<DataRequestState>
    suspend fun getPlayer(id: String): Flow<DataRequestState>
    suspend fun observePlayer(id: String): StateFlow<Player?>
}

interface GameRepo {
    suspend fun create(id: String): Flow<DataRequestState>
    suspend fun update(game: Game): Flow<DataRequestState>
    suspend fun getMostRecentGame(): Flow<DataRequestState>
    suspend fun getTopTenGames(): Flow<DataRequestState>
    suspend fun observeGame(id: String): StateFlow<Game?>
}