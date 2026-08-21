package io.bashpsk.zerodownload.core.datastore.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.datastore by preferencesDataStore(name = "ZERO-DOWNLOAD-PSK")
