package com.huntergaming.gamedata.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.huntergaming.gamedata.PlayerRepo
import com.huntergaming.gamedata.model.Player
import com.huntergaming.ui.uitl.DataRequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerRepo: PlayerRepo
): ViewModel() {

    // functions

    suspend fun observePlayer(): LiveData<Player?> = liveData {
        playerRepo.observePlayer(Firebase.auth.currentUser?.uid!!)
    }

    suspend fun updatePlayerName(name: String): LiveData<DataRequestState> = liveData {
        playerRepo.updateName(Firebase.auth.currentUser?.uid!!, name)
    }
}