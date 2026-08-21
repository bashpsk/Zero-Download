package io.bashpsk.zerodownload.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.bashpsk.zerodownload.core.domain.repositories.EmptyAbout
import io.bashpsk.zerodownload.core.data.repositories.EmptyAboutImpl
import io.bashpsk.zerodownload.core.domain.repositories.EmptyDatastore
import io.bashpsk.zerodownload.core.data.repositories.EmptyDatastoreImpl
import io.bashpsk.zerodownload.core.domain.repositories.EmptyMedia
import io.bashpsk.zerodownload.core.data.repositories.EmptyMediaImpl
import io.bashpsk.zerodownload.core.domain.repositories.EmptyNotification
import io.bashpsk.zerodownload.core.data.repositories.EmptyNotificationImpl
import io.bashpsk.zerodownload.core.domain.repositories.EmptyPlayer
import io.bashpsk.zerodownload.core.data.repositories.EmptyPlayerImpl
import io.bashpsk.zerodownload.core.domain.repositories.EmptyStorage
import io.bashpsk.zerodownload.core.data.repositories.EmptyStorageImpl
import io.bashpsk.zerodownload.core.domain.repositories.EmptyWorker
import io.bashpsk.zerodownload.core.data.repositories.EmptyWorkerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEmptyMedia(emptyMedia: EmptyMediaImpl): EmptyMedia

    @Binds
    @Singleton
    abstract fun bindEmptyStorage(emptyStorage: EmptyStorageImpl): EmptyStorage

    @Binds
    @Singleton
    abstract fun bindEmptyWorker(emptyWorker: EmptyWorkerImpl): EmptyWorker

    @Binds
    @Singleton
    abstract fun bindEmptyDatastore(emptyDatastore: EmptyDatastoreImpl): EmptyDatastore

    @Binds
    @Singleton
    abstract fun bindEmptyNotification(emptyNotification: EmptyNotificationImpl): EmptyNotification

    @Binds
    @Singleton
    abstract fun bindEmptyPlayer(emptyPlayer: EmptyPlayerImpl): EmptyPlayer

    @Binds
    @Singleton
    abstract fun bindEmptyAbout(emptyAbout: EmptyAboutImpl): EmptyAbout
}