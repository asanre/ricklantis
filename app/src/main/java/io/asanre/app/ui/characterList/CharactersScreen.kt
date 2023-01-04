package io.asanre.app.ui.characterList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.asanre.app.R
import io.asanre.app.core.ui.components.LoadingIndicator
import io.asanre.app.domain.entities.Character
import io.asanre.app.domain.entities.CharacterList
import io.asanre.app.domain.entities.Location
import io.asanre.app.domain.entities.Status
import org.koin.androidx.compose.koinViewModel

data class CharacterListState(
    val characters: List<CharacterListItem>,
    val error: Boolean,
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

    fun showError() = copy(error = true, hideLoading = true)
    fun dismissError() = copy(error = false)

    companion object {
        val INITIAL = CharacterListState(
            characters = emptyList(),
            error = false,
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

fun Character.toItem() = CharacterListItem(
    id = id,
    name = name,
    imageUrl = imageUrl,
    status = status,
    species = species,
    lastLocation = lastLocation
)

@Composable
fun CharactersScreen(
    viewmodel: CharacterListViewmodel = koinViewModel(),
    modifier: Modifier = Modifier,
    onError: () -> Unit = {},
    onItemClick: (CharacterListItem) -> Unit,
) {
    LaunchedEffect(viewmodel) { viewmodel.getCharacters() }
    val state by viewmodel.state.collectAsState()
    if (state.error) {
        onError()
        viewmodel.onErrorShown()
    }

    if (state.showEmptyScreen)
        EmptyView(modifier) { viewmodel.getCharacters() }
    else
        CharacterListContent(
            state = state,
            modifier = modifier,
            loadMoreCharacters = viewmodel::getCharacters,
            onItemClick = onItemClick
        )
}

@Composable
private fun EmptyView(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        OutlinedButton(onClick = onClick) {
            Text(text = "Retry")
        }
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
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                item.imageUrl,
                contentDescription = null,
                alignment = Alignment.TopStart,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.33f)
                    .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)),
                placeholder = painterResource(id = R.drawable.place_holder),
                error = painterResource(id = R.drawable.error_place_holder)
            )
            Column(modifier = Modifier.padding(10.dp)) {
                Text(item.name, style = MaterialTheme.typography.subtitle1)
                Text(
                    text = "${item.status} - ${item.species}",
                    style = MaterialTheme.typography.caption
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    stringResource(R.string.last_location_title),
                    color = LocalContentColor.current.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.caption
                )
                Text(
                    item.lastLocation.name,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

            }
        }
    }
}