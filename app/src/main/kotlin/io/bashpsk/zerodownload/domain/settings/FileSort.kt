package io.bashpsk.zerodownload.domain.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FileOpen
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.ui.graphics.vector.ImageVector

enum class FileSort(
    val label: String,
    val selectedIcon: ImageVector,
    val deselectedIcon: ImageVector
) {

    NAME(
        label = "Name",
        selectedIcon = Icons.Filled.TextFields,
        deselectedIcon = Icons.Outlined.TextFields
    ),
    DATE(
        label = "Date",
        selectedIcon = Icons.Filled.CalendarMonth,
        deselectedIcon = Icons.Outlined.CalendarMonth
    ),
    SIZE(
        label = "Size",
        selectedIcon = Icons.Filled.FileOpen,
        deselectedIcon = Icons.Outlined.FileOpen
    );

    enum class Type {

        NameAsc,
        NameDesc,
        DateAsc,
        DateDesc,
        SizeAsc,
        SizeDesc;

        companion object {

            fun findFromValue(value: String): Type {

                return try {

                    valueOf(value = value)
                } catch (exception: IllegalArgumentException) {

                    NameAsc
                }
            }

            fun parseSortOrderType(value: String): Pair<FileSort, SortType> {

                return when (findFromValue(value = value)) {
                    NameAsc -> Pair(first = NAME, second = SortType.ASC)
                    NameDesc -> Pair(first = NAME, second = SortType.DESC)
                    DateAsc -> Pair(first = DATE, second = SortType.ASC)
                    DateDesc -> Pair(first = DATE, second = SortType.DESC)
                    SizeAsc -> Pair(first = SIZE, second = SortType.ASC)
                    SizeDesc -> Pair(first = SIZE, second = SortType.DESC)
                }
            }

            fun toSortOrderType(sort: FileSort, type: SortType): Type {

                return when (sort) {

                    NAME -> when (type) {

                        SortType.ASC -> NameAsc
                        SortType.DESC -> NameDesc
                    }

                    DATE -> when (type) {

                        SortType.ASC -> DateAsc
                        SortType.DESC -> DateDesc
                    }

                    SIZE -> when (type) {

                        SortType.ASC -> SizeAsc
                        SortType.DESC -> SizeDesc
                    }
                }
            }
        }
    }
}