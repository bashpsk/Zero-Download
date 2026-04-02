package io.bashpsk.zerodownload.domain.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.datastore by preferencesDataStore(name = "EDM-PSK")