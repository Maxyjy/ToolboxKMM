package org.example.project.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.icon_check_box_checked
import kotlinproject.composeapp.generated.resources.icon_check_box_uncheck
import kotlinproject.composeapp.generated.resources.icon_error
import kotlinproject.composeapp.generated.resources.icon_folder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.project.adb.ADB_REBOOT
import org.example.project.adb.ADB_ROOT
import org.example.project.adb.AdbExecuteCallback
import org.example.project.adb.AdbExecutor
import org.example.project.adb.AdbFileExplorer
import org.example.project.adb.ApkPushExecutor
import org.example.project.adb.ApkPushExecutor.HONOR_SUFFIX
import org.example.project.adb.ApkPushExecutor.HYPER_COMM_APK_NAME
import org.example.project.adb.ApkPushExecutor.HYPER_COMM_APK_PATH
import org.example.project.adb.ApkPushExecutor.ROAMING_APK_NAME
import org.example.project.adb.ApkPushExecutor.ROAMING_APK_PATH
import org.example.project.component.ColorDivider
import org.example.project.component.ColorTheme
import org.example.project.component.DimenDivider
import org.example.project.component.PressedIndication
import org.example.project.component.RButton
import org.example.project.component.RoundedCorner
import org.example.project.util.AppPreferencesKey
import org.example.project.util.SettingsDelegate
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
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
 * adb shell ls /product_h/region_comm/china/app/HnRoamingRed
 * adb shell rm /product_h/region_comm/china/app/HnRoamingRed/xxxx.apk
 * adb shell mv /product_h/region_comm/china/app/HnRoamingRed/HnRoamingRed.apk.apk.honor /product_h/region_comm/china/app/HnRoamingRed/HnRoamingRed
 *
 * adb shell mv /system/priv-app/HnSystemServer/HyperComm.apk /system/priv-app/HnSystemServer/HyperComm.apk.redtea
 * adb shell mv /system/priv-app/HnSystemServer/HyperComm.apk.honor /system/priv-app/HnSystemServer/HyperComm.apk
 *
 *
 * @author YangJianyu
 * @date 2024/8/29
 */
const val ADB_RESULT_UNKNOWN = 0
const val ADB_RESULT_OK = 1
const val ADB_RESULT_FAILED = -1

