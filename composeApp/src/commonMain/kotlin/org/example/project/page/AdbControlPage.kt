package org.example.project.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.pickFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.component.ColorDivider
import org.example.project.component.ColorText
import org.example.project.component.ColorTheme
import org.example.project.component.DimenDivider
import org.example.project.component.RoundedCorner
import org.example.project.adb.ADB_CLEAR_DATA
import org.example.project.adb.ADB_DEVICE_BRAND
import org.example.project.adb.ADB_DEVICE_NAME
import org.example.project.adb.ADB_DUMP_SHOW_TOP_ACTIVITY
import org.example.project.adb.ADB_INSTALL
import org.example.project.adb.ADB_KILL_APP
import org.example.project.adb.ADB_PRINT_PATH
import org.example.project.adb.ADB_REBOOT
import org.example.project.adb.ADB_REMOUNT
import org.example.project.adb.ADB_ROOT
import org.example.project.adb.ADB_SAVE_SCREEN_RECORD
import org.example.project.adb.ADB_SAVE_SCREEN_SHOT
import org.example.project.adb.ADB_SCREEN_SHOT
import org.example.project.adb.ADB_SCREEN_START_RECORD
import org.example.project.adb.ADB_SCREEN_FIND_RECORD_PID
import org.example.project.adb.ADB_SCREEN_STOP_RECORD
import org.example.project.adb.ADB_START_APP
import org.example.project.adb.ADB_UNINSTALL
import org.example.project.adb.AdbExecuteCallback
import org.example.project.adb.AdbExecutor
import org.example.project.adb.DIR_PATH_HOLDER
import org.example.project.adb.FILE_PATH_HOLDER
import org.example.project.adb.PACKAGE_NAME_HOLDER
import org.example.project.formatTime
import org.example.project.getSystemCurrentTimeMillis
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
@Composable
@Preview
fun AdbControlPage(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {

    var outputText by remember { mutableStateOf("") }

    var packageNameInputHint by remember { mutableStateOf(false) }
    var packageName by remember { mutableStateOf("com.hihonor.redteamobile.roaming") }

    var deviceName by remember { mutableStateOf("No Connected Device") }
    var deviceBrand by remember { mutableStateOf("") }

    var isRecording by remember { mutableStateOf(false) }
    var startRecordingTime by remember { mutableStateOf(0L) }
    var readableRecordingDuration by remember { mutableStateOf("") }

    fun executeADB(adbCommand: String) {
        when (adbCommand) {
            ADB_DEVICE_NAME -> {
                AdbExecutor.findConnectedDeviceName {
                    deviceName = if (it.contains("no devices")) {
                        "No Connected Device"
                    } else {
                        it
                    }
                }
            }

            ADB_DEVICE_BRAND -> {
                AdbExecutor.findConnectedDeviceBrand {
                    deviceBrand = if (it.contains("no devices")) {
                        ""
                    } else {
                        it
                    }
                }
            }

            ADB_SCREEN_SHOT -> {
                AdbExecutor.screenshot(object : AdbExecuteCallback {
                    override fun onPrint(line: String) {
                        outputText = appendOutput(outputText, line)
                    }

                    override fun onExit(exitCode: Int) {
                        if (exitCode == 0) {
                            outputText = appendOutput(outputText, "capture screenshot successful")
                        }
                    }
                })
            }

            ADB_SCREEN_START_RECORD -> {
                isRecording = true
                startRecordingTime = getSystemCurrentTimeMillis()
                readableRecordingDuration = "00:00"
                CoroutineScope(Dispatchers.Default).launch {
                    while (isRecording) {
                        delay(1000L)
                        readableRecordingDuration =
                            formatTime(getSystemCurrentTimeMillis() - startRecordingTime)
                    }
                }
                AdbExecutor.startRecord(object : AdbExecuteCallback {
                    override fun onPrint(line: String) {
                        outputText = appendOutput(outputText, line)
                    }

                    override fun onExit(exitCode: Int) {
                        if (adbCommand == ADB_SCREEN_FIND_RECORD_PID || adbCommand == ADB_SCREEN_START_RECORD) {
                            isRecording = false
                        }
                    }
                })
            }

            ADB_SCREEN_STOP_RECORD -> {
                AdbExecutor.stopRecord(object : AdbExecuteCallback {
                    override fun onPrint(line: String) {
                        outputText = appendOutput(outputText, line)
                    }

                    override fun onExit(exitCode: Int) {
                        isRecording = false
                    }
                })
            }

            else -> {
                AdbExecutor.exec(adbCommand, object : AdbExecuteCallback {
                    override fun onPrint(line: String) {
                        outputText = appendOutput(outputText, line)
                    }

                    override fun onExit(exitCode: Int) {
                        if (exitCode != 0) {
                            outputText = appendOutput(outputText, "exit code [$exitCode]")
                        }
                    }
                })
            }
        }
    }

    DisposableEffect(key1 = lifecycleOwner) {
        // 进入组件时执行，lifecycleOwner 改变后重新执行（先回调 onDispose）
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                CoroutineScope(Dispatchers.Default).launch {
                    while(true){
                        executeADB(ADB_DEVICE_BRAND)
                        executeADB(ADB_DEVICE_NAME)
                        delay(5000)
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Android Command Executor",
                fontSize = 30.sp,
                fontWeight = FontWeight(700),
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
            )
            Row {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth().padding(2.dp, 0.dp, 0.dp, 10.dp),
                        textAlign = TextAlign.Start,
                        text = "$deviceBrand $deviceName"
                    )
                    BasicTextField(
                        outputText,
                        onValueChange = {
                        },
                        modifier = Modifier.fillMaxWidth().fillMaxHeight()
                            .border(
                                DimenDivider,
                                color = ColorDivider,
                                shape = RoundedCornerShape(RoundedCorner)
                            )
                            .background(
                                Color.White,
                                RoundedCornerShape(RoundedCorner)
                            ).padding(10.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .width(280.dp)
                        .padding(start = 20.dp)
                ) {

                    val onButtonClick = { adbCommand: String ->
                        // require package name
                        if (adbCommand.contains(PACKAGE_NAME_HOLDER)) {
                            var cmd: String = adbCommand
                            if (packageName.isNotEmpty()) {
                                cmd = cmd.replace(PACKAGE_NAME_HOLDER, packageName)
                            } else {
                                packageNameInputHint = true
                            }
                            // execute
                            executeADB(cmd)
                        } else if (adbCommand.contains(FILE_PATH_HOLDER)) {
                            // require pick a file
                            CoroutineScope(Dispatchers.Default).launch {
                                val file = FileKit.pickFile(title = "Pick File")
                                println(file?.path)
                                file?.let {
                                    if (!file.path.equals("null")) {
                                        var cmd: String = adbCommand
                                        if (file.path?.isNotEmpty() == true) {
                                            cmd = cmd.replace(FILE_PATH_HOLDER, file.path!!)
                                            // execute
                                            executeADB(cmd)
                                        }
                                    }
                                }
                            }
                        } else if (adbCommand.contains(DIR_PATH_HOLDER)) {
                            // require pick a directory
                            CoroutineScope(Dispatchers.Default).launch {
                                val file = FileKit.pickDirectory(title = "Pick Directory")
                                println(file?.path)
                                file?.let {
                                    if (!file.path.equals("null")) {
                                        var cmd: String = adbCommand
                                        if (file.path?.isNotEmpty() == true) {
                                            cmd = cmd.replace(DIR_PATH_HOLDER, file.path!!)
                                            // execute
                                            executeADB(cmd)
                                        }
                                    }
                                }
                            }
                        } else {
                            // execute directly
                            executeADB(adbCommand)
                        }
                    }

                    Column {
                        Text(
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth().padding(0.dp, 5.dp, 0.dp, 5.dp),
                            textAlign = TextAlign.Start,
                            text = "Target Package Name :",
                            color = if (packageNameInputHint) {
                                ColorTheme
                            } else {
                                ColorText
                            }
                        )
                        BasicTextField(
                            packageName,
                            onValueChange = {
                                packageNameInputHint = false
                                packageName = it
                            },
                            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                                .border(
                                    DimenDivider,
                                    color = ColorDivider,
                                    shape = RoundedCornerShape(RoundedCorner)
                                )
                                .background(
                                    Color.White,
                                    RoundedCornerShape(RoundedCorner)
                                ).padding(10.dp)
                        )
                    }
                    AppPanel(onButtonClick)
                    DevicePanel(onButtonClick)
                    ScreenPanel(
                        isRecording,
                        readableRecordingDuration,
                        onButtonClick
                    )
                }
            }
        }
    }
}

@Composable
fun AppPanel(onButtonClick: (String) -> Any) {
    Column {
        Text(
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 5.dp),
            textAlign = TextAlign.Start,
            text = "App"
        )
        Column {
            Row {
                AdbExecuteButton("Kill") {
                    onButtonClick.invoke(ADB_KILL_APP)
                }
                AdbExecuteButton("Start") {
                    onButtonClick.invoke(ADB_START_APP)
                }
                AdbExecuteButton("Restart") {
                    onButtonClick.invoke(ADB_KILL_APP)
                    onButtonClick.invoke(ADB_START_APP)
                }
            }
            Row {
                AdbExecuteButton("Clear Data") {
                    onButtonClick.invoke(ADB_CLEAR_DATA)
                }
                AdbExecuteButton("Print Path") {
                    onButtonClick.invoke(ADB_PRINT_PATH)
                }
            }
            Row {
                AdbExecuteButton("Install") {
                    onButtonClick.invoke(ADB_INSTALL)
                }
                AdbExecuteButton("Uninstall") {
                    onButtonClick.invoke(ADB_UNINSTALL)
                }
            }
            Row {
                AdbExecuteButton("Top Activity") {
                    onButtonClick.invoke(ADB_DUMP_SHOW_TOP_ACTIVITY)
                }
            }
        }
    }
}

