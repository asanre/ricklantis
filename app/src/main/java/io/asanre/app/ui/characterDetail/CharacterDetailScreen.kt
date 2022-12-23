package io.asanre.app.ui.characterDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.asanre.app.domain.entities.CharacterDetails
import org.koin.androidx.compose.koinViewModel

data class CharacterDetailState(
    val value: CharacterDetails?, val showError: Boolean
) {


    companion object {
        val INITIAL = CharacterDetailState(null, false)
    }
}

@Composable
fun CharacterDetailScreen(
    characterId: Int,
    viewModel: CharacterDetailViewmodel = koinViewModel(),
    onError: () -> Unit = {},
) {
    LaunchedEffect(characterId) { viewModel.getDetail(characterId) }
    val state by viewModel.state.collectAsState()

    if (state.showError) {
        onError()
        viewModel.onErrorShown()
    }

    Column(Modifier.fillMaxSize()) {
        Text(text = "Detail baby")
        state.value?.also { detail ->
            Text(text = detail.name)
        }
    }
}