package io.bashpsk.zerodownload.data.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.qualifiers.ApplicationContext
import io.bashpsk.emptylibs.datastoreui.extension.clearAllPreference
import io.bashpsk.emptylibs.datastoreui.extension.getPreference
import io.bashpsk.emptylibs.datastoreui.extension.resetPreference
import io.bashpsk.emptylibs.datastoreui.extension.setPreference
import io.bashpsk.zerodownload.domain.datastore.datastore
import io.bashpsk.zerodownload.domain.repositories.EmptyDatastore
import io.bashpsk.zerodownload.domain.utils.LOG_TAG
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmptyDatastoreImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : EmptyDatastore {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

        Log.e(LOG_TAG, throwable.message, throwable)
    }

    override fun <T> getPreference(key: Preferences.Key<T>, initial: T): Flow<T> {

        return context.datastore.getPreference(key = key, initial = initial)
    }

    override fun <T> getPreferenceOrNull(key: Preferences.Key<T>, initial: T): T? {

        return runBlocking(context = Dispatchers.IO) {

            getPreference(key = key, initial = initial).firstOrNull()
        }
    }

    override suspend fun <T> setPreference(key: Preferences.Key<T>, value: T) {

        withContext(context = Dispatchers.IO + exceptionHandler) {

            context.datastore.setPreference(key = key, value = value)
        }
    }

    override suspend fun <T> resetPreference(key: Preferences.Key<T>) {

        withContext(context = Dispatchers.IO + exceptionHandler) {

            context.datastore.resetPreference(key = key)
        }
    }

    override suspend fun clearAllPreference() {

        withContext(context = Dispatchers.IO + exceptionHandler) {

            context.datastore.clearAllPreference()
        }
    }
}