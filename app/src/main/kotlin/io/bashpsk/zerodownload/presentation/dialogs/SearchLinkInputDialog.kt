package io.bashpsk.zerodownload.presentation.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.presentation.widgets.DialogTitleView
import io.bashpsk.zerodownload.ytdlext.utils.hasPlaylistLink

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchLinkInputDialog(
    dialogVisibleState: MutableTransitionState<Boolean>,
    onSearchClick: (link: String) -> Unit
) {

    val linkFieldState = rememberTextFieldState(initialText = "")

    val isPlaylistLink by remember(linkFieldState) {
        derivedStateOf { linkFieldState.text.toString().hasPlaylistLink() }
    }

    AnimatedVisibility(
        visibleState = dialogVisibleState,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
    ) {

        AlertDialog(
            modifier = Modifier.fillMaxWidth(fraction = 0.95F),
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
                    title = stringResource(R.string.search_link_input_dialog_title),
                    onClick = {

                        dialogVisibleState.targetState = false
                    }
                )
            },
            text = {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = 4.dp)
                ) {

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        state = linkFieldState,
                        shape = MaterialTheme.shapes.extraSmall,
                        lineLimits = TextFieldLineLimits.SingleLine,
                        label = {

                            Text(
                                text = stringResource(R.string.link_input_label),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        placeholder = {

                            Text(
                                text = stringResource(R.string.link_input_label),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        leadingIcon = {

                            IconButton(
                                enabled = linkFieldState.undoState.canUndo,
                                onClick = {

                                    linkFieldState.undoState.undo()
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Undo,
                                    contentDescription = stringResource(R.string.undo)
                                )
                            }
                        },
                        trailingIcon = {

                            Row(
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.spacedBy(space = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                IconButton(
                                    onClick = {

                                        onSearchClick(linkFieldState.text.toString())
                                        dialogVisibleState.targetState = false
                                    }
                                ) {

                                    Icon(
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = stringResource(R.string.search)
                                    )
                                }
                            }
                        },
                        supportingText = {

                            if (isPlaylistLink) Text(
                                text = stringResource(R.string.playlist_url_detected),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Uri,
                            capitalization = KeyboardCapitalization.None,
                            autoCorrectEnabled = false,
                            imeAction = ImeAction.Search
                        ),
                        onKeyboardAction = KeyboardActionHandler {

                            onSearchClick(linkFieldState.text.toString())
                            dialogVisibleState.targetState = false
                        }
                    )
                }
            },
            confirmButton = {

                Button(
                    onClick = {

                        dialogVisibleState.targetState = false
                    }
                ) {

                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.cancel)
                    )

                    Spacer(modifier = Modifier.width(width = 4.dp))

                    Text(
                        text = stringResource(R.string.cancel),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        )
    }
}