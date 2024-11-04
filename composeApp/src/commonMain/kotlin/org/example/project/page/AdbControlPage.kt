package org.example.project.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.pickFile
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.icon_delete
import kotlinproject.composeapp.generated.resources.icon_folder
import kotlinproject.composeapp.generated.resources.icon_recorder_play
import kotlinproject.composeapp.generated.resources.icon_recorder_stop
import kotlinproject.composeapp.generated.resources.icon_send
import kotlinproject.composeapp.generated.resources.icon_switch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.adb.ADB_ANDROID_VERSION
import org.example.project.adb.ADB_APP_VERSION
import org.example.project.adb.ADB_BUILD_VERSION
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
import org.example.project.adb.ADB_HONOR_SCREEN_RECORDER_START
import org.example.project.adb.ADB_HONOR_SCREEN_RECORDER_STOP
import org.example.project.adb.ADB_INSTALL
import org.example.project.adb.ADB_KILL_APP
import org.example.project.adb.ADB_OPEN_ACCESSIBILITY_SETTING
import org.example.project.adb.ADB_OPEN_APP_DETAIL_SETTING
import org.example.project.adb.ADB_OPEN_DATA_ROAMING_SETTING
import org.example.project.adb.ADB_OPEN_DATE_SETTING
import org.example.project.adb.ADB_OPEN_HONOR_SIM_SETTING
import org.example.project.adb.ADB_OPEN_LANGUAGE_SETTING
import org.example.project.adb.ADB_OPEN_SETTING
import org.example.project.adb.ADB_OPEN_WIFI_SETTING
import org.example.project.adb.ADB_PRINT_PATH
import org.example.project.adb.ADB_REBOOT
import org.example.project.adb.ADB_REMOUNT
import org.example.project.adb.ADB_ROOT
import org.example.project.adb.ADB_SAVE_SCREEN_SHOT
import org.example.project.adb.ADB_SCREEN_SHOT
import org.example.project.adb.ADB_SCREEN_START_RECORD
import org.example.project.adb.ADB_SCREEN_ON
import org.example.project.adb.ADB_SCREEN_STOP_RECORD
import org.example.project.adb.ADB_SCREEN_SWIPE
import org.example.project.adb.ADB_START_APP
import org.example.project.adb.ADB_UNINSTALL
import org.example.project.adb.AdbExecuteCallback
import org.example.project.adb.AdbExecutor
import org.example.project.adb.DIR_PATH_HOLDER
import org.example.project.adb.FILE_PATH_HOLDER
import org.example.project.adb.MCC_HOLDER
import org.example.project.adb.PACKAGE_NAME_HOLDER
import org.example.project.adb.SPACE_HOLDER
import org.example.project.component.ColorGray
import org.example.project.component.PressedIndication
import org.example.project.component.RButton
import org.example.project.getSystemCurrentTimeMillis
import org.example.project.util.AppPreferencesKey
import org.example.project.util.AppPreferencesKey.SCRCPY_HOME_PATH
import org.example.project.util.SettingsDelegate
import org.jetbrains.compose.resources.DrawableResource
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

    var refreshFlag = true
    var outputText by remember { mutableStateOf("") }

    var scrcpyDialogState by remember { mutableStateOf(false) }

    var packageNameInputHint by remember { mutableStateOf(false) }
    var packageName by remember { mutableStateOf("") }
    var appVersionName by remember { mutableStateOf("") }
    var appVersionNameDuration = 0L

    var deviceName by remember { mutableStateOf("No Connected Device") }
    var deviceBrand by remember { mutableStateOf("") }
    var deviceAndroidVersion by remember { mutableStateOf("") }
    var deviceBuildVersion by remember { mutableStateOf("") }

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
                AdbExecutor.startRecord(object : AdbExecuteCallback {
                    override fun onPrint(line: String) {
                        outputText = appendOutput(outputText, line)
                    }

                    override fun onExit(exitCode: Int) {
                    }
                })
            }

            ADB_SCREEN_STOP_RECORD -> {
                AdbExecutor.stopRecord(object : AdbExecuteCallback {
                    override fun onPrint(line: String) {
                        outputText = appendOutput(outputText, line)
                    }

                    override fun onExit(exitCode: Int) {
                    }
                })
            }

            ADB_HONOR_GET_MCC -> {
                AdbExecutor.exec(ADB_HONOR_GET_MCC, object : AdbExecuteCallback {
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
                AdbExecutor.exec(ADB_HONOR_GET_MCC_LEVEL, object : AdbExecuteCallback {
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
                AdbExecutor.exec(ADB_HONOR_GET_MCC_ENABLE_OVERSEA, object : AdbExecuteCallback {
                    override fun onPrint(line: String) {
                        honorCurrentMccOverseaEnable = line == "1"
                        println("ADB_HONOR_GET_MCC_ENABLE_OVERSEA $line")
                    }
                })
            }

            ADB_APP_VERSION -> {
                if (packageName.isNotEmpty()) {
                    val adb = ADB_APP_VERSION.replace(PACKAGE_NAME_HOLDER, packageName)
                    AdbExecutor.exec(adb, object : AdbExecuteCallback {
                        override fun onPrint(line: String) {
                            if (getSystemCurrentTimeMillis() - appVersionNameDuration >= 1000L) {
                                if (line.contains("versionName")) {
                                    appVersionName = line.replace("versionName=", "").trim()
                                } else {
                                    appVersionName = ""
                                }
                            }
                            appVersionNameDuration = getSystemCurrentTimeMillis()
                        }
                    })
                }
            }

            ADB_BUILD_VERSION -> {
                AdbExecutor.exec(ADB_BUILD_VERSION, object : AdbExecuteCallback {
                    override fun onPrint(line: String) {
                        deviceBuildVersion = line
                    }
                })
            }

            ADB_ANDROID_VERSION -> {
                AdbExecutor.exec(ADB_ANDROID_VERSION, object : AdbExecuteCallback {
                    override fun onPrint(line: String) {
                        try {
                            val version = line.toInt()
                            deviceAndroidVersion = when (version) {
                                35 -> {
                                    "Android 15"
                                }

                                34 -> {
                                    "Android 14"
                                }

                                33 -> {
                                    "Android 13"
                                }

                                32 -> {
                                    "Android 12L"
                                }

                                31 -> {
                                    "Android 12"
                                }

                                30 -> {
                                    "Android 11"
                                }

                                29 -> {
                                    "Android 10"
                                }

                                28 -> {
                                    "Android 9"
                                }

                                27 -> {
                                    "Android 8.1"
                                }

                                26 -> {
                                    "Android 8.0"
                                }

                                else -> {
                                    ""
                                }
                            }
                        } catch (e: Exception) {
                            e.message
                            deviceAndroidVersion = ""
                        }
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
                }, cmdPrinter = { cmd ->
                    outputText = appendOutput(outputText, "———————————————————————————————")
                    outputText = appendOutput(outputText, cmd)
                    outputText = appendOutput(outputText, "———————————————————————————————")
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
            if (event == Lifecycle.Event.ON_START) {
                CoroutineScope(Dispatchers.Default).launch {
                    val targetPackageName =
                        SettingsDelegate.getString(AppPreferencesKey.TARGET_PACKAGE_NAME)
                    if (!targetPackageName.isNullOrEmpty()) {
                        packageName = targetPackageName
                        execADB(ADB_APP_VERSION)
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
                            execADB(ADB_APP_VERSION)
                            execADB(ADB_ANDROID_VERSION)
                            execADB(ADB_BUILD_VERSION)
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
            refreshFlag = false
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        if (scrcpyDialogState) {
            ScreenCopyPathDialog() {
                scrcpyDialogState = false
            }
        }
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
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f).padding(2.dp, 0.dp, 0.dp, 0.dp),
                            textAlign = TextAlign.Start,
                            lineHeight = 13.sp,
                            text = "$deviceBrand $deviceName ($deviceAndroidVersion)"
                        )
                    }
                    BasicTextField(
                        "$deviceBuildVersion              ",
                        textStyle = TextStyle(
                            fontSize = 11.sp,
                            lineHeight = 11.sp,
                            color = Color(0xff686868),
                            textAlign = TextAlign.Start
                        ),
                        onValueChange = {},
                        modifier = Modifier.padding(2.dp, 5.dp, 20.dp, 10.dp),
                    )
                    Box {
                        BasicTextField(
                            outputText,
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth().fillMaxHeight().border(
                                DimenDivider,
                                color = ColorDivider,
                                shape = RoundedCornerShape(RoundedCorner)
                            ).background(
                                Color.White, RoundedCornerShape(RoundedCorner)
                            ).padding(10.dp)
                        )
                        Image(
                            painter = painterResource(Res.drawable.icon_delete),
                            "delete log",
                            colorFilter = ColorFilter.tint(
                                ColorGray
                            ),
                            modifier = Modifier.align(Alignment.BottomEnd)
                                .padding(end = 10.dp, bottom = 10.dp).height(20.dp).width(20.dp)
                                .clickable {
                                    outputText = ""
                                },
                        )
                    }
                }
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()).width(280.dp)
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
                                    SettingsDelegate.putString(
                                        AppPreferencesKey.TARGET_PACKAGE_NAME, packageName
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp)
                        ) {
                            Text(
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Start,
                                text = "App Package Name :",
                                fontWeight = FontWeight(600),
                                color = if (packageNameInputHint) {
                                    ColorTheme
                                } else {
                                    ColorText
                                }
                            )
                        }
                        BasicTextField(
                            packageName, onValueChange = {
                                packageNameInputHint = false
                                packageName = it
                            }, modifier = Modifier.fillMaxWidth().wrapContentHeight().border(
                                DimenDivider,
                                color = ColorDivider,
                                shape = RoundedCornerShape(RoundedCorner)
                            ).background(
                                Color.White, RoundedCornerShape(RoundedCorner)
                            ).padding(top = 8.dp, bottom = 8.dp, start = 10.dp, end = 10.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                    AppPanel(appVersionName, onButtonClick)
                    SettingPanel(deviceBrand, onButtonClick)
                    DevicePanel(onButtonClick)
                    if (deviceBrand.contains("HONOR")) {
                        HonorScreenPanel(
                            onButtonClick
                        )
                        HonorMccPanel(
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
}

@Composable
fun AppPanel(appVersionName: String, onButtonClick: (String) -> Any) {
    Column {
        Row(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 7.dp)) {
            Text(
                fontSize = 12.sp,
                modifier = Modifier.wrapContentWidth().align(Alignment.CenterVertically),
                textAlign = TextAlign.Start,
                text = "App",
                fontWeight = FontWeight(600)
            )
            Text(
                fontSize = 10.sp,
                modifier = Modifier.wrapContentWidth().align(Alignment.CenterVertically)
                    .padding(start = 5.dp, end = 5.dp),
                textAlign = TextAlign.Start,
                text = if (appVersionName.isNotEmpty()) {
                    "($appVersionName)"
                } else {
                    ""
                },
                color = Color(0xff767676)
            )
        }
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
            AdbExecuteButton("Reboot") {
                onButtonClick.invoke(ADB_REBOOT)
            }
            AdbExecuteButton("Remount") {
                onButtonClick.invoke(ADB_REMOUNT)
            }
            AdbExecuteButton("Root") {
                onButtonClick.invoke(ADB_ROOT)
            }
        }
        Row {
            AdbExecuteButton("Top Activity") {
                onButtonClick.invoke(ADB_DUMP_SHOW_TOP_ACTIVITY)
            }
            AdbExecuteButton("Unlock") {
                CoroutineScope(Dispatchers.Default).launch {
                    onButtonClick.invoke(ADB_SCREEN_ON)
                    delay(500L)
                    onButtonClick.invoke(ADB_SCREEN_SWIPE)
                }
            }
        }
    }
}

@Composable
fun SettingPanel(deviceBrand: String, onButtonClick: (String) -> Any) {
    val brand by remember { mutableStateOf(deviceBrand) }
    Column {
        Text(
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 5.dp),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight(600),
            text = "Settings"
        )
        Row {
            AdbExecuteButton("Settings") {
                onButtonClick.invoke(ADB_OPEN_SETTING)
            }
            AdbExecuteButton("Date") {
                onButtonClick.invoke(ADB_OPEN_DATE_SETTING)
            }
            AdbExecuteButton("Language") {
                onButtonClick.invoke(ADB_OPEN_LANGUAGE_SETTING)
            }
        }
        Row {
            AdbExecuteButton("Accessibility") {
                onButtonClick.invoke(ADB_OPEN_ACCESSIBILITY_SETTING)
            }
            AdbExecuteButton("App Detail") {
                onButtonClick.invoke(ADB_OPEN_APP_DETAIL_SETTING)
            }
        }
        Row {
            if (brand.contains("HONOR")) {
                AdbExecuteButton("SIM Manage") {
                    onButtonClick.invoke(ADB_OPEN_HONOR_SIM_SETTING)
                }
            }
            AdbExecuteButton("Wifi") {
                onButtonClick.invoke(ADB_OPEN_WIFI_SETTING)
            }
            AdbExecuteButton("Data Roaming") {
                onButtonClick.invoke(ADB_OPEN_DATA_ROAMING_SETTING)
            }
        }
    }
}

@Composable
fun HonorScreenPanel(
    onButtonClick: (String) -> Any
) {
    Column {
        Row {
            Text(
                fontSize = 12.sp,
                modifier = Modifier.wrapContentWidth().padding(0.dp, 0.dp, 0.dp, 5.dp),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight(600),
                text = "Honor ScreenShot / Record"
            )
        }
        Row(modifier = Modifier.wrapContentHeight().height(intrinsicSize = IntrinsicSize.Max)) {
            AdbExecuteButton("Capture") {
                onButtonClick.invoke(ADB_SCREEN_SHOT)
            }
            Box(
                modifier = Modifier.fillMaxHeight().padding(end = 5.dp, bottom = 6.dp, top = 2.dp)
                    .width(0.5.dp)
                    .background(ColorDivider)
            )
            AdbExecuteButton(Res.drawable.icon_recorder_play, "Start") {
                onButtonClick.invoke(ADB_HONOR_SCREEN_RECORDER_START)
            }
            AdbExecuteButton(Res.drawable.icon_recorder_stop, "Stop") {
                onButtonClick.invoke(ADB_HONOR_SCREEN_RECORDER_STOP)
            }
            Box(
                modifier = Modifier.fillMaxHeight().padding(end = 5.dp, bottom = 6.dp, top = 2.dp)
                    .width(0.5.dp)
                    .background(ColorDivider)
            )
            AdbExecuteButton("Save") {
                onButtonClick.invoke(ADB_SAVE_SCREEN_SHOT)
            }
        }
    }
}

@Composable
fun HonorMccPanel(
    honorCurrentMcc: String,
    honorCurrentMccLevel: String,
    honorCurrentMccOverseaEnable: Boolean,
    onButtonClick: (String) -> Any,
) {
    var mcc by remember { mutableStateOf(honorCurrentMcc) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    var fastMode by remember { mutableStateOf(true) }

    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(0.dp, 5.dp, 0.dp, 5.dp)
        ) {
            Text(
                lineHeight = 12.sp,
                fontSize = 12.sp,
                modifier = Modifier.wrapContentWidth().align(Alignment.CenterVertically),
                textAlign = TextAlign.Start,
                text = "Honor MCC: ",
                fontWeight = FontWeight(600),
            )
            Text(
                lineHeight = 12.sp,
                fontSize = 12.sp,
                modifier = Modifier.wrapContentWidth().align(Alignment.CenterVertically),
                textAlign = TextAlign.Start,
                text = "[$honorCurrentMcc]",
                fontWeight = FontWeight(600),
            )
            Column(modifier = Modifier.weight(1f)) {
                Image(
                    painter = painterResource(Res.drawable.icon_switch),
                    "change mode",
                    colorFilter = ColorFilter.tint(
                        ColorDivider
                    ),
                    modifier = Modifier.align(Alignment.End)
                        .height(20.dp)
                        .width(20.dp).clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = PressedIndication(radius = 4f)
                        ) {
                            fastMode = !fastMode
                            println("fastMode$fastMode")
                        }.padding(3.dp),
                )
            }
        }
        if (fastMode) {
            Row {
                AdbExecuteButton("CN 460") {
                    CoroutineScope(Dispatchers.Main).launch {
                        onButtonClick.invoke(ADB_HONOR_PUT_MCC.replace(MCC_HOLDER, "460"))
                        delay(1000L)
                        onButtonClick.invoke(
                            ADB_HONOR_MCC_BROAD_CAST_SEND.replace(
                                MCC_HOLDER, mcc
                            )
                        )
                    }
                }
                AdbExecuteButton("HK 454") {
                    CoroutineScope(Dispatchers.Main).launch {
                        onButtonClick.invoke(ADB_HONOR_PUT_MCC.replace(MCC_HOLDER, "454"))
                        delay(1000L)
                        onButtonClick.invoke(
                            ADB_HONOR_MCC_BROAD_CAST_SEND.replace(
                                MCC_HOLDER, mcc
                            )
                        )
                    }
                }
                AdbExecuteButton("MO 455") {
                    CoroutineScope(Dispatchers.Main).launch {
                        onButtonClick.invoke(ADB_HONOR_PUT_MCC.replace(MCC_HOLDER, "455"))
                        delay(1000L)
                        onButtonClick.invoke(
                            ADB_HONOR_MCC_BROAD_CAST_SEND.replace(
                                MCC_HOLDER, mcc
                            )
                        )
                    }
                }
                AdbExecuteButton("TW 466") {
                    CoroutineScope(Dispatchers.Main).launch {
                        onButtonClick.invoke(ADB_HONOR_PUT_MCC.replace(MCC_HOLDER, "466"))
                        delay(1000L)
                        onButtonClick.invoke(
                            ADB_HONOR_MCC_BROAD_CAST_SEND.replace(
                                MCC_HOLDER, mcc
                            )
                        )
                    }
                }
            }
        } else {
            Box(modifier = Modifier.padding(bottom = 2.dp)) {
                BasicTextField(
                    mcc,
                    textStyle = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight(500),
                        fontStyle = FontStyle.Normal,
                    ),
                    onValueChange = {
                        mcc = it
                    }, modifier = Modifier.fillMaxWidth().border(
                        DimenDivider,
                        color = ColorDivider,
                        shape = RoundedCornerShape(RoundedCorner)
                    ).background(
                        Color.White, RoundedCornerShape(RoundedCorner)
                    ).padding(vertical = 8.dp, horizontal = 10.dp)
                )
                Image(
                    painter = painterResource(Res.drawable.icon_send),
                    "send mcc",
                    colorFilter = ColorFilter.tint(
                        ColorText
                    ),
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 5.dp).height(20.dp)
                        .width(20.dp).clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = PressedIndication(radius = 4f)
                        ) {
                            CoroutineScope(Dispatchers.Main).launch {
                                if (mcc.isNotEmpty()) {
                                    onButtonClick.invoke(ADB_HONOR_PUT_MCC.replace(MCC_HOLDER, mcc))
                                    delay(1000L)
                                    onButtonClick.invoke(
                                        ADB_HONOR_MCC_BROAD_CAST_SEND.replace(
                                            MCC_HOLDER, mcc
                                        )
                                    )
                                }
                            }
                        },
                )
            }
        }
        Text(
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth().padding(0.dp, 2.dp, 0.dp, 5.dp),
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
fun TextInputPanel(onButtonClick: (String) -> Any) {
    var inputText by remember { mutableStateOf("") }
    Column {
        Text(
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 5.dp),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight(600),
            text = "Text Input"
        )
        Box {
            BasicTextField(
                inputText,
                textStyle = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight(500),
                    fontStyle = FontStyle.Normal,
                ),
                onValueChange = {
                    inputText = it
                }, modifier = Modifier.fillMaxWidth().border(
                    DimenDivider,
                    color = ColorDivider,
                    shape = RoundedCornerShape(RoundedCorner)
                ).background(
                    Color.White, RoundedCornerShape(RoundedCorner)
                ).padding(vertical = 8.dp, horizontal = 10.dp)
            )
            Image(
                painter = painterResource(Res.drawable.icon_send),
                "input text",
                colorFilter = ColorFilter.tint(
                    ColorText
                ),
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 5.dp).height(20.dp)
                    .width(20.dp).clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = PressedIndication(radius = 4f)
                    ) {
                        onButtonClick.invoke(ADB_HONOR_PUT_MCC.replace(MCC_HOLDER, inputText))
                    },
            )
        }
    }
}

