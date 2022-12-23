package io.asanre.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.asanre.app.core.ui.components.modal.ModalBottomSheet
import io.asanre.app.core.ui.components.modal.ModalBottomSheetContainer
import io.asanre.app.core.ui.components.modal.SheetMode
import io.asanre.app.core.ui.components.modal.rememberModalSheetState
import io.asanre.app.core.ui.theme.RicklantisTheme
import io.asanre.app.ui.characterDetail.CharacterDetailScreen
import io.asanre.app.ui.characterList.CharactersScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RicklantisTheme {
                val errorMessage = stringResource(R.string.generic_error)
                val scaffoldState = rememberScaffoldState()
                val coroutineScope = rememberCoroutineScope()

                val modalState = rememberModalSheetState(mode = SheetMode.FULL_HEIGHT)
                var selectedCharacter by remember { mutableStateOf<Int?>(null) }

                fun showError(message: String) {
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                }

                Scaffold(scaffoldState = scaffoldState) {
                    ModalBottomSheetContainer(modal = {
                        ModalBottomSheet(state = modalState, sheetContent = {
                            selectedCharacter?.let { characterId ->
                                CharacterDetailScreen(
                                    characterId = characterId,
                                    onError = {
                                        modalState.hide()
                                        showError(errorMessage)
                                    },
                                )
                            }
                        })
                    }) {
                        CharactersScreen(modifier = Modifier.padding(it),
                            onError = { showError(errorMessage) },
                            onItemClick = {
                                selectedCharacter = it.id
                                modalState.show()
                            })
                    }
                }
            }
        }
    }
}