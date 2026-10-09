package io.bashpsk.zerodownload.core.ui.topbar

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExpandedDockedSearchBar
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.Text
import androidx.compose.material3.rememberContainedSearchBarState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
    placeholder: String,
    scrollBehavior: SearchBarScrollBehavior? = null,
    onSearch: (query: String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {}
) {

    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()
    val coroutineScope = rememberCoroutineScope()
    val appBarWithSearchColors = SearchBarDefaults.appBarWithSearchColors()

    val inputField = @Composable {

        SearchBarDefaults.InputField(
            textFieldState = textFieldState,
            searchBarState = searchBarState,
            colors = appBarWithSearchColors.searchBarColors.inputFieldColors,
            onSearch = { query ->

                coroutineScope.launch { searchBarState.animateToCollapsed() }
                onSearch(query)
            },
            keyboardOptions = keyboardOptions.copy(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Search
            ),
            placeholder = {

                Text(
                    text = placeholder,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    }

    AppBarWithSearch(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        state = searchBarState,
        colors = appBarWithSearchColors,
        inputField = inputField,
        navigationIcon = navigationIcon,
        actions = actions,
        windowInsets = SearchBarDefaults.windowInsets
    )

    ExpandedDockedSearchBar(
        state = searchBarState,
        inputField = inputField,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFullTopBar(
    modifier: Modifier = Modifier,
    placeholder: String,
    scrollBehavior: SearchBarScrollBehavior? = null,
    onSearch: (query: String) -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {

    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberContainedSearchBarState()
    val coroutineScope = rememberCoroutineScope()
    val appBarWithSearchColors = SearchBarDefaults.appBarWithSearchColors()

    val inputField = @Composable {

        SearchBarDefaults.InputField(
            textFieldState = textFieldState,
            searchBarState = searchBarState,
            colors = appBarWithSearchColors.searchBarColors.inputFieldColors,
            onSearch = { query ->

                coroutineScope.launch { searchBarState.animateToCollapsed() }
                onSearch(query)
            },
            placeholder = {

                Text(
                    text = placeholder,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    }

    AppBarWithSearch(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        state = searchBarState,
        colors = appBarWithSearchColors,
        inputField = inputField,
        navigationIcon = navigationIcon,
        actions = actions,
    )

    ExpandedFullScreenSearchBar(
        state = searchBarState,
        inputField = inputField,
        content = content
    )
}