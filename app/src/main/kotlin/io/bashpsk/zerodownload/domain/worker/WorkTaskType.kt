package io.bashpsk.zerodownload.domain.worker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DriveFileMove
import androidx.compose.material.icons.automirrored.outlined.DriveFileMove
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FileCopy
import androidx.compose.material.icons.outlined.SystemUpdate
import androidx.compose.ui.graphics.vector.ImageVector

enum class WorkTaskType(
    val uuid: String = "",
    val id: Int,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {

    LIBRARY_UPDATE(
        uuid = "LIBRARY-UPDATE-WORKER",
        id = 0,
        label = "Library Update",
        selectedIcon = Icons.Filled.SystemUpdate,
        unselectedIcon = Icons.Outlined.SystemUpdate
    ),
    YtDlCommand(
        uuid = "YT-DL-COMMAND-WORKER",
        id = 1,
        label = "Yt-Dl Command",
        selectedIcon = Icons.Filled.Download,
        unselectedIcon = Icons.Outlined.Download
    ),
    FileCopy(
        uuid = "FILE-COPY-WORKER",
        id = 2,
        label = "File Copy",
        selectedIcon = Icons.Filled.FileCopy,
        unselectedIcon = Icons.Outlined.FileCopy
    ),
    FileMove(
        uuid = "FILE-MOVE-WORKER",
        id = 3,
        label = "File Move",
        selectedIcon = Icons.AutoMirrored.Filled.DriveFileMove,
        unselectedIcon = Icons.AutoMirrored.Outlined.DriveFileMove
    ),
    FileDelete(
        uuid = "FILE-DELETE-WORKER",
        id = 4,
        label = "File Delete",
        selectedIcon = Icons.Filled.DeleteForever,
        unselectedIcon = Icons.Outlined.DeleteForever
    ),
    FileRename(
        uuid = "FILE-RENAME-WORKER",
        id = 4,
        label = "File Rename",
        selectedIcon = Icons.Filled.Edit,
        unselectedIcon = Icons.Outlined.Edit
    );
}