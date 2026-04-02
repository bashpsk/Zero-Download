package io.bashpsk.zerodownload.presentation.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.states.MediaSearchState
import io.bashpsk.zerodownload.presentation.widgets.DialogTitleView

@Composable
fun MediaDetailDialog(
    dialogVisibleState: MutableTransitionState<Boolean>,
    searchState: MediaSearchState
) {

    AnimatedVisibility(
        visibleState = dialogVisibleState,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
    ) {

        AlertDialog(
            modifier = Modifier.fillMaxSize(),
            onDismissRequest = {

                dialogVisibleState.targetState = false
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            ),
            shape = MaterialTheme.shapes.extraSmall,
            title = {

                DialogTitleView(
                    title = stringResource(R.string.media_details_dialog_title),
                    onClick = {

                        dialogVisibleState.targetState = false
                    }
                )
            },
            text = {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = 4.dp)
                ) {

                    when (searchState) {

                        is MediaSearchState.Success -> {

                            item {

                                SelectionContainer {

                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(space = 4.dp)
                                    ) {

                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = "${stringResource(R.string.title)}:",
                                            textAlign = TextAlign.Start,
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = searchState.media.title,
                                            textAlign = TextAlign.Start,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }

                            item {

                                SelectionContainer {

                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(space = 4.dp)
                                    ) {

                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = "${stringResource(R.string.description)}:",
                                            textAlign = TextAlign.Start,
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = searchState.media.description,
                                            textAlign = TextAlign.Start,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }

                        else -> {}
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
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(R.string.ok)
                    )

                    Spacer(modifier = Modifier.width(width = 4.dp))

                    Text(
                        text = stringResource(R.string.ok),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        )
    }
}