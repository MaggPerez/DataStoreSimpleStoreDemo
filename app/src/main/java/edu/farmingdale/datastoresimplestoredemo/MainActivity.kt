package edu.farmingdale.datastoresimplestoredemo
import android.content.Context
import java.io.PrintWriter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.farmingdale.datastoresimplestoredemo.data.AppPreferences
import edu.farmingdale.datastoresimplestoredemo.ui.theme.DataStoreSimpleStoreDemoTheme
import kotlinx.coroutines.launch
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DataStoreSimpleStoreDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DataStoreDemo(modifier = Modifier.padding(innerPadding))
                }
            }
        }
        writeToInternalFile()
        val fileContents = readFromInternalFile()
        Log.d("MainActivity", fileContents)
    }
    private fun writeToInternalFile() {
        val outputStream: FileOutputStream = openFileOutput("edufilename", Context.MODE_PRIVATE)
        val writer = PrintWriter(outputStream)

        // Write three lines
        writer.println("This world of fun")
        writer.println("and yet, and yet.")

        writer.close()
    }

    private fun readFromInternalFile(): String {
        val inputStream = openFileInput("edufilename")
        val reader = inputStream.bufferedReader()
        val stringBuilder = StringBuilder()

        // Append each line and newline character to stringBuilder
        reader.forEachLine {
            stringBuilder.append(it).append("\n CSC 371 \n").append(System.lineSeparator())
        }

        return stringBuilder.toString()
    }
}

@Composable
fun DataStoreDemo(modifier: Modifier) {
    val store = AppStorage(LocalContext.current)
    val appPrefs = store.appPreferenceFlow.collectAsState(AppPreferences())
    val coroutineScope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    Column (modifier = Modifier.padding(50.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Values = ${appPrefs.value.userName}, " +
                "${appPrefs.value.highScore}, ${appPrefs.value.darkMode}")
        Button(onClick = {
            coroutineScope.launch {
                store.saveUsername("somevaluehere")
            }

        }) {
            Text("Save Values")
        }

        HorizontalDivider()

        // ToDo 1: Modify the App to store a high score and a dark mode preference
        //stores a high score and dark mode preference
        Button(
            onClick = {
                coroutineScope.launch {
                    store.saveHighScore(10)
                    store.saveDarkModePreference(true)
                }
            }
        ) {
            Text("Save HighScore and save Dark Mode Preference")
        }

        HorizontalDivider()

        // ToDo 2: Modify the APP to store the username through a text field
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Enter username")}
        )



        // ToDo 3: Modify the App to save the username when the button is clicked
        //button to save username from textfield
        Button(
            onClick = {
                coroutineScope.launch {
                    store.saveUsername(username)
                }
            }
        ) {
            Text("Click to save username")
        }

        HorizontalDivider()




    }
}

@Preview (showBackground = true)
@Composable
fun DataStorePreview() {
    DataStoreDemo(modifier = Modifier)
}





// ToDo 4: Modify the App to display the values stored in the DataStore


