package io.bashpsk.zerodownload.core.ui.action

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.bashpsk.zerodownload.core.model.action.ContextualAction
import kotlinx.collections.immutable.ImmutableList

@Composable
inline fun ContextualActionBar(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    actionList: ImmutableList<ContextualAction>,
    crossinline onActionClick: (action: ContextualAction) -> Unit,
    windowInsets: WindowInsets = BottomAppBarDefaults.windowInsets
) {

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 }
    ) {

        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = BottomAppBarDefaults.containerColor),
            contentAlignment = Alignment.Center
        ) {

            AppBarRow(
                modifier = Modifier.windowInsetsPadding(windowInsets),
                overflowIndicator = { state ->

                    ContextualActionBarItem(
                        optionData = ContextualAction.More(),
                        onClick = {

                            if (state.isShowing) state.dismiss() else state.show()
                            onActionClick(ContextualAction.More())
                        }
                    )
                }
            ) {

                actionList.forEach { item ->

                    customItem(
                        appbarContent = {

                            ContextualActionBarItem(
                                optionData = item,
                                onClick = { onActionClick(item) }
                            )
                        },
                        menuContent = {

                            ContextualActionMenuItem(
                                optionData = item,
                                onClick = {

                                    it.dismiss()
                                    onActionClick(item)
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}