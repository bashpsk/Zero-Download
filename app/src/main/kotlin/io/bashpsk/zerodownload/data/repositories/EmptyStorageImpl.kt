package io.bashpsk.zerodownload.data.repositories

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.bashpsk.emptylibs.storage.storage.DirectoryData
import io.bashpsk.emptylibs.storage.storage.DirectoryFileData
import io.bashpsk.emptylibs.storage.storage.FileData
import io.bashpsk.emptylibs.storage.storage.FileVisibleType
import io.bashpsk.emptylibs.storage.storage.MakeFileResult
import io.bashpsk.emptylibs.storage.storage.StorageExt
import io.bashpsk.emptylibs.storage.storage.StorageVolumeData
import io.bashpsk.zerodownload.domain.repositories.EmptyMedia
import io.bashpsk.zerodownload.domain.repositories.EmptyStorage
import io.bashpsk.zerodownload.domain.settings.FileSort
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EmptyStorageImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val emptyMedia: EmptyMedia
) : EmptyStorage {

    override fun getStorageVolumes(): Flow<ImmutableList<StorageVolumeData>> {

        return StorageExt.getStorageVolumeFlow(context = context)
    }

    override fun getDirectoryDataList(path: String): Flow<DirectoryFileData> {

        return StorageExt.getDirectoryFileFlow(context = context, path = path)
    }

    override fun getFolderDataSortList(
        folders: ImmutableList<DirectoryData>,
        fileSort: FileSort.Type
    ): Flow<ImmutableList<DirectoryData>> {

        return flow {

            val newFolderList = when (fileSort) {

                FileSort.Type.NameAsc -> folders.sortedBy { folder ->

                    folder.title
                }.toImmutableList()

                FileSort.Type.NameDesc -> folders.sortedByDescending { folder ->

                    folder.title
                }.toImmutableList()

                FileSort.Type.DateAsc -> folders.sortedBy { folder ->

                    folder.modifiedDate
                }.toImmutableList()

                FileSort.Type.DateDesc -> folders.sortedByDescending { folder ->

                    folder.modifiedDate
                }.toImmutableList()

                FileSort.Type.SizeAsc -> folders.sortedBy { folder ->

                    folder.folders + folder.files
                }.toImmutableList()

                FileSort.Type.SizeDesc -> folders.sortedByDescending { folder ->

                    folder.folders + folder.files
                }.toImmutableList()
            }

            emit(value = newFolderList)
        }.flowOn(context = Dispatchers.Default)
    }

    override fun getFileDataSortList(
        files: ImmutableList<FileData>,
        fileSort: FileSort.Type
    ): Flow<ImmutableList<FileData>> {

        return flow {

            val newFileList = when (fileSort) {

                FileSort.Type.NameAsc -> files.sortedBy { file ->

                    file.title
                }.toImmutableList()

                FileSort.Type.NameDesc -> files.sortedByDescending { file ->

                    file.title
                }.toImmutableList()

                FileSort.Type.DateAsc -> files.sortedBy { file ->

                    file.modifiedDate
                }.toImmutableList()

                FileSort.Type.DateDesc -> files.sortedByDescending { file ->

                    file.modifiedDate
                }.toImmutableList()

                FileSort.Type.SizeAsc -> files.sortedBy { file ->

                    file.size
                }.toImmutableList()

                FileSort.Type.SizeDesc -> files.sortedByDescending { file ->

                    file.size
                }.toImmutableList()
            }

            emit(value = newFileList)
        }.flowOn(context = Dispatchers.Default)
    }

    override fun getCacheSize(): Flow<Long> {

        return flow {

            val cacheSize = StorageExt.getFileSize(context.cacheDir.path)

            emit(value = cacheSize)
        }.flowOn(context = Dispatchers.IO)
    }

    override fun setMakeFolderFile(
        parentPath: String,
        name: String,
        isFolder: Boolean,
        visibleType: FileVisibleType
    ): Flow<MakeFileResult> {

        return flow {

            val result = StorageExt.makeFolderOrFile(
                parentPath = parentPath,
                name = name,
                isFolder = isFolder,
                visibleType = visibleType
            )

            emit(value = result)
        }.flowOn(context = Dispatchers.IO)
    }
}