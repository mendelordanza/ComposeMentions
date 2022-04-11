package com.ralphordanza.compose_mentions

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.constraintlayout.compose.ConstraintLayout

val selectedMentions = mutableListOf<Map<String, Any>>()


/**
 * [data] list of suggestions
 * [onMarkupChanged] capture the formatted string during value change
 * [suggestionItemBuilder] displays the item of suggestion
 * [markupBuilder] formats the string to markdown value
 */
@Composable
fun ComposeMentions(
    modifier: Modifier = Modifier,
    dropdownMaxHeight: Dp = 400.dp,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape =
        MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize),
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    trigger: String,
    data: List<Map<String, Any>>,
    onMarkupChanged: (String) -> Unit,
    suggestionItemBuilder: @Composable (Map<String, Any>) -> Unit,
    markupBuilder: (trigger: String, id: String, display: String) -> String,
) {
    var queriedList by remember { mutableStateOf(data) }
    var selectedMention by remember { mutableStateOf(TextFieldValue(text = "")) }
    var message by remember { mutableStateOf(TextFieldValue(annotatedString = AnnotatedString(text = ""))) }
    var showSuggestions by remember { mutableStateOf(false) }

    DisposableEffect(key1 = true) {
        onDispose {
            if(message.text.isNotEmpty()) {
                message = TextFieldValue(annotatedString = AnnotatedString(text = ""))
            }
        }
    }

    Column(modifier = modifier.height(IntrinsicSize.Min)) {
        TextField(
            modifier = modifier,
            value = message,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors,
            onValueChange = { textValueChange ->
                val value = fetchMentions(
                    textValueChange, trigger = trigger, data = data,
                    showSuggestions = { show ->
                        showSuggestions = show
                    },
                    onQueryChanged = { filtered ->
                        queriedList = filtered
                    },
                )

                selectedMention = value

                message = textValueChange

                var finalString = message.text
                selectedMentions.forEach { mention ->
                    val mentionedMember = "$trigger${mention["display"]}"
                    //CHANGE COLOR
                    val annotatedString = buildAnnotatedString {
                        append(message.annotatedString)
                        addStyle(
                            style = SpanStyle(
                                color = Color.Blue,
                            ),
                            start = message.annotatedString.indexOf(mentionedMember),
                            end = message.annotatedString.indexOf(mentionedMember) + mentionedMember.length
                        )
                    }

                    //UPDATE THE TEXT
                    message =
                        message.copy(
                            annotatedString = annotatedString,
                        )

                    //FORMAT FOR MARKDOWN
                    finalString = finalString.replace(
                        mentionedMember,
                        markupBuilder(
                            trigger,
                            mention["id"].toString(),
                            mention["display"].toString(),
                        ),
                    )
                }
                onMarkupChanged(finalString)

                if (textValueChange.text.isEmpty()) {
                    //RESET
                    selectedMention = TextFieldValue(text = "")
                }
            },
        )
        DropdownMenu(
            expanded = showSuggestions,
            onDismissRequest = { showSuggestions = false },
            modifier = Modifier
                .fillMaxWidth()
                .requiredSizeIn(maxHeight = dropdownMaxHeight),
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
        ) {
            queriedList.forEachIndexed { index, s ->
                val item = queriedList[index]
                DropdownMenuItem(onClick = {
                    showSuggestions = false
                }) {
                    Box(
                        modifier = Modifier.clickable {
                            //ADD THE SELECTED MENTION TO THE LIST
                            selectedMentions.add(
                                mapOf(
                                    "id" to "${item["id"]}",
                                    "display" to "${item["display"]}",
                                )
                            )

                            //REPLACE THE CURRENT TEXT WITH THE SELECTED MENTION
                            message = message.copy(
                                text = message.text.replaceRange(
                                    startIndex = selectedMention.selection.start,
                                    endIndex = selectedMention.selection.end,
                                    "$trigger${item["display"]} ",
                                )
                            )

                            var finalString = message.text
                            selectedMentions.forEach { mention ->
                                val mentionedMember = "$trigger${mention["display"]}"
                                //ADD COLOR TO MENTIONS
                                val annotatedString = buildAnnotatedString {
                                    append(message.annotatedString)
                                    addStyle(
                                        style = SpanStyle(
                                            color = Color.Blue,
                                        ),
                                        start = message.annotatedString.indexOf(
                                            mentionedMember),
                                        end = message.annotatedString.indexOf(
                                            mentionedMember) + mentionedMember.length
                                    )
                                }

                                //UPDATE THE TEXT
                                message =
                                    message.copy(
                                        annotatedString = annotatedString,
                                        selection = TextRange(
                                            selectedMention.selection.start + 1 + "$mentionedMember ".length,
                                        )
                                    )

                                //FORMAT FOR MARKDOWN
                                finalString = finalString.replace(
                                    mentionedMember,
                                    markupBuilder(
                                        trigger,
                                        mention["id"].toString(),
                                        mention["display"].toString(),
                                    ),
                                )
                            }
                            onMarkupChanged(finalString)

                            //RESET
                            selectedMention = TextFieldValue(text = "")

                            showSuggestions = false
                        },
                    ) {
                        suggestionItemBuilder(item)
                    }
                }
            }
        }
    }
}

/**
 * [onQueryChanged] returns a filtered list based on the search
 * [showSuggestions] sets the value to show the suggestions
 */
fun fetchMentions(
    input: TextFieldValue,
    trigger: String,
    data: List<Map<String, Any>>,
    onQueryChanged: (List<Map<String, Any>>) -> Unit,
    showSuggestions: (Boolean) -> Unit,
): TextFieldValue {
    val cursorPos = input.selection.start

    if (cursorPos >= 0) {
        var pos = 0

        val lengthMap = mutableListOf<LengthMap>()

        input.text.split("\\s+".toRegex()).forEach {
            lengthMap.add(
                LengthMap(
                    start = pos,
                    end = pos + it.length,
                    str = it
                )
            )

            pos += it.length + 1
        }

        val value = lengthMap.filter {
            it.end == cursorPos && it.str.lowercase().contains(Regex(trigger))
        }.size - 1

        showSuggestions(value != -1)

        lengthMap.forEach { map ->
            if (map.str.contains(trigger)) {
                //QUERY THE MEMBER LIST
                val filteredList = data.filter {
                    val name = it["display"].toString().lowercase().replace(" ", "")
                    name.contains(map.str.removePrefix(trigger).lowercase().replace(" ", ""))
                }
                onQueryChanged(filteredList)
            }
        }

        val wordsCount = lengthMap.size - 1

        if (lengthMap.isNotEmpty() && wordsCount != -1) {
            return TextFieldValue(
                text = input.text,
                selection = TextRange(
                    lengthMap[wordsCount].start,
                    lengthMap[wordsCount].end,
                )
            )
        }
    }

    return input
}

data class LengthMap(
    val start: Int,
    val end: Int,
    val str: String,
)