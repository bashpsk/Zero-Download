package io.bashpsk.zerodownload.core.model.settings

enum class AppLanguage(val code: String, val language: String) {

    System(code = "", language = "System"),
    Tamil(code = "ta", language = "தமிழ்"),
    English(code = "en", language = "English"),
    EnglishIndia(code = "en-IN", language = "English (India)"),
    EnglishUK(code = "en-GB", language = "English (UK)"),
    EnglishUS(code = "en-US", language = "English (US)");

    companion object {

        fun find(name: String): AppLanguage {

            return try {

                valueOf(value = name)
            } catch (exception: Exception) {

                System
            }
        }
    }
}