package io.asanre.app.ui.characterDetail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.asanre.app.R
import io.asanre.app.core.ui.components.LoadingIndicator
import io.asanre.app.domain.entities.CharacterDetails
import io.asanre.app.ui.characterDetail.CharacterDetailScreen.Event
import org.koin.compose.koinInject

object CharacterDetailScreen {
    data class State(
        val character: CharacterDetails? = null,
        val error: Boolean = false,
        val onEvent: (Event) -> Unit
    )

    sealed class Event {
        object ErrorShown : Event()
        data class GetDetail(val id: Int) : Event()
    }
}

@Composable
fun CharacterDetailScreen(
    characterId: Int,
    modifier: Modifier = Modifier,
    viewModel: CharacterDetailViewmodel = koinInject(),
    onError: () -> Unit = { },
    onCloseClick: () -> Unit,
) {

    val state by viewModel.state.collectAsState()
    LaunchedEffect(characterId) {
        state.onEvent(Event.GetDetail(characterId))
    }

    val showError by remember { derivedStateOf { state.error } }
    if (showError) {
        onError()
        state.onEvent(Event.ErrorShown)
    }

    Crossfade(targetState = state.character) { detail ->
        if (detail == null) {
            LoadingIndicator(modifier.fillMaxSize())
        } else {
            DetailContent(
                modifier = modifier,
                character = detail,
                onCloseClick = onCloseClick,
            )
        }
    }
}

@Composable
private fun DetailContent(
    modifier: Modifier,
    character: CharacterDetails,
    onCloseClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box {
            Image(character.imageUrl)
            BackIcon(
                modifier = Modifier.align(Alignment.TopStart),
                onCloseClick = onCloseClick
            )
        }

        Details(character)
    }
}

@Composable
private fun Details(detail: CharacterDetails) {
    Column(Modifier.padding(12.dp)) {
        Text(detail.name, style = MaterialTheme.typography.h3)
        Text(
            text = "${detail.status} - ${detail.species}",
            style = MaterialTheme.typography.caption
        )
        Spacer(modifier = Modifier.padding(top = 24.dp))

        detail.origin?.let {
            DetailSection(stringResource(R.string.detail_original_from), it.value)
        }

        detail.lastLocation?.let {
            DetailSection(stringResource(R.string.detail_last_known_location), it.value)
        }

        detail.firstEpisode?.let {
            DetailSection(stringResource(R.string.detail_first_seen_in), it.name)
        }
    }
}

@Composable
private fun BackIcon(
    modifier: Modifier,
    onCloseClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onCloseClick
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = stringResource(R.string.detail_close)
        )
    }
}

@Composable
private fun Image(imageUrl: String) {
    AsyncImage(
        imageUrl,
        contentDescription = null,
        alignment = Alignment.TopStart,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.25f)
            .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)),
        placeholder = painterResource(id = R.drawable.place_holder),
        error = painterResource(id = R.drawable.error_place_holder)
    )
}

@Composable
private fun DetailSection(label: String, value: String) {
    Spacer(modifier = Modifier.padding(top = 12.dp))
    Text(
        label,
        style = MaterialTheme.typography.body1,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(modifier = Modifier.padding(top = 4.dp))
    Text(value, style = MaterialTheme.typography.subtitle1)
}