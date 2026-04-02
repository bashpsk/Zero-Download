package io.bashpsk.zerodownload.domain.repositories

import io.bashpsk.zerodownload.domain.settings.FileSort
import io.bashpsk.emptylibs.storage.storage.DirectoryData
import io.bashpsk.emptylibs.storage.storage.DirectoryFileData
import io.bashpsk.emptylibs.storage.storage.FileData
import io.bashpsk.emptylibs.storage.storage.FileVisibleType
import io.bashpsk.emptylibs.storage.storage.MakeFileResult
import io.bashpsk.emptylibs.storage.storage.StorageVolumeData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface EmptyStorage {

    fun getStorageVolumes(): Flow<ImmutableList<StorageVolumeData>>

    fun getDirectoryDataList(path: String): Flow<DirectoryFileData>

    fun getFolderDataSortList(
        folders: ImmutableList<DirectoryData>,
        fileSort: FileSort.Type
    ): Flow<ImmutableList<DirectoryData>>

    fun getFileDataSortList(
        files: ImmutableList<FileData>,
        fileSort: FileSort.Type
    ): Flow<ImmutableList<FileData>>

    fun getCacheSize(): Flow<Long>

    fun getPathDirectoryData(path: String): Flow<DirectoryData>

    fun getParentDirectory(path: String): Flow<DirectoryData>

    fun getDirectoryDetailList(
        paths: ImmutableList<String>
    ): Flow<ImmutableList<DirectoryData>>

    fun getFileDetailList(paths: ImmutableList<String>): Flow<ImmutableList<FileData>>

    fun setMakeFolderFile(
        parentPath: String,
        name: String,
        isFolder: Boolean,
        visibleType: FileVisibleType
    ): Flow<MakeFileResult>
}