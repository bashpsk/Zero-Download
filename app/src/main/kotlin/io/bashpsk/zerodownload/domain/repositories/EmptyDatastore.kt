package io.bashpsk.zerodownload.domain.repositories

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface EmptyDatastore {

    fun <T> getPreference(key: Preferences.Key<T>, initial: T): Flow<T>

    fun <T> getPreferenceOrNull(key: Preferences.Key<T>, initial: T): T?

    suspend fun <T> setPreference(key: Preferences.Key<T>, value: T)

    suspend fun <T> removePreference(key: Preferences.Key<T>)

    suspend fun clearPreference()
}