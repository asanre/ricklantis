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
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.asanre.app.R
import io.asanre.app.core.ui.components.LoadingIndicator
import io.asanre.app.domain.entities.CharacterDetails
import io.asanre.app.ui.characterDetail.CharacterDetailScreen.Event
import io.github.xxfast.decompose.router.rememberOnRoute
import org.koin.compose.koinInject

object CharacterDetailScreen {
    data class State(
        val value: CharacterDetails? = null,
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

    val instance =
        rememberOnRoute(CharacterDetailViewmodel::class, characterId) { viewModel }
    val state by instance.state.collectAsState()

    LaunchedEffect(characterId) {
        state.onEvent(Event.GetDetail(characterId))
    }

    if (state.error) {
        onError()
        state.onEvent(Event.ErrorShown)
    }

    val details = state.value
    Crossfade(targetState = details) { detail ->
        if (detail == null) {
            LoadingIndicator(modifier.fillMaxSize())
        } else {
            DetailContent(
                modifier = modifier,
                detail = detail,
                onCloseClick = onCloseClick,
            )
        }
    }
}

@Composable
private fun DetailContent(
    modifier: Modifier,
    detail: CharacterDetails,
    onCloseClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box {
            AsyncImage(
                detail.imageUrl,
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
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                onClick = onCloseClick
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "close")
            }
        }

        Column(Modifier.padding(12.dp)) {
            Text(detail.name, style = MaterialTheme.typography.h3)
            Text(
                text = "${detail.status} - ${detail.species}",
                style = MaterialTheme.typography.caption
            )
            Spacer(modifier = Modifier.padding(top = 24.dp))

            detail.origin?.let {
                DetailSection("Original From:", it.value)
            }

            detail.lastLocation?.let {
                DetailSection("Last known location:", it.value)
            }

            detail.firstEpisode?.let {
                DetailSection("First seen in:", it.name)
            }
        }
    }
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