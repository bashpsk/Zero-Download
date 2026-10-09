package io.bashpsk.zerodownload.feature.home.bottombar

import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import io.bashpsk.zerodownload.core.model.action.ContextualAction
import io.bashpsk.zerodownload.core.navigation.screen.NavScreen
import io.bashpsk.zerodownload.core.ui.action.ContextualActionBar
import io.bashpsk.zerodownload.feature.home.event.HomeUIEvent
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun HomeBottomBar(
    modifier: Modifier = Modifier,
    isMediaSelect: Boolean,
    onNavigateScreen: (navScreen: NavScreen) -> Unit,
    onUIEvent: (uiEvent: HomeUIEvent) -> Unit,
    scrollBehavior: SearchBarScrollBehavior?
) {

    val actionList by remember {
        derivedStateOf {
            persistentListOf(
                ContextualAction.SelectNone(),
            )
        }
    }

    val onActionClick = remember<(ContextualAction) -> Unit> {
        { action ->

            when (action) {

                is ContextualAction.SelectNone -> {

                    onUIEvent(HomeUIEvent.MediaSelect(isVisible = false))
                    onUIEvent(HomeUIEvent.ResetSelectedFormat)
                }

                else -> {}
            }
        }
    }

    val nestedScrollModifier = scrollBehavior?.nestedScrollConnection?.let { connection ->
        Modifier.nestedScroll(connection = connection)
    } ?: Modifier

    ContextualActionBar(
        modifier = modifier.then(nestedScrollModifier),
        isVisible = isMediaSelect,
        actionList = actionList,
        onActionClick = onActionClick
    )
}