package com.bd.cyclists.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import com.bd.cyclists.data.model.Post
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bd.cyclists.MainViewModel
import com.bd.cyclists.data.model.MovieItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun Activities(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel()
) {
//    // Observe the UI state from the ViewModel
//    // use collectAsStateWithLifecycle for lifecycle-aware collection (recommended)
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//    // Alternatively, for simpler cases or older compose versions:
//    // val uiState by viewModel.uiState.collectAsState()
//    val listState = rememberLazyListState()
//
//    MyLazyColumn(uiState.posts, {
//        println("Loading more")
//    }, uiState.isLoading)

    val movieItems = viewModel.moviePagingSource.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(
            count = movieItems.itemCount
        ) { index ->
            movieItems[index]?.let {
                // Your composable for a single activity item
                MovieItem(item = it)
            }
        }

        // Handle loading and error states for initial load and subsequent pages
        movieItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> { // Initial load
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                loadState.append is LoadState.Loading -> { // Loading next page
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                loadState.refresh is LoadState.Error -> { // Initial load error
                    val error = loadState.refresh as LoadState.Error
                    item {
                        ErrorMessage(
                            message = "Error loading activities: ${error.error.localizedMessage}",
                            modifier = Modifier.fillParentMaxSize(),
                            onRetryClick = { retry() } // Provide a retry mechanism
                        )
                    }
                }

                loadState.append is LoadState.Error -> { // Next page load error
                    val error = loadState.append as LoadState.Error
                    item {
                        ErrorMessage(
                            message = "Error loading more: ${error.error.localizedMessage}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            onRetryClick = { retry() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(item: MovieItem) {
    // Design your individual activity item here
    Text(text = "Title: ${item.title}", modifier = Modifier.padding(8.dp))
    // Add more UI elements for your activity item
}

@Composable
fun ErrorMessage(message: String, modifier: Modifier = Modifier, onRetryClick: () -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetryClick) {
            Text("Retry")
        }
    }
}

@Composable
fun MyLazyColumn(
    items: List<Post>,
    loadMore: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LazyColumn(state = listState, modifier = modifier) {
        item {
            Text(text = "Header")
        }

        items(items) { item ->
            Text(item.title)
        }

        // Optional: Loading indicator at the bottom
        if (isLoading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }

    // Check if we're near the bottom and trigger loadMore
    LaunchedEffect(listState.layoutInfo.visibleItemsInfo.lastOrNull()) {
        if (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == items.size - 3) { // Adjust '3' as needed
            loadMore()
        }
    }
}

@Composable
fun SimpleSearchBarExample() {
    // Create and remember the text field state
    val textFieldState = rememberTextFieldState()
    val items = listOf(
        "Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb",
        "Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop"
    )

    // Filter items based on the current search text
    val filteredItems by remember {
        derivedStateOf {
            val searchText = textFieldState.text.toString()
            if (searchText.isEmpty()) {
                emptyList()
            } else {
                items.filter { it.contains(searchText, ignoreCase = true) }
            }
        }
    }

    SimpleSearchBar(
        textFieldState = textFieldState,
        onSearch = { /* Handle search submission */ },
        searchResults = filteredItems
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    modifier: Modifier = Modifier
) {
    // Controls expansion state of the search bar
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search") }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            // Display search results in a scrollable column
            Column(Modifier.verticalScroll(rememberScrollState())) {
                searchResults.forEach { result ->
                    ListItem(
                        headlineContent = { Text(result) },
                        modifier = Modifier
                            .clickable {
                                textFieldState.edit { replace(0, length, result) }
                                expanded = false
                            }
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizableSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
    // Customization options
    placeholder: @Composable () -> Unit = { Text("Search") },
    leadingIcon: @Composable (() -> Unit)? = {
        Icon(
            Icons.Default.Search,
            contentDescription = "Search"
        )
    },
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingContent: (@Composable (String) -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // Track expanded state of search bar
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                // Customizable input field implementation
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        onSearch(query)
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            // Show search results in a lazy column for better performance
            LazyColumn {
                items(count = searchResults.size) { index ->
                    val resultText = searchResults[index]
                    ListItem(
                        headlineContent = { Text(resultText) },
                        supportingContent = supportingContent?.let { { it(resultText) } },
                        leadingContent = leadingContent,
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .clickable {
                                onResultClick(resultText)
                                expanded = false
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomizableSearchBarExample() {
    // Manage query state
    var query by rememberSaveable { mutableStateOf("") }
    val items = listOf(
        "Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb",
        "Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop", "Marshmallow",
        "Nougat", "Oreo", "Pie"
    )

    // Filter items based on query
    val filteredItems by remember {
        derivedStateOf {
            if (query.isEmpty()) {
                items
            } else {
                items.filter { it.contains(query, ignoreCase = true) }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CustomizableSearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = { /* Handle search submission */ },
            searchResults = filteredItems,
            onResultClick = { query = it },
            // Customize appearance with optional parameters
            placeholder = { Text("Search desserts") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = "More options") },
            supportingContent = { Text("Android dessert") },
            leadingContent = { Icon(Icons.Filled.Star, contentDescription = "Starred item") }
        )

        // Display the filtered list below the search bar
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 72.dp, // Provides space for the search bar
                end = 16.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.semantics {
                traversalIndex = 1f
            },
        ) {
            items(count = filteredItems.size) {
                Text(text = filteredItems[it])
            }
        }
    }
}