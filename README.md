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
  Member(id = 1, name = "John Doe"),
  Member(id = 2, name = "Spider Man"),
  Member(id = 3, name = "Peter Parker")
)
var value by remember {
  mutableStateOf("")
}

ComposeMentions(
  placeholder = {
      Text("Enter your thoughts")
  },
  trigger = "@",
  data = members.map {
      mapOf(
          "id" to it.id,
          "display" to "${it.name}",
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
  },
  markupBuilder = { trigger, id, display ->
    // format your own markdown
    // eg. "[$trigger$display](profile/$id)"
  },
)
```
