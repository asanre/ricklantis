package io.asanre.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.asanre.app.core.ui.theme.RicklantisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RicklantisTheme {
                CharacterListScreen()
            }
        }
    }
}

@Composable
private fun CharacterListScreen() {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(20.dp)
            .clickable { }
    ) {
        TestContent()
    }
}

@Composable
private fun TestContent() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
    ) {
        Column {
            Text(
                "Rick",
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                "Human",
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RicklantisTheme {
        CharacterListScreen()
    }
}