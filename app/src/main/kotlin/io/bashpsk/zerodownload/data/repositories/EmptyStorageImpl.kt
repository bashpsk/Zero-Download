package io.bashpsk.zerodownload.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import io.bashpsk.zerodownload.domain.repositories.EmptyMedia
import io.bashpsk.zerodownload.domain.repositories.EmptyStorage
import io.bashpsk.zerodownload.domain.settings.FileSort
import io.bashpsk.zerodownload.domain.utils.LOG_TAG
import io.bashpsk.emptylibs.storage.storage.DirectoryData
import io.bashpsk.emptylibs.storage.storage.DirectoryFileData
import io.bashpsk.emptylibs.storage.storage.FileData
import io.bashpsk.emptylibs.storage.storage.FileType
import io.bashpsk.emptylibs.storage.storage.FileVisibleType
import io.bashpsk.emptylibs.storage.storage.MakeFileResult
import io.bashpsk.emptylibs.storage.storage.StorageExt
import io.bashpsk.emptylibs.storage.storage.StorageVolumeData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import java.io.File
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@SuppressLint("Range")
class EmptyStorageImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val emptyMedia: EmptyMedia
) : EmptyStorage {

    private val emptyScope = CoroutineScope(context = SupervisorJob() + Dispatchers.IO)

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->

        Log.e(LOG_TAG, throwable.message, throwable)
    }

    override fun getStorageVolumes(): Flow<ImmutableList<StorageVolumeData>> {

        return flow {

            val storageVolumeListFlow = StorageExt.getStorageVolumeFlow(context = context)

            emitAll(flow = storageVolumeListFlow)
        }.flowOn(context = Dispatchers.IO)
    }

    override fun getDirectoryDataList(path: String): Flow<DirectoryFileData> {

        return flow {

            val directoryFileList = StorageExt.getDirectoryFileData(context = context, path = path)

            emit(value = directoryFileList)
        }.flowOn(context = Dispatchers.IO)
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

            val cacheSize = context.cacheDir.walkTopDown().sumOf { file: File -> file.length() }

            emit(value = cacheSize)
        }.flowOn(context = Dispatchers.IO)
    }

    override fun getPathDirectoryData(path: String): Flow<DirectoryData> {

        return flow {

            try {

                val sourceFile = File(path)

                val storageList = getStorageVolumes().toList().flatten().distinctBy { storage ->

                    storage.path
                }.toImmutableList()

                val storageData = storageList.firstOrNull { storage ->

                    storage.path == sourceFile.path
                }

                emit(
                    value = DirectoryData(
                        title = storageData?.volumeType?.label ?: sourceFile.name,
                        path = sourceFile.path,
                        uri = sourceFile.toUri().toString(),
                        visibleType = FileVisibleType.getFileVisibleType(file = sourceFile),
                        folders = 0,
                        files = 0,
                        modifiedDate = sourceFile.lastModified()
                    )
                )
            } catch (exception: Exception) {

                Log.e(LOG_TAG, exception.message, exception)
                emit(value = DirectoryData())
            }
        }.flowOn(context = Dispatchers.IO)
    }

    override fun getParentDirectory(path: String): Flow<DirectoryData> {

        return flow {

            try {

                File(path).parentFile?.let { sourceFile ->

                    val newDirectoryData = getPathDirectoryData(path = sourceFile.path)

                    emitAll(flow = newDirectoryData)
                } ?: emit(value = DirectoryData())
            } catch (exception: Exception) {

                Log.e(LOG_TAG, exception.message, exception)
                emit(value = DirectoryData())
            }
        }.flowOn(context = Dispatchers.IO)
    }

    override fun getDirectoryDetailList(
        paths: ImmutableList<String>
    ): Flow<ImmutableList<DirectoryData>> {

        return flow {

            try {

                val storageList = getStorageVolumes().toList().flatten().distinctBy { storage ->

                    storage.path
                }.toImmutableList()

                val detailList = paths.map { path ->

                    val sourceFile = File(path)

                    val storageData = storageList.firstOrNull { storage ->

                        storage.path == sourceFile.path
                    }

                    val folders = sourceFile.listFiles()?.count { folder ->

                        folder.isDirectory
                    } ?: 0

                    val files = sourceFile.listFiles()?.count { file -> file.isFile } ?: 0

                    DirectoryData(
                        title = storageData?.volumeType?.label ?: sourceFile.name,
                        path = sourceFile.path,
                        uri = sourceFile.toUri().toString(),
                        visibleType = FileVisibleType.getFileVisibleType(file = sourceFile),
                        folders = folders,
                        files = files,
                        modifiedDate = sourceFile.lastModified()
                    )
                }.toImmutableList()

                emit(value = detailList)
            } catch (exception: Exception) {

                Log.e(LOG_TAG, exception.message, exception)
                emit(value = persistentListOf())
            }
        }.flowOn(context = Dispatchers.IO)
    }

    override fun getFileDetailList(paths: ImmutableList<String>): Flow<ImmutableList<FileData>> {

        return flow {

            try {

                val detailList = paths.map { path ->

                    val sourceFile = File(path)

                    FileData(
                        title = sourceFile.name,
                        path = sourceFile.path,
                        uri = sourceFile.toUri().toString(),
                        extension = sourceFile.extension,
                        visibleType = FileVisibleType.getFileVisibleType(file = sourceFile),
                        fileType = FileType.getFileType(extension = sourceFile.extension),
                        size = sourceFile.length(),
                        modifiedDate = sourceFile.lastModified()
                    )
                }.toImmutableList()

                emit(value = detailList)
            } catch (exception: Exception) {

                Log.e(LOG_TAG, exception.message, exception)
                emit(value = persistentListOf())
            }
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