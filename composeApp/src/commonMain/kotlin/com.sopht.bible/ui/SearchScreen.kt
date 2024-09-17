package com.sopht.bible.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchScreen() {
    val textFieldValue = remember { mutableStateOf("") }
    val isVisible by remember {
        derivedStateOf {
            textFieldValue.value.isNotEmpty()
        }
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .imePadding(),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Input field where the user types the text
        TextField(
            value = TextFieldValue(
                text = textFieldValue.value,
                selection = TextRange(textFieldValue.value.length) // Move the cursor to the end of the textfield
            ),
            onValueChange = {
                textFieldValue.value = transformInput(it.text) // Transform and Update the text field value
            },
            trailingIcon = {
                // Check if text is visible in the OutlinedTextField
                if (isVisible) {
                    // Add an IconButton to clear all text from the OutlinedTextField
                    IconButton(
                        onClick = { textFieldValue.value = "" }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    }
                }
            },
            placeholder = { Text("Type a letter...", style = TextStyle(color = Color.Gray, fontSize = 18.sp)) }
        )
    }
}

// Transforms the input by shifting the last letter 5 places ahead in the alphabet and appending it to the input
fun transformInput(input: String): String {
    // Store the original input text in a StringBuilder
    val transformedText = StringBuilder(input)

    // Get the last character from the input
    val lastChar = input.last()

    // Check if the character is a letter from the alphabet
    if (lastChar.isLetter()) {
        // Transform the character nd append it to the original input text
        val transformedChar = performCharTransformation(lastChar)
        transformedText.append(transformedChar)
    }

    // Return the transformed text as a string, or return it untransformed if the last character is not a letter
    return transformedText.toString()
}

// Shifts the character 5 positions ahead in the alphabet, wrapping around if necessary
private fun performCharTransformation(char: Char): Char {
    return when (char) {
        in 'A'..'Z' -> 'A' + (char - 'A' + 5) % 26 // Handle uppercase letters
        in 'a'..'z' -> 'a' + (char - 'a' + 5) % 26 // Handle lowercase letters
        else -> char // Return the original character if it's not a letter
    }
}