@Composable
fun AdbExecuteButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.padding(end = 5.dp, bottom = 5.dp).clickable(
            interactionSource = MutableInteractionSource(), indication = PressedIndication()
        ) {
            onClick.invoke()
        }.background(Color.White, RoundedCornerShape(4.dp)).border(
            width = 1.dp, color = ColorTheme, shape = RoundedCornerShape(4.dp)
        ).padding(vertical = 5.dp, horizontal = 5.dp)
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = text,
            lineHeight = 12.sp,
            color = ColorTheme,
            fontWeight = FontWeight(500),
            fontStyle = FontStyle.Normal,
            fontSize = 12.sp,
        )
    }
}

@Composable
fun AdbExecuteButton(resource: DrawableResource, text: String = "", onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 5.dp, bottom = 5.dp).clickable(
            interactionSource = MutableInteractionSource(), indication = PressedIndication()
        ) {
            onClick.invoke()
        }.background(Color.White, RoundedCornerShape(4.dp)).border(
            width = 1.dp, color = ColorTheme, shape = RoundedCornerShape(4.dp)
        ).padding(5.dp),
    ) {
        Image(
            painter = painterResource(resource),
            "",
            modifier = Modifier.height(13.dp).width(13.dp),
            colorFilter = ColorFilter.tint(
                ColorTheme
            ),
        )
        if (text.isNotEmpty()) {
            Text(
                textAlign = TextAlign.Center,
                text = text,
                lineHeight = 12.sp,
                color = ColorTheme,
                fontWeight = FontWeight(500),
                fontStyle = FontStyle.Normal,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

fun appendOutput(oldText: String, text: String): String {
    val appendText = if (oldText.isNotEmpty()) {
        "$text\n$oldText"
    } else {
        text
    }
    return appendText
}

@Composable
fun ScreenCopyPathDialog(dismissListener: (() -> Unit)) {
    var scrcpyPath by remember { mutableStateOf("") }
    var scrcpyPathHintAlpha by remember { mutableStateOf(1f) }
    val launcher = rememberDirectoryPickerLauncher(
        title = "Pick Scrcpy Path",
    ) { directory ->
        CoroutineScope(Dispatchers.Default).launch {
            println(directory?.path)
            val path = directory?.path
            if (path?.isNotEmpty() == true && path != "null") {
                scrcpyPath = path
            }
        }
    }

    Dialog(
        onDismissRequest = {},
    ) {
        Column(
            modifier = Modifier.background(Color.White, RoundedCornerShape(RoundedCorner))
                .padding(top = 10.dp, bottom = 10.dp, start = 15.dp, end = 15.dp)
        ) {
            Text(
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth().padding(0.dp, 5.dp, 0.dp, 5.dp),
                textAlign = TextAlign.Start,
                text = "Scrcpy Path Require",
                lineHeight = 18.sp,
                color = ColorText,
                fontWeight = FontWeight(600)
            )
            Text(
                modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                lineHeight = 22.sp,
                text = "scrcpy require locate scrcpy install path",
                color = ColorText
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(1f).height(50.dp).padding(0.dp, 0.dp, 0.dp, 10.dp)
                    .border(
                        DimenDivider,
                        color = ColorDivider,
                        shape = RoundedCornerShape(RoundedCorner)
                    ).background(
                        Color.White, RoundedCornerShape(RoundedCorner)
                    )
            ) {
                BasicTextField(
                    scrcpyPath,
                    onValueChange = {
                        scrcpyPath = it
                        scrcpyPathHintAlpha = if (scrcpyPath.isNotEmpty()) {
                            0f
                        } else {
                            1f
                        }
                    },
                    modifier = Modifier.weight(1f)
                        .padding(top = 8.dp, bottom = 8.dp, start = 10.dp, end = 10.dp)
                )
                Image(
                    painter = painterResource(Res.drawable.icon_folder),
                    "pick file",
                    colorFilter = ColorFilter.tint(
                        ColorTheme
                    ),
                    modifier = Modifier.padding(end = 8.dp).height(26.dp).width(26.dp).clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = PressedIndication(8f)
                    ) {
                        launcher.launch()
                    }.padding(3.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                RButton(onClick = { dismissListener.invoke() }, "Not Now")
                Spacer(modifier = Modifier.width(15.dp))
                RButton(onClick = {
                    if (scrcpyPath.isNotEmpty()) {
                        dismissListener.invoke()
                        CoroutineScope(Dispatchers.Default).launch {
                            SettingsDelegate.putString(SCRCPY_HOME_PATH, scrcpyPath)
                        }
                    }
                }, "Confirm")
            }
        }
    }
}
