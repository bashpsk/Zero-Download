package io.bashpsk.zerodownload.domain.settings

enum class SortType(val label: String) {

    ASC(label = "Asc"),
    DESC(label = "Desc");

    companion object {

        fun findFromValue(value: String): SortType {

            return try {

                valueOf(value = value)
            } catch (exception: IllegalArgumentException) {

                ASC
            }
        }
    }
}