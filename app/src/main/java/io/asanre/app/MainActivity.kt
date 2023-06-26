package io.asanre.app

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
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

@Parcelize
sealed class Screen : Parcelable {
    object Characters : Screen()

    data class CharacterDetail(val characterId: Int) : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val rootComponentContext: DefaultComponentContext = defaultComponentContext()
            CompositionLocalProvider(LocalComponentContext provides rootComponentContext) {
                RicklantisTheme {
                    val scaffoldState = rememberScaffoldState()
                    Scaffold(scaffoldState = scaffoldState) {
                        NavGraph(Modifier.padding(it), scaffoldState)
                    }
                }
            }
        }
    }

}

@Composable
private fun NavGraph(modifier: Modifier, scaffoldState: ScaffoldState) {
    val showError = ShowGenericError(scaffoldState)
    val router: Router<Screen> = rememberRouter(Screen::class, listOf(Screen.Characters))

    RoutedContent(
        router = router,
        modifier = modifier,
        animation = stackAnimation(slide() + scale())
    ) { screen ->
        when (screen) {
            Screen.Characters -> {
                CharactersScreen(
                    onError = { showError() },
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
                        showError()
                    },
                )
            }
        }
    }
}

@Composable
private fun ShowGenericError(scaffoldState: ScaffoldState): () -> Unit {
    val scope = rememberCoroutineScope()
    val errorMessage = stringResource(R.string.generic_error)
    return {
        scope.launch {
            scaffoldState.snackbarHostState.showSnackbar(errorMessage)
        }
    }
}