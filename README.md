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

```kotlin
val members = listOf(
  Member(id = 1, display = "John Doe"),
  Member(id = 2, display = "Spider Man"),
  Member(id = 3, display = "Peter Parker")
)
var value by remember {
  mutableStateOf(TextFieldValue(""))
}

ComposeMentions(
  placeholder = {
      Text("Enter your thoughts")
  },
  trigger = "@",
  data = members.map {
      mapOf(
          "id" to it.id,
          "display" to it.display,
          // you can add more fields here
      )
  },
  onMarkupChanged = {
    // Do something with markdown
  },
  message = value,
  onValueChanged = {
      value = it
  },
  suggestionItemBuilder = {
      // Customize composable item
      Column {
        Text("${it["id"]}")
        Text("${it["display"]}")
      }
  },
  markupBuilder = { trigger, id, display ->
    // format your own markdown
    // eg. "[$trigger$display](profile/$id)"
  },
)
```
