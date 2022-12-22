package io.asanre.app.ui.characterList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.asanre.app.R
import io.asanre.app.core.ui.components.LoadingIndicator
import io.asanre.app.domain.entities.CharacterEntity
import io.asanre.app.domain.entities.CharacterList
import io.asanre.app.domain.entities.Location
import io.asanre.app.domain.entities.Status
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

data class CharacterListState(
    val characters: List<CharacterListItem>,
    val showError: Boolean,
    private val hideLoading: Boolean,
    private val hasAllCharacters: Boolean,
) {
    val showLoading: Boolean = !(hideLoading || hasAllCharacters)
    val count = characters.size.takeUnless { hasAllCharacters }
    val showEmptyScreen: Boolean = !showLoading && characters.isEmpty()

    fun addCharacters(characterList: CharacterList) = copy(
        characters = characters + characterList.characters.map { it.toItem() },
        hideLoading = false,
        hasAllCharacters = characterList.allCharacterLoaded
    )

    fun showError() = copy(showError = true, hideLoading = true)
    fun dismissError() = copy(showError = false)

    companion object {
        val INITIAL = CharacterListState(
            characters = emptyList(),
            showError = false,
            hasAllCharacters = false,
            hideLoading = false
        )
    }
}

data class CharacterListItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val status: Status,
    val species: String,
    val lastLocation: Location
)

fun CharacterEntity.toItem() = CharacterListItem(
    id = id,
    name = name,
    imageUrl = imageUrl,
    status = status,
    species = species,
    lastLocation = lastLocation
)

@Composable
fun CharactersScreen(viewmodel: CharacterListViewmodel = koinViewModel()) {
    LaunchedEffect(viewmodel) { viewmodel.getCharacters() }
    val state by viewmodel.state.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()


    val message = stringResource(R.string.generic_error)
    if (state.showError) LaunchedEffect(scaffoldState) {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(message)
            viewmodel.onErrorShown()
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        if (state.showEmptyScreen)
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                OutlinedButton(onClick = { viewmodel.getCharacters() }) {
                    Text(text = "Retry")
                }
            }
        else
            CharacterListContent(
                state = state,
                modifier = Modifier.padding(it),
                loadMoreCharacters = { viewmodel.getCharacters() },
                onItemClick = { }
            )
    }

}

@Composable
private fun CharacterListContent(
    state: CharacterListState,
    modifier: Modifier = Modifier,
    loadMoreCharacters: () -> Unit,
    onItemClick: (CharacterListItem) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        itemsIndexed(state.characters) { index, item ->
            if (index == state.characters.lastIndex) {
                LaunchedEffect(state, index) { loadMoreCharacters() }
            }

            CharacterView(item = item, onItemClick = onItemClick)
        }

        if (state.showLoading) {
            item {
                LoadingIndicator(Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun CharacterView(item: CharacterListItem, onItemClick: (CharacterListItem) -> Unit) {
    Card(
        elevation = 4.dp,
        modifier = Modifier.clickable { onItemClick(item) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            Column {
                Text(
                    item.name,
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    item.species,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}