package io.bashpsk.zerodownload.feature.settings.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bashpsk.zerodownload.core.common.viewmodel.stateInWhileSubscribed
import io.bashpsk.zerodownload.core.domain.repositories.EmptyWorker
import io.bashpsk.zerodownload.core.model.worker.WorkTaskType
import io.bashpsk.zerodownload.feature.settings.event.DownloaderSettingUIEvent
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
        workerId = WorkTaskType.LibraryUpdate.uuid
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