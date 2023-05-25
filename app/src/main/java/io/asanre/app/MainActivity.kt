package io.asanre.app

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import io.asanre.app.core.ui.theme.RicklantisTheme
import io.asanre.app.ui.characterDetail.CharacterDetailScreen
import io.asanre.app.ui.characterList.CharactersScreen
import io.github.xxfast.decompose.LocalComponentContext
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

sealed class Screen : Parcelable {
    @Parcelize
    object Characters : Screen()

    @Parcelize
    data class CharacterDetail(val characterId: Int) : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val rootComponentContext: DefaultComponentContext = defaultComponentContext()
            CompositionLocalProvider(LocalComponentContext provides rootComponentContext) {
                RicklantisTheme {
                    val router: Router<Screen> =
                        rememberRouter(Screen::class, listOf(Screen.Characters))

                    val errorMessage = stringResource(R.string.generic_error)
                    val scaffoldState = rememberScaffoldState()
                    val coroutineScope = rememberCoroutineScope()


                    fun showError(message: String) {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }

                    Scaffold(scaffoldState = scaffoldState) {
                        RoutedContent(router = router, Modifier.padding(it)) { screen ->
                            when (screen) {
                                Screen.Characters -> {
                                    CharactersScreen(
                                        onError = { showError(errorMessage) },
                                        onItemClick = { item ->
                                            router.push(Screen.CharacterDetail(item.id))
                                        })
                                }

                                is Screen.CharacterDetail -> {
                                    CharacterDetailScreen(
                                        characterId = screen.characterId,
                                        onCloseClick = { router.pop() },
                                        onError = {
                                            router.pop()
                                            showError(errorMessage)
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}