@Composable
fun DevicePanel(onButtonClick: (String) -> Any) {
    Column {
        Text(
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 5.dp),
            textAlign = TextAlign.Start,
            text = "Device"
        )
        Row {
            AdbExecuteButton("Root") {
                onButtonClick.invoke(ADB_ROOT)
            }
            AdbExecuteButton("Remount") {
                onButtonClick.invoke(ADB_REMOUNT)
            }
            AdbExecuteButton("Reboot") {
                onButtonClick.invoke(ADB_REBOOT)
            }
        }
    }
}

@Composable
fun ScreenPanel(
    isRecording: Boolean,
    readableRecordingDuration: String,
    onButtonClick: (String) -> Any
) {

    Column {
        Row {
            Text(
                fontSize = 12.sp,
                modifier = Modifier.wrapContentWidth().padding(0.dp, 0.dp, 0.dp, 5.dp),
                textAlign = TextAlign.Start,
                text = "ScreenShot/Record"
            )
            if (isRecording) {
                Text(
                    color = ColorTheme,
                    text = "Recording $readableRecordingDuration",
                    fontSize = 12.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f).padding(0.dp, 0.dp, 0.dp, 5.dp),
                )
            }
        }
        Row {
            AdbExecuteButton("ScreenShot") {
                onButtonClick.invoke(ADB_SCREEN_SHOT)
            }
            AdbExecuteButton("Save") {
                onButtonClick.invoke(ADB_SAVE_SCREEN_SHOT)
            }
        }
        Row {
            AdbExecuteButton("Start Record") {
                onButtonClick.invoke(ADB_SCREEN_START_RECORD)
            }
            AdbExecuteButton("Stop Record") {
                onButtonClick.invoke(ADB_SCREEN_STOP_RECORD)
            }
            AdbExecuteButton("Save") {
                onButtonClick.invoke(ADB_SAVE_SCREEN_RECORD)
            }
        }
    }
}

@Composable
fun AdbExecuteButton(text: String, onClick: () -> Unit) {
    Text(
        modifier = Modifier.padding(end = 5.dp, bottom = 5.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                onClick.invoke()
            }.background(Color.White, RoundedCornerShape(4.dp))
            .border(
                width = 1.dp,
                color = ColorTheme,
                shape = RoundedCornerShape(4.dp)
            ).padding(horizontal = 5.dp, vertical = 5.dp),
        text = text,
        lineHeight = 12.sp,
        color = ColorTheme,
        fontWeight = FontWeight(500),
        fontStyle = FontStyle.Normal,
        fontSize = 12.sp,
    )
}

fun appendOutput(oldText: String, text: String): String {
    val appendText = if (oldText.isNotEmpty()) {
        "$oldText\n$text"
    } else {
        text
    }
    return appendText
}
