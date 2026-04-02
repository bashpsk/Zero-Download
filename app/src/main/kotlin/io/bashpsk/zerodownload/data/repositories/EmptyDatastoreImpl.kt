package io.bashpsk.zerodownload.data.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import io.bashpsk.zerodownload.domain.datastore.datastore
import io.bashpsk.zerodownload.domain.repositories.EmptyDatastore
import io.bashpsk.zerodownload.domain.utils.LOG_TAG
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class EmptyDatastoreImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : EmptyDatastore {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

        Log.e(LOG_TAG, throwable.message, throwable)
    }

    override fun <T> getPreference(key: Preferences.Key<T>, initial: T): Flow<T> {

        return context.datastore.data.mapLatest { preferences ->

            preferences[key] ?: initial
        }.flowOn(context = Dispatchers.IO)
    }

    override fun <T> getPreferenceOrNull(key: Preferences.Key<T>, initial: T): T? {

        return runBlocking { getPreference(key = key, initial = initial).firstOrNull() }
    }

    override suspend fun <T> setPreference(key: Preferences.Key<T>, value: T) {

        withContext(context = Dispatchers.IO + exceptionHandler) {

            context.datastore.updateData { preferences ->

                preferences.toMutablePreferences().apply { this[key] = value }
            }
        }
    }

    override suspend fun <T> removePreference(key: Preferences.Key<T>) {

        withContext(context = Dispatchers.IO + exceptionHandler) {

            context.datastore.edit { preferences -> preferences.remove(key = key) }
        }
    }

    override suspend fun clearPreference() {

        withContext(context = Dispatchers.IO + exceptionHandler) {

            context.datastore.edit { preferences -> preferences.clear() }
        }
    }
}