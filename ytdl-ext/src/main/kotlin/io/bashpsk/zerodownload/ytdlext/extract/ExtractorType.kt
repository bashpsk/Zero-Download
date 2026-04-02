package io.bashpsk.zerodownload.ytdlext.extract

enum class ExtractorType(val label: String = "") {

    Youtube(label = "Youtube"),
    Unknown(label = "Unknown");

    companion object {

        fun find(name: String): ExtractorType {

            return try {

                valueOf(value = name)
            } catch (exception: Exception) {

                entries.firstOrNull { type -> type.label == name } ?: Unknown
            }
        }
    }
}