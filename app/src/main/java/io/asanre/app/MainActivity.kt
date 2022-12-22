package io.asanre.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.asanre.app.core.ui.theme.RicklantisTheme
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

                fun showError(message: String) {
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                }
                Scaffold(scaffoldState = scaffoldState) {
                    CharactersScreen(
                        modifier = Modifier.padding(it),
                        showError = {
                            showError(errorMessage)
                        })
                }
            }
        }
    }
}