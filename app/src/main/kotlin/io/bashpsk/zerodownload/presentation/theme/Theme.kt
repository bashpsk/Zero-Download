package io.bashpsk.zerodownload.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle

@Composable
fun EDMTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val context = LocalContext.current

    val colorScheme = when {

        isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> when (isDarkTheme) {

            true -> dynamicDarkColorScheme(context = context)
            false -> dynamicLightColorScheme(context = context)
        }

        isDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val Typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = TypographyTokens.BodyLargeFont,
            fontWeight = TypographyTokens.BodyLargeWeight,
            fontSize = TypographyTokens.BodyLargeSize,
            lineHeight = TypographyTokens.BodyLargeLineHeight,
            letterSpacing = TypographyTokens.BodyLargeTracking
        ),
        bodyMedium = TextStyle(
            fontFamily = TypographyTokens.BodyMediumFont,
            fontWeight = TypographyTokens.BodyMediumWeight,
            fontSize = TypographyTokens.BodyMediumSize,
            lineHeight = TypographyTokens.BodyMediumLineHeight,
            letterSpacing = TypographyTokens.BodyMediumTracking
        ),
        bodySmall = TextStyle(
            fontFamily = TypographyTokens.BodySmallFont,
            fontWeight = TypographyTokens.BodySmallWeight,
            fontSize = TypographyTokens.BodySmallSize,
            lineHeight = TypographyTokens.BodySmallLineHeight,
            letterSpacing = TypographyTokens.BodySmallTracking
        ),
        displayLarge = TextStyle(
            fontFamily = TypographyTokens.DisplayLargeFont,
            fontWeight = TypographyTokens.DisplayLargeWeight,
            fontSize = TypographyTokens.DisplayLargeSize,
            lineHeight = TypographyTokens.DisplayLargeLineHeight,
            letterSpacing = TypographyTokens.DisplayLargeTracking
        ),
        displayMedium = TextStyle(
            fontFamily = TypographyTokens.DisplayMediumFont,
            fontWeight = TypographyTokens.DisplayMediumWeight,
            fontSize = TypographyTokens.DisplayMediumSize,
            lineHeight = TypographyTokens.DisplayMediumLineHeight,
            letterSpacing = TypographyTokens.DisplayMediumTracking
        ),
        displaySmall = TextStyle(
            fontFamily = TypographyTokens.DisplaySmallFont,
            fontWeight = TypographyTokens.DisplaySmallWeight,
            fontSize = TypographyTokens.DisplaySmallSize,
            lineHeight = TypographyTokens.DisplaySmallLineHeight,
            letterSpacing = TypographyTokens.DisplaySmallTracking
        ),
        headlineLarge = TextStyle(
            fontFamily = TypographyTokens.HeadlineLargeFont,
            fontWeight = TypographyTokens.HeadlineLargeWeight,
            fontSize = TypographyTokens.HeadlineLargeSize,
            lineHeight = TypographyTokens.HeadlineLargeLineHeight,
            letterSpacing = TypographyTokens.HeadlineLargeTracking
        ),
        headlineMedium = TextStyle(
            fontFamily = TypographyTokens.HeadlineMediumFont,
            fontWeight = TypographyTokens.HeadlineMediumWeight,
            fontSize = TypographyTokens.HeadlineMediumSize,
            lineHeight = TypographyTokens.HeadlineMediumLineHeight,
            letterSpacing = TypographyTokens.HeadlineMediumTracking
        ),
        headlineSmall = TextStyle(
            fontFamily = TypographyTokens.HeadlineSmallFont,
            fontWeight = TypographyTokens.HeadlineSmallWeight,
            fontSize = TypographyTokens.HeadlineSmallSize,
            lineHeight = TypographyTokens.HeadlineSmallLineHeight,
            letterSpacing = TypographyTokens.HeadlineSmallTracking
        ),
        labelLarge = TextStyle(
            fontFamily = TypographyTokens.LabelLargeFont,
            fontWeight = TypographyTokens.LabelLargeWeight,
            fontSize = TypographyTokens.LabelLargeSize,
            lineHeight = TypographyTokens.LabelLargeLineHeight,
            letterSpacing = TypographyTokens.LabelLargeTracking
        ),
        labelMedium = TextStyle(
            fontFamily = TypographyTokens.LabelMediumFont,
            fontWeight = TypographyTokens.LabelMediumWeight,
            fontSize = TypographyTokens.LabelMediumSize,
            lineHeight = TypographyTokens.LabelMediumLineHeight,
            letterSpacing = TypographyTokens.LabelMediumTracking
        ),
        labelSmall = TextStyle(
            fontFamily = TypographyTokens.LabelSmallFont,
            fontWeight = TypographyTokens.LabelSmallWeight,
            fontSize = TypographyTokens.LabelSmallSize,
            lineHeight = TypographyTokens.LabelSmallLineHeight,
            letterSpacing = TypographyTokens.LabelSmallTracking
        ),
        titleLarge = TextStyle(
            fontFamily = TypographyTokens.TitleLargeFont,
            fontWeight = TypographyTokens.TitleLargeWeight,
            fontSize = TypographyTokens.TitleLargeSize,
            lineHeight = TypographyTokens.TitleLargeLineHeight,
            letterSpacing = TypographyTokens.TitleLargeTracking
        ),
        titleMedium = TextStyle(
            fontFamily = TypographyTokens.TitleMediumFont,
            fontWeight = TypographyTokens.TitleMediumWeight,
            fontSize = TypographyTokens.TitleMediumSize,
            lineHeight = TypographyTokens.TitleMediumLineHeight,
            letterSpacing = TypographyTokens.TitleMediumTracking
        ),
        titleSmall = TextStyle(
            fontFamily = TypographyTokens.TitleSmallFont,
            fontWeight = TypographyTokens.TitleSmallWeight,
            fontSize = TypographyTokens.TitleSmallSize,
            lineHeight = TypographyTokens.TitleSmallLineHeight,
            letterSpacing = TypographyTokens.TitleSmallTracking
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}