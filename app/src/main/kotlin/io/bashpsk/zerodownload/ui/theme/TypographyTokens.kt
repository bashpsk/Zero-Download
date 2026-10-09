package io.bashpsk.zerodownload.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import io.bashpsk.zerodownload.core.ui.R

internal object TypeScaleTokens {

    val Display = FontFamily(Font(resId = R.font.righteous_regular))
    val Headline = FontFamily(Font(resId = R.font.funnel_sans_semi_bold, weight = FontWeight.SemiBold))
    val Title = FontFamily(Font(resId = R.font.museo_moderno_semi_bold, weight = FontWeight.SemiBold))
    val Label = FontFamily.SansSerif
    val Body = FontFamily(Font(resId = R.font.roboto_regular, weight = FontWeight.Normal))
}