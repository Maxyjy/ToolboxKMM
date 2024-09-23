package org.example.project.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.project.ApplicationComponent
import org.example.project.component.ColorDivider
import org.example.project.component.DimenDivider
import org.example.project.component.RButton
import org.example.project.component.RoundedCorner
import org.example.project.util.AppPreferencesKey
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 *
 * adb root
 * adb remount
 *
 * adb shell mv /system/priv-app/HnSystemServer/HyperComm.apk /system/priv-app/HnSystemServer/HyperComm.apk.honor
 * adb push ${} /system/priv-app/HnSystemServer
 *
 * adb shell mv /product_h/region_comm/china/app/HnRoamingRed/HnRoamingRed.apk /product_h/region_comm/china/app/HnRoamingRed/HnRoamingRed.apk.honor
 * adb push ${} /product_h/region_comm/china/app/HnRoamingRed
 *
 * @author YangJianyu
 * @date 2024/8/29
 */
@Composable
@Preview
fun ApkPushPage(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {
    var hyperCommPath by remember { mutableStateOf("") }
    var redTeaRoamingPath by remember { mutableStateOf("") }

    val hyperCommApkPickLauncher = rememberFilePickerLauncher(
        title = "Pick RedTea Signed HyperComm Apk",
    ) { file ->
        println(file?.path)
        val path = file?.path
        path?.let {
            if (path != "null" && path.isNotEmpty()) {
                hyperCommPath = path
            }
        }
    }

    val redTeaSignedApkPickLauncher = rememberFilePickerLauncher(
        title = "Pick RedTea Signed Roaming Apk",
    ) { file ->
        println(file?.path)
        val path = file?.path
        path?.let {
            if (path != "null" && path.isNotEmpty()) {
                redTeaRoamingPath = path
            }
        }
    }

    DisposableEffect(key1 = lifecycleOwner) {
        // 进入组件时执行，lifecycleOwner 改变后重新执行（先回调 onDispose）
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                CoroutineScope(Dispatchers.Default).launch {
                    val hyperCommApkPath =
                        ApplicationComponent.coreComponent.appPreferences.getString(
                            AppPreferencesKey.HYPER_COMM_APK_PATH
                        )
                    val redTeaRoamingApkPath =
                        ApplicationComponent.coreComponent.appPreferences.getString(
                            AppPreferencesKey.RED_TEA_MOBILE_APK_PATH
                        )
                    if (!hyperCommApkPath.isNullOrEmpty()) {
                        hyperCommPath = hyperCommApkPath
                    }
                    if (!redTeaRoamingApkPath.isNullOrEmpty()) {
                        redTeaRoamingPath = redTeaRoamingApkPath
                    }

                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column {
        Text(
            "APK File Push",
            fontSize = 30.sp,
            fontWeight = FontWeight(700),
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
        )

        Text(
            text = "Pick HyperComm APK File:",
            fontSize = 14.sp,
            modifier = Modifier.padding(2.dp, 0.dp, 0.dp, 10.dp),
            textAlign = TextAlign.Start,
        )
        BasicTextField(
            value = if (hyperCommPath == "null") {
                ""
            } else {
                hyperCommPath
            },
            onValueChange = {
                hyperCommPath = it
            },
            modifier = Modifier.fillMaxWidth()
                .height(80.dp)
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
        Text(
            text = "Pick RedTeaRoaming APK File:",
            fontSize = 14.sp,
            modifier = Modifier.padding(2.dp, 0.dp, 0.dp, 10.dp),
            textAlign = TextAlign.Start,
        )
        BasicTextField(
            value = if (redTeaRoamingPath == "null") {
                ""
            } else {
                redTeaRoamingPath
            },
            onValueChange = {
                redTeaRoamingPath = it
            },
            modifier = Modifier.fillMaxWidth()
                .height(80.dp)
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
            RButton(
                onClick = {
                    hyperCommApkPickLauncher.launch()
                }, "Pick HyperComm APK"
            )
            Spacer(modifier = Modifier.width(20.dp))
            RButton(
                onClick = {
                    redTeaSignedApkPickLauncher.launch()
                }, "Pick RedTea Roaming APK"
            )
            RButton(
                onClick = {
                    CoroutineScope(Dispatchers.Default).launch {
                        ApplicationComponent.coreComponent.appPreferences.putString(
                            AppPreferencesKey.HYPER_COMM_APK_PATH,
                            hyperCommPath
                        )
                        ApplicationComponent.coreComponent.appPreferences.putString(
                            AppPreferencesKey.RED_TEA_MOBILE_APK_PATH,
                            redTeaRoamingPath
                        )
                    }
                }, "Process"
            )
        }

    }
}