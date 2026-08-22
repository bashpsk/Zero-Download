package io.bashpsk.zerodownload.core.model.extract

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.core.model.R

enum class ExtractorType(@StringRes val id: Int) {

    Youtube(id = R.string.extractor_youtube),
    Unknown(id = R.string.extractor_unknown);

    companion object {

        fun find(name: String): ExtractorType {

            return try {

                valueOf(value = name)
            } catch (exception: Exception) {

                Unknown
            }
        }

        val ExtractorType.label: String
        @Stable
        @ReadOnlyComposable
        @Composable
        get() = stringResource(id)
    }
}