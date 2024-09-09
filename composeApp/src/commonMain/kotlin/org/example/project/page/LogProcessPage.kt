package org.example.project.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import org.example.project.component.RButton
import org.example.project.util.LogProcessor
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
@Composable
@Preview
fun LogProcessPage() {
    var path by remember { mutableStateOf("") }
    var processInfo by remember { mutableStateOf("") }
    val launcher = rememberDirectoryPickerLauncher(
        title = "Pick Android Log directory",
    ) { directory ->
        println(directory?.path)
        path = directory?.path.toString()
    }

    Column {
        Text(
            "Log Merge & Filter",
            fontSize = 30.sp,
            fontWeight = FontWeight(700),
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
        )
        Text("Android Logs Directory : $path")
        RButton(
            onClick = {
                launcher.launch()
            }, "Pick Android Logs Directory"
        )
        Text(processInfo)
        RButton(
            onClick = {
                if (path.isEmpty()) {
                    return@RButton
                }
                val result =
                    LogProcessor.process(path, object : LogProcessor.Companion.LogProcessListener {
                        override fun onStep(info: String) {
                            processInfo = "$processInfo\n$info"
                        }

                        override fun onResult(isSuccess: Boolean) {
                            if (isSuccess) {
                                processInfo = "$processInfo\nLog merge&filter process success"
                            } else {
                                processInfo = "$processInfo\nLog merge&filter process failed"
                            }
                        }
                    })
                println("log process result [$result]")
            },
            "Process",
            enable = path.isNotEmpty() && path != "null"
        )
    }

}