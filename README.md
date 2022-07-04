# ComposeMentions

Customizable Jetpack Compose mentions

# Installation
```
dependencies {
  implementation "com.github.mendelordanza:ComposeMentions:<version>"
}
```

# Usage
Use it as a replacement for TextField

`id` and `display` is required

```
ComposeMentions(
                modifier = Modifier
                    .background(color = Color.White),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    disabledLabelColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text("Enter your thoughts", style = MaterialTheme.typography.body1)
                },
                trigger = "@",
                data = members.map {
                    mapOf(
                        "id" to it.id,
                        "display" to "${it.name}",
                        "member" to it
                    )
                },
                onMarkupChanged = {
                    checkInViewModel.onOpenEndedTextMarkdownChanged(it)
                },
                message = checkInViewModel.openEndedText,
                onValueChanged = {
                    checkInViewModel.onOpenEndedTextChanged(it)
                },
                suggestionItemBuilder = {
                    val member = (it["member"] as Member)
                    ProfileInfo(
                        modifier = Modifier.fillMaxWidth(),
                        image = member.avatar ?: "",
                        imageHeight = 40.dp,
                        imageWidth = 40.dp,
                        title = {
                            Text(member.name ?: "")
                        },
                        subtitle = {
                            Text(member.role ?: "")
                        }
                    )
                },
                markupBuilder = { trigger, id, display ->
                    "[$trigger$display](profile/$id)"
                },
            )
```
