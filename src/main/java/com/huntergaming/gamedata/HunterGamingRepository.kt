package com.huntergaming.gamedata

import android.content.Context
import android.util.Log
import androidx.lifecycle.asFlow
import com.google.firebase.FirebaseException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.huntergaming.gamedata.dao.FirestoreGameDao
import com.huntergaming.gamedata.dao.FirestorePlayerDao
import com.huntergaming.gamedata.dao.GameDao
import com.huntergaming.gamedata.dao.HunterGamingMigrateDao
import com.huntergaming.gamedata.dao.PlayerDao
import com.huntergaming.gamedata.dao.RoomDao
import com.huntergaming.gamedata.model.Game
import com.huntergaming.gamedata.model.Player
import com.huntergaming.ui.uitl.DataRequestState
import com.huntergaming.ui.uitl.CommunicationAdapter
import com.huntergaming.web.InternetConnectionStatus
import com.huntergaming.web.InternetStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HunterGamingRepository @Inject constructor(
    private val playerFirebaseDao: FirestorePlayerDao,
    private val gameFirebaseDao: FirestoreGameDao,
    private val playerRoomDao: RoomDao<Player>,
    private val gameRoomDao: RoomDao<Game>,
    private val migrateGame: HunterGamingMigrateDao<Game>,
    private val migratePlayer: HunterGamingMigrateDao<Player>,
    private val communicationAdapter: CommunicationAdapter,
    private val internetStatus: InternetStatus,
    @ApplicationContext private val context: Context
) : PlayerRepo,
    GameRepo {

    // companion objects

    companion object {
        private const val LOG_TAG = "HunterGamingRepository"
    }

    // properties

    private var previouslyDisconnected = false
    private var saveInRoom = true

    // initializers

    init {
        CoroutineScope(Dispatchers.Default).launch {
            internetStatus.internetConnectionStatus.collect {
                when (it) {
                    InternetConnectionStatus.UNKNOWN -> {}

                    InternetConnectionStatus.CONNECTED -> {
                        saveInRoom = false

                        if (previouslyDisconnected) {
                            previouslyDisconnected = false

                            val games = migrateGame.getAll()
                            val players = migratePlayer.getAll()

                            for (game in games) gameFirebaseDao.update(game)
                            for (player in players) playerFirebaseDao.update(player)

                            migrateGame.deleteAll()
                            migratePlayer.deleteAll()
                        }
                    }

                    InternetConnectionStatus.DISCONNECTED -> {
                        previouslyDisconnected = true
                        saveInRoom = true
                    }
                }
            }
        }
    }

    // overridden player functions

    override suspend fun create(id: String, name: String, email: String): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        try {
            if (saveInRoom) {
                val result = playerRoomDao.create(Player(id, name, email))
                emit(DataRequestState.Success(result >= 0))
            }
            else {
                val success = playerFirebaseDao.create(id, name, email) != null
                emit(DataRequestState.Success(success))
            }
        }
        catch (e: FirebaseException) {
            Log.e(LOG_TAG, e.message, e)
            communicationAdapter.message.value?.add(context.getString(R.string.error_create_account))
        }
    }

    override suspend fun update(player: Player): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        try {
            if (saveInRoom) {
                val result = playerRoomDao.update(player)
                emit(DataRequestState.Success(result >= 0))
            }
            else {
                val successful = playerFirebaseDao.update(player)
                emit(DataRequestState.Success(successful))
            }
        }
        catch (e: FirebaseException) {
            Log.e(LOG_TAG, e.message, e)
            communicationAdapter.message.value?.add(context.getString(R.string.error_update_player))
        }
    }

    override suspend fun updateName(id: String, name: String): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        try {
            if (saveInRoom) {
                (playerRoomDao as PlayerDao).updateName(Firebase.auth.currentUser?.uid!!, name)
            }
            else {
                playerFirebaseDao.updateName(id, name)
            }
        }
        catch (e: FirebaseException) {
            Log.e(LOG_TAG, e.message, e)
            communicationAdapter.message.value?.add(context.getString(R.string.error_update_name))
        }
    }

    override suspend fun getPlayer(id: String): Flow<DataRequestState> = flow {
        emit(DataRequestState.InProgress)

        try {
            if (saveInRoom) {
                val player = playerRoomDao.read()
                emit(DataRequestState.Success(player))
            }
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
            if (saveInRoom) {
                playerRoomDao.observe(id).asFlow().stateIn(CoroutineScope(Dispatchers.IO))
            }
            else {
                playerFirebaseDao.observePlayer(id)
            }
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
            if (saveInRoom) {
                val game = gameRoomDao.create(Game(id))
                emit(DataRequestState.Success(game))
            }
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
            if (saveInRoom) {
                val result = gameRoomDao.update(game)
                emit(DataRequestState.Success(result))
            }
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
            if (saveInRoom) {
                val game = (gameRoomDao as GameDao).getMostRecentGame()
                emit(DataRequestState.Success(game))
            }
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
            if (saveInRoom) {
                val games = (gameRoomDao as GameDao).getTopTenGames()
                emit(DataRequestState.Success(games))
            }
            val games = gameFirebaseDao.getTopTenGames()
            emit(DataRequestState.Success(games))
        }
        catch (e: FirebaseException) {
            Log.e(LOG_TAG, e.message, e)
            communicationAdapter.message.value?.add(context.getString(R.string.error_high_score))
        }
    }

    override suspend fun observeGame(id: String): StateFlow<Game?> {
        return try {
            if (saveInRoom) {
                gameRoomDao.observe(id).asFlow().stateIn(CoroutineScope(Dispatchers.IO))
            }
            else {
                gameFirebaseDao.observeGame(id)
            }
        }
        catch (e: FirebaseException) {
            Log.e(LOG_TAG, e.message, e)

            communicationAdapter.message.value?.add(context.getString(R.string.error_get_player))
            MutableStateFlow(null)
        }
    }
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