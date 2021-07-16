package com.huntergaming.gamedata

sealed class RequestState {
    object NoInternet: RequestState()
    object InProgress: RequestState()
    object Success : RequestState()
    class Error(val message: String?) : RequestState()
}