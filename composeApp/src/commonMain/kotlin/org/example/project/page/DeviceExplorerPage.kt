package org.example.project.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.ScrollableState
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PickerType
import io.github.vinceglb.filekit.core.pickFile
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.icon_arrow
import kotlinproject.composeapp.generated.resources.icon_folder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.project.adb.ADB_ROOT
import org.example.project.adb.AdbExecutor
import org.example.project.adb.AdbFileExplorer
import org.example.project.adb.AdbFileExplorer.ROOT_PATH
import org.example.project.adb.ApkPushExecutor
import org.example.project.component.ColorDivider
import org.example.project.component.ColorGray
import org.example.project.component.ColorText
import org.example.project.component.ColorTheme
import org.example.project.component.ColorThemeHint
import org.example.project.component.PressedIndication
import org.example.project.component.RButton
import org.example.project.model.AndroidFileModel
import org.jetbrains.compose.resources.painterResource
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
    var childFileList by remember { mutableStateOf(List(0) { index -> "" }) }
    var pathList by remember { mutableStateOf(ArrayList<String>()) }

    val pushPickLauncher = rememberFilePickerLauncher(
        title = "Pick Push File",
    ) { directory ->
        CoroutineScope(Dispatchers.Default).launch {
            println(directory?.path)
            val path = directory?.path
            if (path?.isNotEmpty() == true && path != "null") {
                val androidPath = getPathFromList(pathList)
                ApkPushExecutor.push(path, androidPath, { result ->
                })
            }
        }
    }

    val pullPickLauncher = rememberDirectoryPickerLauncher { directory ->
        CoroutineScope(Dispatchers.Default).launch {
            println(directory?.path)
            val path = directory?.path
            if (path?.isNotEmpty() == true && path != "null") {
                val androidPath = getPathFromList(pathList)
                ApkPushExecutor.pull(androidPath, path) { result -> }
            }
        }
    }

    DisposableEffect(key1 = lifecycleOwner) {
        // 进入组件时执行，lifecycleOwner 改变后重新执行（先回调 onDispose）
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                pathList.add(ROOT_PATH)
                list(getPathFromList(pathList)) { childFiles ->
                    childFileList = childFiles
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
            "Device Explorer",
            fontSize = 30.sp,
            fontWeight = FontWeight(700),
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
        )
        Row(modifier = Modifier.padding(bottom = 5.dp)) {
            Text(
                "Path :",
                fontSize = 14.sp,
                lineHeight = 14.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
            )
            LazyRow {
                items(pathList.size,
                    itemContent = { index ->
                        Text(
                            lineHeight = 14.sp,
                            fontSize = 14.sp,
                            text = "/${pathList[index]}",
                            modifier = Modifier.clickable(
                            ) {
                                val rawPath = ArrayList<String>()
                                for (i in 0 until (index + 1)) {
                                    rawPath.add(pathList[i])
                                }
                                pathList = rawPath
                                list(getPathFromList(pathList)) { childFiles ->
                                    childFileList = childFiles
                                }
                            }.padding(top = 10.dp, bottom = 10.dp)
                        )
                    }
                )
            }
        }
        Spacer(
            modifier = Modifier.padding(end = 20.dp).fillMaxWidth().height(1.dp)
                .background(ColorDivider)
        )
        LazyColumn(modifier = Modifier.weight(1f).padding(start = 20.dp, end = 20.dp)) {
            println("list:" + childFileList.size)
            items(
                childFileList.size,
                key = { index -> ROOT_PATH + childFileList[index] },
                itemContent = { index ->
                    AndroidFileListItem(childFileList[index], getPathFromList(pathList), { name ->
                        AdbFileExplorer.checkIsDirectory("${getPathFromList(pathList)}/$name") { isDirectory ->
                            if (isDirectory) {
                                val rawPathList = ArrayList<String>()
                                rawPathList.addAll(pathList)
                                rawPathList.add(name)
                                pathList = rawPathList
                                list(getPathFromList(pathList)) { childFiles ->
                                    childFileList = childFiles
                                }
                            }
                        }
                    }, { name -> println("on long click " + name) })
                })
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(end = 20.dp, top = 10.dp)
        ) {
            RButton(onClick = {
                pushPickLauncher.launch()
            }, "Push")
            Spacer(modifier = Modifier.width(10.dp))
            RButton(onClick = {
                pullPickLauncher.launch()
            }, "Pull")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AndroidFileListItem(
    data: String,
    parent: String,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit
) {
    var isDir by remember { mutableStateOf(false) }
    AdbFileExplorer.checkIsDirectory("$parent/$data") { isDirectory ->
        isDir = isDirectory
    }
    Column(
        modifier = Modifier.clickable {
            onClick.invoke(data)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(start = 10.dp, top = 5.dp, bottom = 5.dp, end = 10.dp)
        ) {
            Text(
                text = data,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.wrapContentWidth().alpha(
                    if (isDir) {
                        1f
                    } else {
                        0f
                    }
                )
            ) {
                Image(
                    painter = painterResource(Res.drawable.icon_arrow),
                    "",
                    colorFilter = ColorFilter.tint(
                        ColorDivider
                    ),
                    modifier = Modifier.padding()
                        .height(26.dp)
                        .width(26.dp)
                )
            }
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(ColorGray))
    }
}

fun list(path: String, callback: (List<String>) -> Unit) {
    AdbFileExplorer.getFileList(path) { files ->
        callback.invoke(List(files.size) { index -> "${files[index]}" })
    }
}

fun getPathFromList(pathList: ArrayList<String>): String {
    var path = ""
    for (s in pathList) {
        path += "/$s"
    }
    return path
}
