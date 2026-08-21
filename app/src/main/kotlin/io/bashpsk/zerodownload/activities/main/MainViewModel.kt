package io.bashpsk.zerodownload.activities.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bashpsk.zerodownload.activities.MainKeepSplashScreen
import io.bashpsk.zerodownload.core.navigation.extension.findNavScreen
import io.bashpsk.zerodownload.core.navigation.screen.NavScreen
import io.bashpsk.zerodownload.feature.navigation.event.MainUIEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val isKeepSplashScreen = savedStateHandle.getStateFlow(
        key = MainKeepSplashScreen,
        initialValue = false
    )

    private val _navScreenChannel = Channel<NavScreen?>()
    val navScreenChannel = _navScreenChannel.receiveAsFlow()

    fun onUIEvent(uiEvent: MainUIEvent) = viewModelScope.launch(context = Dispatchers.Default) {

        when (uiEvent) {

            is MainUIEvent.DoNothing -> {}

            is MainUIEvent.SetNavChannel -> {

                _navScreenChannel.send(findNavScreen(uri = uiEvent.uri, type = uiEvent.type))
                savedStateHandle[MainKeepSplashScreen] = false
            }
        }
    }
}