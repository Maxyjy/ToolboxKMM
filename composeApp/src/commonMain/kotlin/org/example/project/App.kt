package org.example.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.icon_folder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.adb.ADB_ANDROID_VERSION
import org.example.project.adb.ADB_APP_VERSION
import org.example.project.adb.ADB_DEVICE_BRAND
import org.example.project.adb.ADB_DEVICE_NAME
import org.example.project.adb.ADB_HONOR_GET_MCC
import org.example.project.adb.ADB_HONOR_GET_MCC_ENABLE_OVERSEA
import org.example.project.adb.ADB_HONOR_GET_MCC_LEVEL
import org.example.project.component.ColorDivider
import org.example.project.component.ColorGray
import org.example.project.component.ColorPanelBackgroundMac
import org.example.project.component.ColorPanelBackgroundWin
import org.example.project.component.ColorText
import org.example.project.component.ColorTheme
import org.example.project.component.DimenDivider
import org.example.project.component.PressedIndication
import org.example.project.component.RButton
import org.example.project.component.RoundedCorner
import org.example.project.page.AdbControlPage
import org.example.project.page.DeviceExplorerPage
import org.example.project.page.ApkInspectPage
import org.example.project.page.Base64Page
import org.example.project.page.JsonFormatPage
import org.example.project.page.LogProcessPage
import org.example.project.page.PerformancePage
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.example.project.component.SideBar
import org.example.project.page.ApkPushPage
import org.example.project.page.SettingsPage
import org.example.project.util.AppPreferencesKey.ANDROID_HOME_PATH
import org.example.project.util.SettingsDelegate
import org.jetbrains.compose.resources.painterResource

@Composable
@Preview
fun App(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {

    var dialogState by remember { mutableStateOf(false) }
    var androidHomePath by remember { mutableStateOf("") }
    var androidHomePathHintAlpha by remember { mutableStateOf(1f) }
    var rightPanelIndex by remember { mutableStateOf(0) }

    val launcher = rememberDirectoryPickerLauncher(
        title = "Pick Android Home Path",
    ) { directory ->
        CoroutineScope(Dispatchers.Default).launch {
            println(directory?.path)
            val path = directory?.path
            if (path?.isNotEmpty() == true && path != "null") {
                androidHomePath = path
            }
        }
    }

    MaterialTheme {
        DisposableEffect(key1 = lifecycleOwner) {
            // 进入组件时执行，lifecycleOwner 改变后重新执行（先回调 onDispose）
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    CoroutineScope(Dispatchers.Default).launch {
                        androidHomePath = SettingsDelegate.getString(ANDROID_HOME_PATH).toString()
                        if (androidHomePath.isNotEmpty() && androidHomePath != "null") {
                            dialogState = false
                        } else {
                            dialogState = true
                            androidHomePath = ""
                        }
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        if (dialogState) {
            Dialog(onDismissRequest = {}, properties = DialogProperties()) {
                Column(
                    modifier = Modifier.background(Color.White, RoundedCornerShape(RoundedCorner))
                        .padding(top = 10.dp, bottom = 10.dp, start = 15.dp, end = 15.dp)
                ) {
                    Text(
                        fontSize = 18.sp,
                        modifier = Modifier.fillMaxWidth().padding(0.dp, 5.dp, 0.dp, 5.dp),
                        textAlign = TextAlign.Start,
                        text = "Adb Tool Path Require",
                        lineHeight = 18.sp,
                        color = ColorText,
                        fontWeight = FontWeight(600)
                    )
                    Text(
                        modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        lineHeight = 22.sp,
                        text = "adb tools require locate android home path of system environment",
                        color = ColorText
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(1f)
                            .height(50.dp)
                            .padding(0.dp, 0.dp, 0.dp, 10.dp)
                            .border(
                                DimenDivider,
                                color = ColorDivider,
                                shape = RoundedCornerShape(RoundedCorner)
                            ).background(
                                Color.White,
                                RoundedCornerShape(RoundedCorner)
                            )
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            BasicTextField(
                                androidHomePath,
                                onValueChange = {
                                    androidHomePath = it
                                    androidHomePathHintAlpha = if (androidHomePath.isNotEmpty()) {
                                        0f
                                    } else {
                                        1f
                                    }
                                },
                                modifier = Modifier
                                    .padding(top = 8.dp, bottom = 8.dp, start = 10.dp, end = 10.dp)
                            )
                            Text(
                                modifier = Modifier.padding(
                                    top = 8.dp,
                                    bottom = 8.dp,
                                    start = 10.dp,
                                    end = 10.dp
                                ).alpha(androidHomePathHintAlpha),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 6.sp,
                                text = "e.g. /../Android/sdk",
                                color = ColorGray
                            )
                        }
                        Image(
                            painter = painterResource(Res.drawable.icon_folder),
                            "pick file",
                            colorFilter = ColorFilter.tint(
                                ColorTheme
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                                .height(26.dp)
                                .width(26.dp).clickable(
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
                        RButton(onClick = { dialogState = false }, "Not Now")
                        Spacer(modifier = Modifier.width(15.dp))
                        RButton(onClick = {
                            if (androidHomePath.isNotEmpty()) {
                                dialogState = false
                                CoroutineScope(Dispatchers.Default).launch {
                                    SettingsDelegate.putString(ANDROID_HOME_PATH, androidHomePath)
                                }
                            }
                        }, "Confirm")
                    }
                }
            }
        }

        Row(
            Modifier.fillMaxWidth().background(Color(0xFFFFFFFF)),
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier.width(80.dp).fillMaxHeight()
            ) {
                SideBar { index -> rightPanelIndex = index }
            }
            Box(modifier = Modifier.fillMaxHeight().width(DimenDivider).background(ColorDivider))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        if (getSystemName().contains("mac", true)) {
                            ColorPanelBackgroundMac
                        } else {
                            ColorPanelBackgroundWin
                        }
                    )
                    .padding(20.dp),
            ) {
                when (rightPanelIndex) {
                    0 -> {
                        AdbControlPage()
                    }

                    1 -> {
                        LogProcessPage()
                    }

                    2 -> {
                        ApkPushPage()
                    }

                    3 -> {
                        DeviceExplorerPage()
                    }

                    4 -> {
                        ApkInspectPage()
                    }

                    5 -> {
                        PerformancePage()
                    }

                    6 -> {
                        JsonFormatPage()
                    }

                    7 -> {
                        Base64Page()
                    }
                    8->{
                        SettingsPage()
                    }
                }
            }
        }
    }
}