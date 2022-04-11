package com.ralphordanza.compose_mentions

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ralphordanza.compose_mentions.ui.theme.ComposePlaygroundTheme
import com.ralphordanza.compose_mentions.ComposeMentions

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val members = listOf(
            Member(
                id = "1",
                name = "John Doe",
                role = "Android"
            ),
            Member(
                id = "2",
                name = "Keanu Reeves",
                role = "iOS"
            ),
            Member(
                id = "3",
                name = "Gal Gadot",
                role = "Wonder Woman"
            ),
            Member(
                id = "4",
                name = "Peter Parker",
                role = "Spider-Man"
            ),
            Member(
                id = "1",
                name = "John Doe",
                role = "Android"
            ),
            Member(
                id = "2",
                name = "Keanu Reeves",
                role = "iOS"
            ),
            Member(
                id = "3",
                name = "Gal Gadot",
                role = "Wonder Woman"
            ),
            Member(
                id = "4",
                name = "Peter Parker",
                role = "Spider-Man"
            ),
            Member(
                id = "1",
                name = "John Doe",
                role = "Android"
            ),
            Member(
                id = "2",
                name = "Keanu Reeves",
                role = "iOS"
            ),
            Member(
                id = "3",
                name = "Gal Gadot",
                role = "Wonder Woman"
            ),
            Member(
                id = "4",
                name = "Peter Parker",
                role = "Spider-Man"
            )
        )

        setContent {
            ComposePlaygroundTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("SAMPLE")
                            }
                        )
                    }
                ){
                    ComposeMentions(
                        modifier = Modifier.fillMaxSize(),
                        placeholder = {
                            Text("Enter your thoughts")
                        },
                        trigger = "@",
                        data = members.map {
                            mapOf(
                                "id" to it.id,
                                "display" to it.name,
                                "member" to it
                            )
                        },
                        onMarkupChanged = {
                            Log.d("MARKUP", it)
                        },
                        suggestionItemBuilder = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                            ) {
                                Text(it["id"].toString())
                                Text(it["display"].toString())
                                Text((it["member"] as Member).role)
                            }
                        },
                        markupBuilder = { trigger, id, display ->
                            "[$trigger$display](profile/$id)"
                        }
                    )
                }
            }
        }
    }
}

data class Member(
    val id: String,
    val name: String,
    val role: String,
)

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposePlaygroundTheme {
        Greeting("Android")
    }
}