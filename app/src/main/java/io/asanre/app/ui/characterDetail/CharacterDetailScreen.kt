package io.asanre.app.ui.characterDetail

import androidx.compose.foundation.layout.*
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
import io.asanre.app.domain.entities.CharacterDetails
import org.koin.androidx.compose.koinViewModel

data class CharacterDetailState(
    val value: CharacterDetails?, val error: Boolean
) {

    fun displayDetail(detail: CharacterDetails) = copy(value = detail)
    fun onError() = copy(error = true)
    fun dismissError() = copy(error = false)

    companion object {
        val INITIAL = CharacterDetailState(null, false)
    }
}

@Composable
fun CharacterDetailScreen(
    characterId: Int,
    modifier: Modifier = Modifier,
    viewModel: CharacterDetailViewmodel = koinViewModel(),
    onError: () -> Unit = { },
    onCloseClick: () -> Unit,
) {
    LaunchedEffect(characterId) { viewModel.getDetail(characterId) }
    val state by viewModel.state.collectAsState()

    if (state.error) {
        onError()
        viewModel.onErrorShown()
    }

    state.value?.let { detail ->
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
            state.value?.also { detail ->
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