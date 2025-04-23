package com.sopht.bible.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sopht.bible.models.Version
import kotlinx.coroutines.launch

@Composable
fun VersionsScreen(viewModel: BibleViewModel) {
    var shouldShowOnboarding by remember { mutableStateOf(true) }
    var numberOfBibles by remember { mutableStateOf(0) }
    var downloadMessage by remember { mutableStateOf("KJV") }
    var versions = viewModel.getVersions()
    val scope = rememberCoroutineScope()

//    scope.launch { versions = viewModel.getVersions() }
//    print(versions[0])
    LazyColumn(modifier = Modifier.fillMaxWidth().height(100.dp), contentPadding = PaddingValues(16.dp)) {
        items(versions, key = { it.id }) { version ->
            var downloadProgress: Float? by mutableStateOf(null)
            ColumnItem(viewModel, version)
        }
    }
}

@Composable
fun ColumnItem(
    viewModel: BibleViewModel,
    version: Version
) {
    var downloadProgress: Float? by remember { mutableStateOf(null) }
    Card(
        modifier = Modifier.padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.width(IntrinsicSize.Max)
                    .padding(4.dp)
                    .weight(1f)
            ) {
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = version.acronym,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = version.name,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Box(
                modifier = Modifier.padding(4.dp)
            ) {
                if (downloadProgress == null || downloadProgress!! < 0) {
                    IconButton(
                        modifier = Modifier.align(Alignment.Center),
                        onClick = {
                            viewModel.downloadVersion(version, {message: String, progress: Float ->
                                downloadProgress = progress
                            }, onComplete = {
                                downloadProgress = null
                                version.isDownloaded = true
                            })
                        }
                    ) {
                        Icon(
                            imageVector = if (version.isDownloaded) Icons.Filled.Check else  Icons.Filled.Download,
                            contentDescription = "Download or Downloaded"
                        )
                    }
                }
                downloadProgress?.let {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        progress = {
                            it
                        },
                    )
                }
            }
        }
    }
}
