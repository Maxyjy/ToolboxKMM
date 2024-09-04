package org.example.project.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.project.component.ColorDivider
import org.example.project.component.DimenDivider
import org.example.project.constant.ADB_DEVICE_LIST
import org.example.project.constant.ADB_REBOOT
import org.example.project.executeADB
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
@Composable
@Preview
fun AdbControlPage() {
    var outputText by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            BasicTextField(
                outputText,
                onValueChange = {
                },
                modifier = Modifier.fillMaxWidth().height(400.dp)
                    .border(
                        DimenDivider,
                        color = ColorDivider,
                        shape = RoundedCornerShape(2)
                    )
                    .background(
                        Color.White,
                        RoundedCornerShape(2)
                    ).padding(10.dp)
            )
            AdbExecuteButton("Reboot") {
                CoroutineScope(Dispatchers.Default).launch {
                    executeADB(ADB_REBOOT) { line ->
                        outputText = appendOutput(outputText, line)
                    }
                }
            }
            AdbExecuteButton("Attached Device") {
                CoroutineScope(Dispatchers.Default).launch {
                    executeADB(ADB_DEVICE_LIST) { line ->
                        outputText = appendOutput(outputText, line)
                    }
                }
            }
        }
    }
}

fun appendOutput(oldText: String, text: String): String {
    val appendText = if (oldText.isNotEmpty()) {
        "$oldText\n$text"
    } else {
        text
    }
    return appendText
}

@Composable
fun AdbExecuteButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) { Text(text) }
}