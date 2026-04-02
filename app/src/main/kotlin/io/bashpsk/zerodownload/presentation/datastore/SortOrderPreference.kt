package io.bashpsk.zerodownload.presentation.datastore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.settings.FileSort
import io.bashpsk.zerodownload.domain.settings.MediaSort
import io.bashpsk.zerodownload.domain.settings.SortType
import io.bashpsk.emptylibs.datastoreui.datastore.LocalDatastore
import io.bashpsk.emptylibs.datastoreui.extension.getPreference
import io.bashpsk.emptylibs.datastoreui.extension.setPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.enums.EnumEntries

@Composable
fun MediaSortOrderPreference(
    dialogVisibleState: MutableTransitionState<Boolean>,
    key: Preferences.Key<String>,
    entities: EnumEntries<MediaSort>,
) {

    val dataStore = LocalDatastore.current
    val coroutineScope = rememberCoroutineScope()

    val initialSortOrder by remember(entities) {
        derivedStateOf {

            MediaSort.Type.toSortOrderType(sort = entities.first(), type = SortType.ASC).name
        }
    }

    val getMediaSortOrder by dataStore.getPreference(
        key = key,
        initial = initialSortOrder
    ).collectAsStateWithLifecycle(initialValue = initialSortOrder)

    val (selectedMediaSort, selectedSortType) = remember(getMediaSortOrder) {
        MediaSort.Type.parseSortOrderType(value = getMediaSortOrder)
    }

    AnimatedVisibility(visibleState = dialogVisibleState) {

        AlertDialog(
            modifier = Modifier.fillMaxWidth(fraction = 0.95F),
            onDismissRequest = {

                dialogVisibleState.targetState = false
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = true
            ),
            shape = MaterialTheme.shapes.small,
            title = {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = stringResource(R.string.sort_order_dialog_title),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    IconButton(
                        onClick = {

                            dialogVisibleState.targetState = false
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.close)
                        )
                    }
                }
            },
            text = {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                ) {

                    entities.forEach { entity ->

                        val isSelected by remember(selectedMediaSort) {
                            derivedStateOf { entity == selectedMediaSort }
                        }

                        SortOrderView(
                            mediaSort = entity,
                            isSelected = isSelected,
                            onSortOrderClick = { sort ->

                                when {

                                    !isSelected -> {

                                        val sortOrder = MediaSort.Type.toSortOrderType(
                                            sort = sort,
                                            type = selectedSortType
                                        )

                                        coroutineScope.launch(context = Dispatchers.IO) {

                                            dataStore.setPreference(
                                                key = key,
                                                value = sortOrder.name
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }

                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {

                        val isAscSelected by remember(selectedSortType) {
                            derivedStateOf { selectedSortType == SortType.ASC }
                        }

                        val isDescSelected by remember(selectedSortType) {
                            derivedStateOf { selectedSortType == SortType.DESC }
                        }

                        SegmentedButton(
                            selected = isAscSelected,
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                            onClick = {

                                val sortOrder = MediaSort.Type.toSortOrderType(
                                    sort = selectedMediaSort,
                                    type = SortType.ASC
                                )

                                coroutineScope.launch(context = Dispatchers.IO) {

                                    dataStore.setPreference(key = key, value = sortOrder.name)
                                }
                            }
                        ) {

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = "Asc",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Icon(
                                    modifier = Modifier.size(size = 16.dp),
                                    imageVector = Icons.Filled.ArrowDownward,
                                    contentDescription = "Asc"
                                )
                            }
                        }

                        SegmentedButton(
                            selected = isDescSelected,
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                            onClick = {

                                val sortOrder = MediaSort.Type.toSortOrderType(
                                    sort = selectedMediaSort,
                                    type = SortType.DESC
                                )

                                coroutineScope.launch(context = Dispatchers.IO) {

                                    dataStore.setPreference(key = key, value = sortOrder.name)
                                }
                            }
                        ) {

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = "Desc",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Icon(
                                    modifier = Modifier.size(size = 16.dp),
                                    imageVector = Icons.Filled.ArrowUpward,
                                    contentDescription = "Desc"
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {

                Button(
                    onClick = {

                        dialogVisibleState.targetState = false
                    }
                ) {

                    Icon(
                        modifier = Modifier.size(size = 18.dp),
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(R.string.done)
                    )

                    Spacer(modifier = Modifier.width(width = 4.dp))

                    Text(
                        text = stringResource(R.string.done),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        )
    }
}

@Composable
fun FileSortOrderPreferenceDialog(
    dialogVisibleState: MutableTransitionState<Boolean>,
    key: Preferences.Key<String>,
    entities: EnumEntries<FileSort>,
) {

    val dataStore = LocalDatastore.current
    val coroutineScope = rememberCoroutineScope()

    val initialSortOrder by remember {
        derivedStateOf {

            FileSort.Type.toSortOrderType(sort = entities.first(), type = SortType.ASC).name
        }
    }

    val getFileSortOrder by dataStore.getPreference(
        key = key,
        initial = initialSortOrder
    ).collectAsStateWithLifecycle(initialValue = initialSortOrder)

    val (selectedFileSort, selectedSortType) = remember(getFileSortOrder) {
        FileSort.Type.parseSortOrderType(value = getFileSortOrder)
    }

    AnimatedVisibility(visibleState = dialogVisibleState) {

        AlertDialog(
            modifier = Modifier.fillMaxWidth(fraction = 0.95F),
            onDismissRequest = {

                dialogVisibleState.targetState = false
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = true
            ),
            shape = MaterialTheme.shapes.small,
            title = {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = stringResource(R.string.sort_order_dialog_title),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    IconButton(
                        onClick = {

                            dialogVisibleState.targetState = false
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.close)
                        )
                    }
                }
            },
            text = {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                ) {

                    entities.forEach { entity ->

                        val isSelected by remember(selectedFileSort) {
                            derivedStateOf { entity == selectedFileSort }
                        }

                        SortOrderView(
                            fileSort = entity,
                            isSelected = isSelected,
                            onSortOrderClick = { sort ->

                                when {

                                    !isSelected -> {

                                        val sortOrder = FileSort.Type.toSortOrderType(
                                            sort = sort,
                                            type = selectedSortType
                                        )

                                        coroutineScope.launch(context = Dispatchers.IO) {

                                            dataStore.setPreference(
                                                key = key,
                                                value = sortOrder.name
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }

                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {

                        val isAscSelected by remember(selectedSortType) {
                            derivedStateOf { selectedSortType == SortType.ASC }
                        }

                        val isDescSelected by remember(selectedSortType) {
                            derivedStateOf { selectedSortType == SortType.DESC }
                        }

                        SegmentedButton(
                            selected = isAscSelected,
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                            onClick = {

                                val sortOrder = FileSort.Type.toSortOrderType(
                                    sort = selectedFileSort,
                                    type = SortType.ASC
                                )

                                coroutineScope.launch(context = Dispatchers.IO) {

                                    dataStore.setPreference(key = key, value = sortOrder.name)
                                }
                            }
                        ) {

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = "Asc",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Icon(
                                    modifier = Modifier.size(size = 16.dp),
                                    imageVector = Icons.Filled.ArrowDownward,
                                    contentDescription = "Asc"
                                )
                            }
                        }

                        SegmentedButton(
                            selected = isDescSelected,
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                            onClick = {

                                val sortOrder = FileSort.Type.toSortOrderType(
                                    sort = selectedFileSort,
                                    type = SortType.DESC
                                )

                                coroutineScope.launch(context = Dispatchers.IO) {

                                    dataStore.setPreference(key = key, value = sortOrder.name)
                                }
                            }
                        ) {

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = "Desc",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Icon(
                                    modifier = Modifier.size(size = 16.dp),
                                    imageVector = Icons.Filled.ArrowUpward,
                                    contentDescription = "Desc"
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {

                Button(
                    onClick = {

                        dialogVisibleState.targetState = false
                    }
                ) {

                    Icon(
                        modifier = Modifier.size(size = 18.dp),
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(R.string.done)
                    )

                    Spacer(modifier = Modifier.width(width = 4.dp))

                    Text(
                        text = stringResource(R.string.done),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        )
    }
}

@Composable
private fun SortOrderView(
    mediaSort: MediaSort,
    isSelected: Boolean,
    onSortOrderClick: (sort: MediaSort) -> Unit
) {

    val cardColors = CardDefaults.cardColors(
        containerColor = when (isSelected) {

            true -> MaterialTheme.colorScheme.primaryFixedDim
            false -> CardDefaults.cardColors().containerColor
        },
        contentColor = when (isSelected) {

            true -> MaterialTheme.colorScheme.onPrimaryFixedVariant
            false -> CardDefaults.cardColors().contentColor
        }
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraSmall,
        colors = cardColors,
        onClick = {

            onSortOrderClick(mediaSort)
        }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = when (isSelected) {

                    true -> mediaSort.selectedIcon
                    false -> mediaSort.deselectedIcon
                },
                contentDescription = mediaSort.label
            )

            Text(
                modifier = Modifier.weight(weight = 1.0F),
                text = mediaSort.label,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SortOrderView(
    fileSort: FileSort,
    isSelected: Boolean,
    onSortOrderClick: (sort: FileSort) -> Unit
) {

    val cardColors = CardDefaults.cardColors(
        containerColor = when (isSelected) {

            true -> MaterialTheme.colorScheme.primaryFixedDim
            false -> CardDefaults.cardColors().containerColor
        },
        contentColor = when (isSelected) {

            true -> MaterialTheme.colorScheme.onPrimaryFixedVariant
            false -> CardDefaults.cardColors().contentColor
        }
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraSmall,
        colors = cardColors,
        onClick = {

            onSortOrderClick(fileSort)
        }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = when (isSelected) {

                    true -> fileSort.selectedIcon
                    false -> fileSort.deselectedIcon
                },
                contentDescription = fileSort.label
            )

            Text(
                modifier = Modifier.weight(weight = 1.0F),
                text = fileSort.label,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}