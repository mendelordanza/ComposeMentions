package com.ralphordanza.compose_mentions

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            var message by remember {
                mutableStateOf(TextFieldValue(annotatedString = AnnotatedString(text = "")))
            }
            ComposePlaygroundTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("SAMPLE")
                            }
                        )
                    }
                ) {
                    Row {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(30.dp)),
                        ) {
                            ComposeMentions(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                dropdownMaxHeight = (LocalConfiguration.current.screenHeightDp / 2).dp,
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
                                    Log.d("MARKDOWN", it)
                                },
                                message = message,
                                onValueChanged = {
                                    message = it
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
                        Button(
                            onClick = {
                                message =
                                    TextFieldValue(annotatedString = AnnotatedString(text = ""))
                            },
                        ) {
                            Text("Button")
                        }
                    }
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