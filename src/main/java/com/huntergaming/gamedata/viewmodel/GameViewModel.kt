package com.huntergaming.gamedata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.huntergaming.gamedata.GameRepo
import com.huntergaming.ui.uitl.DataRequestState
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val gameRepo: GameRepo
): ViewModel() {

    fun getHighScoreGames() = liveData<DataRequestState> {
        gameRepo.getTopTenGames()
    }
}