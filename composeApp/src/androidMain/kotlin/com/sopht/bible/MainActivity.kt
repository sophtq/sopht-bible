package com.sopht.bible

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.sopht.bible.cache.DatabaseDriverFactory
import styles.SophtBibleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainApp()
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_XL)
@Composable
fun AppAndroidPreview(modifier: Modifier = Modifier) {
    SophtBibleTheme {
        Column  {
            Text("SB", modifier.align(Alignment.CenterHorizontally))
            Text("Sopht Bible", modifier.align(Alignment.CenterHorizontally))
            Text("Living & Powerful")
        }
    }
}