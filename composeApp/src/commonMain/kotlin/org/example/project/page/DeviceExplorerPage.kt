package org.example.project.page

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.ApplicationComponent
import org.example.project.adb.ADB_DEVICE_BRAND
import org.example.project.adb.ADB_DEVICE_NAME
import org.example.project.adb.ADB_HONOR_GET_MCC
import org.example.project.adb.ADB_HONOR_GET_MCC_ENABLE_OVERSEA
import org.example.project.adb.ADB_HONOR_GET_MCC_LEVEL
import org.example.project.adb.ADB_ROOT
import org.example.project.adb.AdbFileExplorer
import org.example.project.util.AppPreferencesKey
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/29
 */
@Composable
@Preview
fun DeviceExplorerPage(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {

    var path = ArrayList<String>()

    DisposableEffect(key1 = lifecycleOwner) {
        // 进入组件时执行，lifecycleOwner 改变后重新执行（先回调 onDispose）
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                AdbFileExplorer.getFileList(ADB_ROOT)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Text(
        "Device Explorer",
        fontSize = 30.sp,
        fontWeight = FontWeight(700),
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
    )
}