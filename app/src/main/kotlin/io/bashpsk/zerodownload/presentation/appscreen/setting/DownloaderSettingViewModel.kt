package io.bashpsk.zerodownload.presentation.appscreen.setting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bashpsk.zerodownload.data.worker.YtDlUpdateWorker
import io.bashpsk.zerodownload.domain.events.DownloaderSettingUIEvent
import io.bashpsk.zerodownload.domain.repositories.EmptyWorker
import io.bashpsk.zerodownload.utilities.viewmodel.stateInWhileSubscribed
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DownloaderSettingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val emptyWorker: EmptyWorker
) : ViewModel() {

    val runningUpdateWorkList = emptyWorker.getWorkInfoList(
        workerId = YtDlUpdateWorker.WORKER_ID
    ).flatMapLatest { workInfos ->

        val newInfoList = workInfos.filter { workInfo ->

            !workInfo.state.isFinished
        }.toImmutableList()

        flowOf(value = newInfoList)
    }.flowOn(context = Dispatchers.Default).stateInWhileSubscribed(initial = null)

    val isYtDlUpdating = runningUpdateWorkList.flatMapLatest { workInfos ->

        flowOf(value = workInfos?.isNotEmpty())
    }.flowOn(context = Dispatchers.Default).stateInWhileSubscribed(initial = null)

    fun onUIEvent(uiEvent: DownloaderSettingUIEvent) = viewModelScope.launch(Dispatchers.Default) {

        when (uiEvent) {

            is DownloaderSettingUIEvent.DoNothing -> {}

            is DownloaderSettingUIEvent.UpdateYtDl -> {

                emptyWorker.setYtDlUpdate().collectLatest { requestResult ->

                }
            }
        }
    }
}