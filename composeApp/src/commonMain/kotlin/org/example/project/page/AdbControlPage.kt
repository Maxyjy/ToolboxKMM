package org.example.project.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
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
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.icon_delete
import kotlinproject.composeapp.generated.resources.icon_scrcpy
import kotlinproject.composeapp.generated.resources.icon_send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.ApplicationComponent
import org.example.project.component.ColorDivider
import org.example.project.component.ColorText
import org.example.project.component.ColorTheme
import org.example.project.component.DimenDivider
import org.example.project.component.RoundedCorner
import org.example.project.adb.ADB_CLEAR_DATA
import org.example.project.adb.ADB_DEVICE_BRAND
import org.example.project.adb.ADB_DEVICE_NAME
import org.example.project.adb.ADB_DUMP_SHOW_TOP_ACTIVITY
import org.example.project.adb.ADB_HONOR_DELETE_MCC_ENABLE_OVERSEA
import org.example.project.adb.ADB_HONOR_DELETE_MCC_LEVEL
import org.example.project.adb.ADB_HONOR_GET_MCC
import org.example.project.adb.ADB_HONOR_GET_MCC_ENABLE_OVERSEA
import org.example.project.adb.ADB_HONOR_GET_MCC_LEVEL
import org.example.project.adb.ADB_HONOR_MCC_BROAD_CAST_SEND
import org.example.project.adb.ADB_HONOR_PUT_MCC
import org.example.project.adb.ADB_HONOR_PUT_MCC_ENABLE_OVERSEA
import org.example.project.adb.ADB_HONOR_PUT_MCC_LEVEL
import org.example.project.adb.ADB_INSTALL
import org.example.project.adb.ADB_KILL_APP
import org.example.project.adb.ADB_OPEN_LANGUAGE_CHANGE_SETTING
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
import org.example.project.adb.MCC_HOLDER
import org.example.project.adb.PACKAGE_NAME_HOLDER
import org.example.project.adb.SCREEN_COPY
import org.example.project.adb.SPACE_HOLDER
import org.example.project.component.ColorGray
import org.example.project.component.PressedIndication
import org.example.project.executeADB
import org.example.project.formatTime
import org.example.project.getSystemCurrentTimeMillis
import org.example.project.util.AppPreferencesKey
import org.example.project.util.Base64Util
import org.jetbrains.compose.resources.painterResource
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

    var refreshFlag = false
    var outputText by remember { mutableStateOf("") }

    var packageNameInputHint by remember { mutableStateOf(false) }
    var packageName by remember { mutableStateOf("") }

    var deviceName by remember { mutableStateOf("No Connected Device") }
    var deviceBrand by remember { mutableStateOf("") }

    var isRecording by remember { mutableStateOf(false) }
    var startRecordingTime by remember { mutableStateOf(0L) }
    var readableRecordingDuration by remember { mutableStateOf("") }

    var honorCurrentMcc by remember { mutableStateOf("") }
    var honorCurrentMccLevel by remember { mutableStateOf("") }
    var honorCurrentMccOverseaEnable by remember { mutableStateOf(false) }

    fun execADB(adbCommand: String) {
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

            ADB_HONOR_GET_MCC -> {
                executeADB(ADB_HONOR_GET_MCC, object : AdbExecuteCallback {
                    override fun onPrint(line: String) {
                        honorCurrentMcc = if (line == "null" || line.contains("no devices")) {
                            ""
                        } else {
                            line
                        }
                        println("ADB_HONOR_GET_MCC $line")
                    }
                })
            }

            ADB_HONOR_GET_MCC_LEVEL -> {
                executeADB(ADB_HONOR_GET_MCC_LEVEL, object : AdbExecuteCallback {
                    override fun onPrint(line: String) {
                        honorCurrentMccLevel = if (line == "null" || line.contains("no devices")) {
                            ""
                        } else {
                            line
                        }
                        println("ADB_HONOR_GET_MCC_LEVEL $line")
                    }
                })
            }

            ADB_HONOR_GET_MCC_ENABLE_OVERSEA -> {
                executeADB(ADB_HONOR_GET_MCC_ENABLE_OVERSEA, object : AdbExecuteCallback {
                    override fun onPrint(line: String) {
                        honorCurrentMccOverseaEnable = line == "1"
                        println("ADB_HONOR_GET_MCC_ENABLE_OVERSEA $line")
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
                        if (adbCommand.contains("redtea_mcc")) {
                            execADB(ADB_HONOR_GET_MCC)
                            execADB(ADB_HONOR_GET_MCC_LEVEL)
                            execADB(ADB_HONOR_GET_MCC_ENABLE_OVERSEA)
                        }
                    }
                })
            }
        }
    }

    DisposableEffect(key1 = lifecycleOwner) {
        // 进入组件时执行，lifecycleOwner 改变后重新执行（先回调 onDispose）
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                refreshFlag = true
            }
            if (event == Lifecycle.Event.ON_PAUSE) {
                refreshFlag = false
            }
            if (event == Lifecycle.Event.ON_START) {
                CoroutineScope(Dispatchers.Default).launch {
                    val targetPackageName =
                        ApplicationComponent.coreComponent.appPreferences.getString(
                            AppPreferencesKey.TARGET_PACKAGE_NAME
                        )
                    if (!targetPackageName.isNullOrEmpty()) {
                        packageName = targetPackageName
                    }
                }
                CoroutineScope(Dispatchers.Default).launch {
                    while (true) {
                        if (refreshFlag) {
                            execADB(ADB_DEVICE_BRAND)
                            execADB(ADB_DEVICE_NAME)
                            execADB(ADB_HONOR_GET_MCC)
                            execADB(ADB_HONOR_GET_MCC_LEVEL)
                            execADB(ADB_HONOR_GET_MCC_ENABLE_OVERSEA)
                        }
                        delay(5000)
                    }
                }
                CoroutineScope(Dispatchers.Default).launch {
                    execADB(ADB_HONOR_GET_MCC)
                    execADB(ADB_HONOR_GET_MCC_LEVEL)
                    execADB(ADB_HONOR_GET_MCC_ENABLE_OVERSEA)
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
                    Row {
                        Text(
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f).padding(2.dp, 0.dp, 0.dp, 10.dp),
                            textAlign = TextAlign.Start,
                            text = "$deviceBrand $deviceName"
                        )
                        Image(
                            painter = painterResource(Res.drawable.icon_scrcpy),
                            "scrcpy",
                            colorFilter = ColorFilter.tint(
                                ColorText
                            ),
                            modifier = Modifier.align(Alignment.CenterVertically)
                                .padding(5.dp)
                                .height(22.dp)
                                .width(22.dp).clickable {
                                    AdbExecutor.exec(SCREEN_COPY, object : AdbExecuteCallback {
                                        override fun onPrint(line: String) {
                                        }

                                        override fun onExit(exitCode: Int) {
                                            println("scrcpy$exitCode")
                                            if (exitCode == -1) {
                                                outputText =
                                                    appendOutput(outputText, "scrcpy not install")
                                            }
                                        }
                                    })
                                },
                        )
                    }
                    Box {
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
                        Image(
                            painter = painterResource(Res.drawable.icon_delete),
                            "delete log",
                            colorFilter = ColorFilter.tint(
                                ColorGray
                            ),
                            modifier = Modifier.align(Alignment.BottomEnd)
                                .padding(end = 10.dp, bottom = 10.dp)
                                .height(20.dp)
                                .width(20.dp).clickable {
                                    outputText = ""
                                },
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .width(280.dp)
                        .padding(start = 20.dp)
                ) {

                    val onButtonClick = { rawCommand: String ->
                        val adbCommand = rawCommand
                        // require package name
                        if (adbCommand.contains(PACKAGE_NAME_HOLDER)) {
                            var cmd: String = adbCommand
                            if (packageName.isNotEmpty()) {
                                cmd = cmd.replace(PACKAGE_NAME_HOLDER, packageName)
                                CoroutineScope(Dispatchers.Default).launch {
                                    ApplicationComponent.coreComponent.appPreferences.putString(
                                        AppPreferencesKey.TARGET_PACKAGE_NAME,
                                        packageName
                                    )
                                }
                            } else {
                                packageNameInputHint = true
                            }
                            // execute
                            execADB(cmd)
                        } else if (adbCommand.contains(FILE_PATH_HOLDER)) {
                            // require pick a file
                            CoroutineScope(Dispatchers.Default).launch {
                                val file = FileKit.pickFile(title = "Pick File")
                                println(file?.path)
                                val path = file?.path
                                path?.let {
                                    if (path != "null") {
                                        var cmd: String = adbCommand
                                        if (path.isNotEmpty()) {
                                            if (path.contains(" ")) {
                                                path.replace(" ", SPACE_HOLDER)
                                            }
                                            cmd = cmd.replace(FILE_PATH_HOLDER, path)
                                            // execute
                                            execADB(cmd)
                                        }
                                    }
                                }
                            }
                        } else if (adbCommand.contains(DIR_PATH_HOLDER)) {
                            // require pick a directory
                            CoroutineScope(Dispatchers.Default).launch {
                                val file = FileKit.pickDirectory(title = "Pick Directory")
                                println(file?.path)
                                val path = file?.path
                                path.let {
                                    if (!path.equals("null")) {
                                        var cmd: String = adbCommand
                                        if (path?.isNotEmpty() == true) {
                                            cmd = cmd.replace(DIR_PATH_HOLDER, path)
                                            // execute
                                            execADB(cmd)
                                        }
                                    }
                                }
                            }
                        } else {
                            // execute directly
                            execADB(adbCommand)
                        }
                    }

                    Column {
                        Text(
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth().padding(0.dp, 5.dp, 0.dp, 5.dp),
                            textAlign = TextAlign.Start,
                            text = "Target Package Name :",
                            fontWeight = FontWeight(600),
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
                                ).padding(top = 8.dp, bottom = 8.dp, start = 10.dp, end = 10.dp)
                        )
                    }
                    AppPanel(onButtonClick)
                    DevicePanel(onButtonClick)
                    ScreenPanel(
                        isRecording,
                        readableRecordingDuration,
                        onButtonClick
                    )
                    MccPanel(
                        honorCurrentMcc,
                        honorCurrentMccLevel,
                        honorCurrentMccOverseaEnable,
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
            text = "App",
            fontWeight = FontWeight(600)
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
            fontWeight = FontWeight(600),
            text = "Device"
        )
        Row {
            AdbExecuteButton("Remount") {
                onButtonClick.invoke(ADB_REMOUNT)
            }
            AdbExecuteButton("Root") {
                onButtonClick.invoke(ADB_ROOT)
            }
        }
        Row {
            AdbExecuteButton("Language Change") {
                onButtonClick.invoke(ADB_OPEN_LANGUAGE_CHANGE_SETTING)
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
                fontWeight = FontWeight(600),
                text = "ScreenShot / Record"
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
fun MccPanel(
    honorCurrentMcc: String,
    honorCurrentMccLevel: String,
    honorCurrentMccOverseaEnable: Boolean,
    onButtonClick: (String) -> Any
) {
    var mcc by remember { mutableStateOf(honorCurrentMcc) }

    Column {
        Text(
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 5.dp),
            textAlign = TextAlign.Start,
            text = "Honor Mobile Country Code : [$honorCurrentMcc]",
            fontWeight = FontWeight(600),
        )
        Box {
            BasicTextField(
                mcc,
                onValueChange = {
                    mcc = it
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
                    ).padding(top = 8.dp, bottom = 8.dp, start = 10.dp, end = 10.dp)
            )
            Image(
                painter = painterResource(Res.drawable.icon_send),
                "send mcc",
                colorFilter = ColorFilter.tint(
                    ColorText
                ),
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 5.dp)
                    .height(20.dp)
                    .width(20.dp).clickable {
                        onButtonClick.invoke(ADB_HONOR_PUT_MCC.replace(MCC_HOLDER, mcc))
                        onButtonClick.invoke(ADB_HONOR_MCC_BROAD_CAST_SEND.replace(MCC_HOLDER, mcc))
                    },
            )
        }
        Text(
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 5.dp),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight(600),
            text = "Test Level: [$honorCurrentMccLevel]  Oversea Enabled: [$honorCurrentMccOverseaEnable]"
        )
        Row {
            AdbExecuteButton("Test Level") {
                onButtonClick.invoke(ADB_HONOR_PUT_MCC_LEVEL)
            }
            AdbExecuteButton("Delete Level") {
                onButtonClick.invoke(ADB_HONOR_DELETE_MCC_LEVEL)
            }
        }
        Row {
            AdbExecuteButton("Oversea Enabled") {
                onButtonClick.invoke(ADB_HONOR_PUT_MCC_ENABLE_OVERSEA)
            }
            AdbExecuteButton("Oversea Disable") {
                onButtonClick.invoke(ADB_HONOR_DELETE_MCC_ENABLE_OVERSEA)
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
                indication = PressedIndication()
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
