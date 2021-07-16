package com.huntergaming.gamedata.repository

import com.huntergaming.gamedata.RequestState
import com.huntergaming.gamedata.data.Dao
import com.huntergaming.gamedata.model.Player
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SolitaireRepository @Inject constructor(
    private val playerDao: Dao<Player>
) {

   suspend fun create(player: Player): Flow<RequestState> {
        return playerDao.create(player)
    }

    /**
     * Get all games from the database.
     * @return A flow of games in the database.
     */
//    suspend fun getAllGames(): Flow<List<Games>> =
//        if (allGamesCache.contains(allGamesKey)) {
//            flowOf(allGamesCache.get(allGamesKey))
//        }
//        else {
//            val allGames = gamesDao.getAllGames()
//            allGamesCache.add(allGamesKey, allGames.value)
//            allGames
//        }

    /**
     * Get the top ten games, by score, from the database.
     * @return A flow of the top ten games in the database.
     */
//    suspend fun getTopTenGames(): Flow<List<Games>> =
//        if (topTenGamesCache.contains(topTenKey)) {
//            flowOf(topTenGamesCache.get(topTenKey))
//        }
//        else {
//            val topTenGames = gamesDao.getAllGames()
//            topTenGamesCache.add(topTenKey, topTenGames.value)
//            topTenGames
//        }

    /**
     * Get the player from the database.
     * @return A flow of the player.
     */
//    suspend fun getPlayer(): Flow<Player> {
//        if (playerCache.contains(playerKey)) {
//            return flowOf(playerCache.get(playerKey))
//        }
//        return playerDao.getPlayer()
//    }

    /**
     * Insert a new player object into the database. Will overwrite an existing player if there already is one.
     * @param player The player.
     */
//    suspend fun insert(player: Player) = playerDao.insert(player)

    /**
     * Updates an existing player.
     * @param player The player.
     */
//    suspend fun update(player: Player) = playerDao.update(player)

    /**
     * Insert a new Games object into the database. Will overwrite an existing player if there already is one.
     * @param games The games.
     */
//    suspend fun insert(games: Games) = gamesDao.insert(games)
}