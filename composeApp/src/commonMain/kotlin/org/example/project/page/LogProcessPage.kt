package org.example.project.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.vinceglb.filekit.compose.PickerResultLauncher
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import org.example.project.component.ColorDivider
import org.example.project.component.ColorGray
import org.example.project.component.ColorTheme
import org.example.project.component.ColorThemeProgressBar
import org.example.project.component.DimenDivider
import org.example.project.component.RButton
import org.example.project.component.RoundedCorner
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
    var currentProgress by remember { mutableStateOf(0f) }
    var readableProgress by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val launcher = rememberDirectoryPickerLauncher(
        title = "Pick Android Log directory",
    ) { directory ->
        println(directory?.path)
        path = directory?.path.toString()

        processInfo = ""
        currentProgress = 0.0f
        readableProgress = ""
        loading = false
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Log Merge & Filter",
            fontSize = 30.sp,
            fontWeight = FontWeight(700),
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
        )
        Text(
            text = "Step 1 : Pick Android Logs Directory :",
            fontSize = 14.sp,
            modifier = Modifier.padding(2.dp, 0.dp, 0.dp, 10.dp),
            textAlign = TextAlign.Start,
        )
        BasicTextField(
            value = if (path == "null") {
                ""
            } else {
                path
            },
            onValueChange = {},
            modifier = Modifier.fillMaxWidth()
                .height(130.dp)
                .padding(0.dp, 0.dp, 0.dp, 10.dp)
                .border(
                    DimenDivider,
                    color = ColorDivider,
                    shape = RoundedCornerShape(RoundedCorner)
                ).background(
                    Color.White,
                    RoundedCornerShape(RoundedCorner)
                ).padding(start = 15.dp, top = 10.dp, end = 15.dp, bottom = 10.dp)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Column(modifier = Modifier.wrapContentHeight(), horizontalAlignment = Alignment.End) {
                Text(
                    text = "It could be a little slow when first open file explorer...",
                    color = Color(0xffc1c1c1),
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(bottom = 3.dp)
                )

                RButton(
                    onClick = {
                        launcher.launch()
                    }, "Open Directory Picker"
                )
            }
        }

        Row(
            modifier = Modifier.padding(2.dp, 20.dp, 0.dp, 10.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Step 2 : Start Processing :",
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.padding(end = 10.dp),
                    text = readableProgress,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                )
                LinearProgressIndicator(
                    progress = currentProgress,
                    modifier = Modifier.fillMaxWidth(0.5f).padding(top = 5.dp, end = 10.dp),
                    color = ColorTheme,
                    backgroundColor = ColorThemeProgressBar,
                    strokeCap = StrokeCap.Square
                )
            }
        }

        BasicTextField(
            value = if (processInfo == "null") {
                ""
            } else {
                processInfo
            },
            onValueChange = {

            },
            modifier = Modifier.fillMaxWidth()
                .height(200.dp)
                .padding(0.dp, 0.dp, 0.dp, 10.dp)
                .border(
                    DimenDivider,
                    color = ColorDivider,
                    shape = RoundedCornerShape(RoundedCorner)
                ).background(
                    Color.White,
                    RoundedCornerShape(RoundedCorner)
                ).padding(start = 15.dp, top = 10.dp, end = 15.dp, bottom = 10.dp),
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            RButton(
                onClick = {
                    if (path.isEmpty()) {
                        return@RButton
                    }
                    loading = true
                    val result =
                        LogProcessor.process(
                            path,
                            object : LogProcessor.Companion.LogProcessListener {
                                override fun onStep(progress: Float, info: String) {
                                    currentProgress = progress
                                    readableProgress =
                                        (currentProgress * 100).toInt().toString() + "%"
                                    processInfo = if (processInfo.isNotEmpty()) {
                                        "$processInfo\n$info"
                                    } else {
                                        info
                                    }
                                }

                                override fun onResult(isSuccess: Boolean) {
                                    loading = false
                                    if (isSuccess) {
                                        processInfo =
                                            "$processInfo\nLog Merge & filter process success"
                                    } else {
                                        processInfo =
                                            "$processInfo\nLog Merge & filter process failed"
                                    }
                                }
                            })
                    println("log process result [$result]")
                },
                "Process",
                enable = !loading && path.isNotEmpty() && path != "null"
            )
        }
    }
}
