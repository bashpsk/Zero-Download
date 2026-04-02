package io.bashpsk.zerodownload.presentation.activities.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bashpsk.zerodownload.domain.events.MainUIEvent
import io.bashpsk.zerodownload.domain.navigation.NavScreen
import io.bashpsk.zerodownload.domain.navigation.findNavScreen
import io.bashpsk.zerodownload.domain.resources.ConstantKey
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
        key = ConstantKey.MAIN_KEEP_SPLASH_SCREEN,
        initialValue = false
    )

    private val _navScreenChannel = Channel<NavScreen?>()
    val navScreenChannel = _navScreenChannel.receiveAsFlow()

    fun onUIEvent(uiEvent: MainUIEvent) = viewModelScope.launch(context = Dispatchers.Default) {

        when (uiEvent) {

            is MainUIEvent.DoNothing -> {}

            is MainUIEvent.SetNavChannel -> {

                _navScreenChannel.send(findNavScreen(uri = uiEvent.uri, type = uiEvent.type))
                savedStateHandle[ConstantKey.MAIN_KEEP_SPLASH_SCREEN] = false
            }
        }
    }
}