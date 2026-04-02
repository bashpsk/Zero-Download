package io.bashpsk.zerodownload.domain.archive

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import io.bashpsk.emptylibs.storage.storage.FileType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Immutable
@Parcelize
@Serializable
data class ArchiveContentData(
    val uuid: String  = Uuid.random().toString(),
    val title: String = "",
    val path: String = "",
    val isDirectory: Boolean = false,
    val size: Long = 0L,
    val entryCount: Int = 0,
    val actualSize: Long = 0L,
    val fileType: FileType = FileType.UNKNOWN,
    val modifiedDate: Long = 0L
) : Parcelable