@Composable
@Preview
fun ApkPushPage(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {
    var hyperCommPath by remember { mutableStateOf("") }
    var redTeaRoamingPath by remember { mutableStateOf("") }

    var rootResult by remember { mutableStateOf(ADB_RESULT_UNKNOWN) }
    var remountResult by remember { mutableStateOf(ADB_RESULT_UNKNOWN) }

    var redTeaHyperCommRenameResult by remember { mutableStateOf(ADB_RESULT_UNKNOWN) }
    var redTeaRedTeaRoamingPushResult by remember { mutableStateOf(ADB_RESULT_UNKNOWN) }

    var redTeaRedTeaRoamingRenameResult by remember { mutableStateOf(ADB_RESULT_UNKNOWN) }
    var redTeaHyperCommPushResult by remember { mutableStateOf(ADB_RESULT_UNKNOWN) }

    var honorRedTeaRoamingDeleteResult by remember { mutableStateOf(ADB_RESULT_UNKNOWN) }
    var honorHyperDeleteResult by remember { mutableStateOf(ADB_RESULT_UNKNOWN) }

    var honorRedTeaRoamingRenameResult by remember { mutableStateOf(ADB_RESULT_UNKNOWN) }
    var honorHyperCommRenameResult by remember { mutableStateOf(ADB_RESULT_UNKNOWN) }


    fun getIconByResult(result: Int): DrawableResource {
        return when (result) {
            ADB_RESULT_UNKNOWN -> {
                Res.drawable.icon_check_box_uncheck
            }

            ADB_RESULT_OK -> {
                Res.drawable.icon_check_box_checked
            }

            ADB_RESULT_FAILED -> {
                Res.drawable.icon_error
            }

            else -> {
                Res.drawable.icon_check_box_uncheck
            }
        }
    }

    val hyperCommApkPickLauncher = rememberFilePickerLauncher(
        title = "RedTea Signed HyperComm Apk",
        type = PickerType.File(extensions = listOf("apk"))
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
        title = "RedTea Signed Roaming Apk",
        type = PickerType.File(extensions = listOf("apk"))
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
                        SettingsDelegate.getString(
                            AppPreferencesKey.HYPER_COMM_APK_PATH
                        )
                    val redTeaRoamingApkPath =
                        SettingsDelegate.getString(
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

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "APK File Push",
            fontSize = 30.sp,
            fontWeight = FontWeight(700),
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
        )

        Text(
            text = "HyperComm APK File :",
            fontSize = 14.sp,
            modifier = Modifier.padding(2.dp, 0.dp, 0.dp, 10.dp),
            textAlign = TextAlign.Start,
        )
        Row(
            modifier = Modifier.fillMaxWidth(1f)
                .height(65.dp)
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
            BasicTextField(
                value = if (hyperCommPath == "null") {
                    ""
                } else {
                    hyperCommPath
                },
                onValueChange = {
                    hyperCommPath = it
                },
                modifier = Modifier.weight(1f)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp, bottom = 10.dp)
            )
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
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
                            hyperCommApkPickLauncher.launch()
                        }.padding(3.dp)
                )
            }
        }
        Text(
            text = "RedteaRoaming APK File :",
            fontSize = 14.sp,
            modifier = Modifier.padding(2.dp, 0.dp, 0.dp, 10.dp),
            textAlign = TextAlign.Start,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(1f)
                .height(65.dp)
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
            BasicTextField(
                value = if (redTeaRoamingPath == "null") {
                    ""
                } else {
                    redTeaRoamingPath
                },
                onValueChange = {
                    redTeaRoamingPath = it
                },
                modifier = Modifier.weight(1f)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp, bottom = 10.dp)
            )
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
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
                            redTeaSignedApkPickLauncher.launch()
                        }.padding(3.dp)
                )
            }
        }
        Column(modifier = Modifier.padding(top = 5.dp)) {
            Text(
                text = "Steps :",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
                textAlign = TextAlign.Center,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(getIconByResult(rootResult)),
                    "Root",
                    colorFilter = ColorFilter.tint(
                        ColorTheme
                    ),
                    modifier = Modifier
                        .height(26.dp)
                        .width(26.dp).padding(end = 5.dp)
                )
                Text(
                    text = "Root",
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(getIconByResult(remountResult)),
                    "Remount",
                    colorFilter = ColorFilter.tint(
                        ColorTheme
                    ),
                    modifier = Modifier
                        .height(26.dp)
                        .width(26.dp).padding(end = 5.dp)
                )
                Text(
                    text = "Remount",
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(getIconByResult(redTeaHyperCommRenameResult)),
                    "Rename Hypercomm.apk to Hypercomm.apk.honor",
                    colorFilter = ColorFilter.tint(
                        ColorTheme
                    ),
                    modifier = Modifier
                        .height(26.dp)
                        .width(26.dp).padding(end = 5.dp)
                )
                Text(
                    text = "Rename Hypercomm.apk to Hypercomm.apk.honor",
                    fontSize = 14.sp,
                    maxLines = 1,
                    lineHeight = 14.sp,
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(getIconByResult(redTeaHyperCommPushResult)),
                    "Push Redtea Signed Hypercomm Apk",
                    colorFilter = ColorFilter.tint(
                        ColorTheme
                    ),
                    modifier = Modifier
                        .height(26.dp)
                        .width(26.dp).padding(end = 5.dp)
                )
                Text(
                    text = "Push Redtea Signed Hypercomm Apk",
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    modifier = Modifier,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(getIconByResult(redTeaRedTeaRoamingRenameResult)),
                    "Rename HnRoamingRed.apk to HnRoamingRed.apk.honor",
                    colorFilter = ColorFilter.tint(
                        ColorTheme
                    ),
                    modifier = Modifier
                        .height(26.dp)
                        .width(26.dp).padding(end = 5.dp)
                )
                Text(
                    text = "Rename HnRoamingRed.apk to HnRoamingRed.apk.honor",
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    modifier = Modifier,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(getIconByResult(redTeaRedTeaRoamingPushResult)),
                    "Push Redtea Signed RedteaRoaming Apk",
                    colorFilter = ColorFilter.tint(
                        ColorTheme
                    ),
                    modifier = Modifier
                        .height(26.dp)
                        .width(26.dp).padding(end = 5.dp)
                )
                Text(
                    text = "Push Redtea Signed RedteaRoaming Apk",
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    maxLines = 1,
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(1f).padding(top = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                RButton(onClick = {
                    AdbExecutor.exec(ADB_REBOOT, object : AdbExecuteCallback {
                        override fun onPrint(line: String) {
                        }
                    })
                }, "Reboot")
                Spacer(modifier = Modifier.width(15.dp))
                RButton(
                    onClick = {
                        rootResult = ADB_RESULT_UNKNOWN
                        remountResult = ADB_RESULT_UNKNOWN
                        redTeaHyperCommRenameResult = ADB_RESULT_UNKNOWN
                        redTeaHyperCommPushResult = ADB_RESULT_UNKNOWN
                        redTeaRedTeaRoamingRenameResult = ADB_RESULT_UNKNOWN
                        redTeaRedTeaRoamingPushResult = ADB_RESULT_UNKNOWN

                        CoroutineScope(Dispatchers.Default).launch {
                            SettingsDelegate.putString(
                                AppPreferencesKey.HYPER_COMM_APK_PATH,
                                hyperCommPath
                            )
                            SettingsDelegate.putString(
                                AppPreferencesKey.RED_TEA_MOBILE_APK_PATH,
                                redTeaRoamingPath
                            )

                            ApkPushExecutor.root { _rootResult ->
                                rootResult = _rootResult
                                if (rootResult == ADB_RESULT_OK) {
                                    ApkPushExecutor.remount { _remountResult ->
                                        remountResult = _remountResult
                                        if (remountResult == ADB_RESULT_OK) {
                                            CoroutineScope(Dispatchers.Default).launch {
                                                ApkPushExecutor.rename(
                                                    oldFilePath = HYPER_COMM_APK_PATH + HYPER_COMM_APK_NAME,
                                                    newFilePath = HYPER_COMM_APK_PATH + HYPER_COMM_APK_NAME + HONOR_SUFFIX
                                                ) { _renameHyperCommResult ->
                                                    redTeaHyperCommRenameResult =
                                                        _renameHyperCommResult
                                                    ApkPushExecutor.push(
                                                        hyperCommPath,
                                                        HYPER_COMM_APK_PATH
                                                    ) { _hyperCommPushResult ->
                                                        redTeaHyperCommPushResult =
                                                            _hyperCommPushResult
                                                    }
                                                }
                                            }
                                            CoroutineScope(Dispatchers.Default).launch {
                                                ApkPushExecutor.rename(
                                                    oldFilePath = ROAMING_APK_PATH + ROAMING_APK_NAME,
                                                    newFilePath = ROAMING_APK_PATH + ROAMING_APK_NAME + HONOR_SUFFIX
                                                ) { _renameRoamingResult ->
                                                    redTeaRedTeaRoamingRenameResult =
                                                        _renameRoamingResult
                                                    ApkPushExecutor.push(
                                                        redTeaRoamingPath,
                                                        ROAMING_APK_PATH
                                                    ) { _redTeaRoamingPushResult ->
                                                        redTeaRedTeaRoamingPushResult =
                                                            _redTeaRoamingPushResult
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }, "Process"
                )
            }
//            Column {
//                Text(
//                    text = "To Honor Signature Steps :",
//                    fontSize = 14.sp,
//                    modifier = Modifier.padding(bottom = 5.dp),
//                    textAlign = TextAlign.Center,
//                )
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Image(
//                        painter = painterResource(getIconByResult(honorRedTeaRoamingDeleteResult)),
//                        "Delete vSIM.. apk(Redtea Signature)",
//                        colorFilter = ColorFilter.tint(
//                            ColorTheme
//                        ),
//                        modifier = Modifier
//                            .height(26.dp)
//                            .width(26.dp).padding(end = 5.dp)
//                    )
//                    Text(
//                        text = "Delete vSIM.. apk(Redtea Signature)",
//                        fontSize = 14.sp,
//                        lineHeight = 14.sp,
//                        modifier = Modifier,
//                        textAlign = TextAlign.Center,
//                    )
//                }
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Image(
//                        painter = painterResource(getIconByResult(honorRedTeaRoamingRenameResult)),
//                        "Rename HnRedRoaming.apk.honor to HnRedRoaming.apk",
//                        colorFilter = ColorFilter.tint(
//                            ColorTheme
//                        ),
//                        modifier = Modifier
//                            .height(26.dp)
//                            .width(26.dp).padding(end = 5.dp)
//                    )
//                    Text(
//                        text = "Rename HnRedRoaming.apk.honor to HnRedRoaming.apk",
//                        fontSize = 14.sp,
//                        lineHeight = 14.sp,
//                        modifier = Modifier,
//                        textAlign = TextAlign.Center,
//                    )
//                }
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Image(
//                        painter = painterResource(getIconByResult(honorHyperDeleteResult)),
//                        "Delete Hypercomm.apk(Redtea Signature)",
//                        colorFilter = ColorFilter.tint(
//                            ColorTheme
//                        ),
//                        modifier = Modifier
//                            .height(26.dp)
//                            .width(26.dp).padding(end = 5.dp)
//                    )
//                    Text(
//                        text = "Delete Hypercomm.apk(Redtea Signature)",
//                        fontSize = 14.sp,
//                        lineHeight = 14.sp,
//                        modifier = Modifier,
//                        textAlign = TextAlign.Center,
//                    )
//                }
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Image(
//                        painter = painterResource(getIconByResult(honorHyperCommRenameResult)),
//                        "Rename Hypercomm.apk.honor to Hypercomm.apk",
//                        colorFilter = ColorFilter.tint(
//                            ColorTheme
//                        ),
//                        modifier = Modifier
//                            .height(26.dp)
//                            .width(26.dp).padding(end = 5.dp)
//                    )
//                    Text(
//                        text = "Rename Hypercomm.apk.honor to Hypercomm.apk",
//                        fontSize = 14.sp,
//                        lineHeight = 14.sp,
//                        modifier = Modifier,
//                        textAlign = TextAlign.Center,
//                    )
//                }
//                Row(
//                    modifier = Modifier.fillMaxWidth(1f).padding(top = 10.dp),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    RButton(onClick = {
//                        AdbExecutor.exec(ADB_REBOOT, object : AdbExecuteCallback {
//                            override fun onPrint(line: String) {
//                            }
//                        })
//                    }, "Reboot")
//                    Spacer(modifier = Modifier.width(15.dp))
//                    RButton(
//                        onClick = {
//                            rootResult = ADB_RESULT_UNKNOWN
//                            remountResult = ADB_RESULT_UNKNOWN
//                            redTeaHyperCommRenameResult = ADB_RESULT_UNKNOWN
//                            redTeaHyperCommPushResult = ADB_RESULT_UNKNOWN
//                            redTeaRedTeaRoamingRenameResult = ADB_RESULT_UNKNOWN
//                            redTeaRedTeaRoamingPushResult = ADB_RESULT_UNKNOWN
//
//                            CoroutineScope(Dispatchers.Default).launch {
//                                SettingsDelegate.putString(
//                                    AppPreferencesKey.HYPER_COMM_APK_PATH,
//                                    hyperCommPath
//                                )
//                                SettingsDelegate.putString(
//                                    AppPreferencesKey.RED_TEA_MOBILE_APK_PATH,
//                                    redTeaRoamingPath
//                                )
//
//                                ApkPushExecutor.root { _rootResult ->
//                                    rootResult = _rootResult
//                                    if (rootResult == ADB_RESULT_OK) {
//                                        ApkPushExecutor.remount { _remountResult ->
//                                            remountResult = _remountResult
//                                            if (remountResult == ADB_RESULT_OK) {
//                                                CoroutineScope(Dispatchers.Default).launch {
//                                                    // redtea roaming apk revert
//                                                    AdbFileExplorer.getFileList(ROAMING_APK_PATH) { childFiles ->
//                                                        println("apk" + childFiles.size)
//                                                        // delete old redtea roaming apk
//                                                        childFiles.forEach {
//                                                            if (it.endsWith(".apk")) {
//                                                                ApkPushExecutor.remove(it) { result ->
//                                                                    honorRedTeaRoamingDeleteResult =
//                                                                        result
//                                                                }
//                                                            }
//                                                        }
//                                                        // rename HnRedRoaming.apk.honor -> HnRedRoaming.apk
//                                                        ApkPushExecutor.rename(
//                                                            oldFilePath = ROAMING_APK_PATH + ROAMING_APK_NAME + HONOR_SUFFIX,
//                                                            newFilePath = ROAMING_APK_PATH + ROAMING_APK_NAME
//                                                        ) { result ->
//                                                            honorRedTeaRoamingRenameResult = result
//                                                        }
//                                                    }
//                                                }
//                                                CoroutineScope(Dispatchers.Default).launch {
//                                                    // hyper comm apk revert
//                                                    AdbFileExplorer.getFileList(HYPER_COMM_APK_PATH) { childFiles ->
//                                                        println("apk" + childFiles.size)
//                                                        // delete old hyper comm apk
//                                                        childFiles.forEach {
//                                                            if (it.endsWith(".apk")) {
//                                                                ApkPushExecutor.remove(it) { result ->
//                                                                    println("delete succ:$it")
//                                                                    honorHyperDeleteResult = result
//                                                                }
//                                                            }
//                                                            // rename HyperComm.apk.honor -> HyperComm.apk
//                                                            ApkPushExecutor.rename(
//                                                                oldFilePath = HYPER_COMM_APK_PATH + HYPER_COMM_APK_NAME + HONOR_SUFFIX,
//                                                                newFilePath = HYPER_COMM_APK_PATH + HYPER_COMM_APK_NAME
//                                                            ) { result ->
//                                                                honorHyperCommRenameResult = result
//                                                                println("rename succ HyperComm.apk.honor -> HyperComm.apk")
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }, "Honor"
//                    )
//                }
//            }

            // Root
            // Remount
            // Delete vSIM.. apk(Redtea Signature)
            // Rename HnRedRoaming.apk.honor to HnRedRoaming.apk
            // Delete Hypercomm.apk(Redtea Signature)
            // Rename Hypercomm.apk.honor to Hypercomm.apk

        }
    }

}