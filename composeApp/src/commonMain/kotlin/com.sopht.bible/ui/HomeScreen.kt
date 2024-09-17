package com.sopht.bible.ui

import com.sopht.bible.Screens
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.sopht.bible.models.Verse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun HomeScreen(navController: NavController, vm: BibleViewModel, modifier: Modifier = Modifier) {
    Surface (color = MaterialTheme.colorScheme.surface) {
        Column (
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var message by remember { mutableStateOf("Sopht Bible") }
            var shouldShowDialog by remember { mutableStateOf(true) }
            var lastMeal by remember { mutableStateOf( Verse(1,20,"John",3,16,"NIV", 2, "For God so loved the world that he gave his one and only Son, that whoever believes in him shall not perish but have eternal life."))}
            var randomVerse by remember { mutableStateOf( Verse(2,20,"John",3,16,"NIV", 3, "For God so loved the world that he gave his one and only Son, that whoever believes in him shall not perish but have eternal life."))}
            var lastBookmark by remember { mutableStateOf( Verse(3,20,"John",3,16,"NIV", 4, "For God so loved the world that he gave his one and only Son, that whoever believes in him shall not perish but have eternal life."))}
            val selectedText = remember{mutableStateOf("")}
            val scope = rememberCoroutineScope()
            var completeCount = 0

            if (shouldShowDialog) {
                Dialog(
                    onDismissRequest = {}
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        ),
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier
                            .fillMaxSize()
                            .defaultMinSize(minHeight = 100.dp),
                        onClick = { navController.navigate(Screens.Bible.route) }
                    ) {
                        Text(
                            text = message,
                            modifier = modifier.padding(16.dp),
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        CircularProgressIndicator(
                            modifier = modifier.width(64.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                }
            }
            message = "Initializing"
            vm.initializeDatabase { count ->
                completeCount += count
                if (completeCount >= 4) {
                    shouldShowDialog = false
                    scope.launch {
                        delay(2000)
//                        randomVerse = vm.getRandomVerse()
//                        lastMeal = vm.getLastMeal()
//                        lastBookmark = vm.getLastBookmarkedVerse() ?: lastMeal
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier
                    .fillMaxSize()
                    .defaultMinSize(minHeight = 100.dp),
//                onClick = { navController.navigate(Screens.Bible.route) }
            ) {
                Text(
                    text = "Welcome!\nReady for ${getFoodType()}?",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = modifier.padding(16.dp),
                    textAlign = TextAlign.Left
                )
                SelectionContainer (
                    modifier = modifier
                ) {
                    Text(
                        text = "Last meal\n$lastMeal",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = modifier.padding(16.dp),
                        textAlign = TextAlign.Left
                    )
                    Text (text = selectedText.value, modifier = modifier)
                }
                TextButton(
                    onClick = {navController.navigate(Screens.Bible.route)},
                    modifier = modifier.align(Alignment.End)
                ) {
                    Text(
                        text = if (true) "Feed" else "Continue feeding",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Left
                    )
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Forward Arrow")
                }
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier
                    .fillMaxSize()
                    .defaultMinSize(minHeight = 100.dp),
//                onClick = { navController.navigate(Screens.Bible.route) }
            ) {
                TextWithSelectedText(
                    selectedText = selectedText
                )
                Text(
                    text = "Random Verse $selectedText",
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "$randomVerse",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = modifier.padding(16.dp),
                    textAlign = TextAlign.Left
                )
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier
                    .fillMaxSize()
                    .defaultMinSize(minHeight = 100.dp),
                onClick = { navController.navigate(Screens.Bible.route) }
            ) {
                Text(
                    text = "Last Bookmark",
                    modifier = modifier.padding(16.dp),
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "$lastBookmark",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = modifier.padding(16.dp),
                    textAlign = TextAlign.Left
                )
            }
        }
    }
}

@Composable fun TextWithSelectedText(
    text:String = "This is a plain text",
    selectedText: MutableState<String>
){
    var textInput by remember{ mutableStateOf(TextFieldValue(text))}
    TextField(
        value = textInput,
        onValueChange = { newValue ->
            textInput = newValue
            selectedText.value = textInput.getSelectedText().text
        } ,
        readOnly =  true ,
    )
}

fun getFoodType(): String {
    val now = Clock.System.now()
    val currentTime = now.toLocalDateTime(TimeZone.currentSystemDefault()).time
    val foodType = when(currentTime.hour) {
        in 0..11 -> "breakfast"
        in 12..15 -> "lunch"
        in 16..20 -> "dinner"
        in 21..23 -> "some late night snack"
        else -> "food"
    }
    return foodType
}