package org.example.project.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.icon_folder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.project.adb.ADB_ROOT
import org.example.project.adb.AdbFileExplorer
import org.example.project.adb.AdbFileExplorer.ROOT_PATH
import org.example.project.component.ColorDivider
import org.example.project.component.ColorTheme
import org.example.project.component.PressedIndication
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
        Row(modifier = Modifier.padding(bottom = 10.dp)) {
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
        LazyColumn() {
            println("list:" + childFileList.size)
            items(
                childFileList.size,
                key = { index -> ROOT_PATH + childFileList[index] },
                itemContent = { index ->
                    AndroidFileListItem(childFileList[index], getPathFromList(pathList)) { name ->
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
                    }
                })
        }
    }
}

@Composable
fun AndroidFileListItem(data: String, parent: String, onClick: (String) -> Unit) {
    var isDir by remember { mutableStateOf(false) }
    AdbFileExplorer.checkIsDirectory("$parent/$data") { isDirectory ->
        isDir = isDirectory
    }
    Column(modifier = Modifier.clickable {
        onClick.invoke(data)
    }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
        ) {
            if (isDir) {
                Image(
                    painter = painterResource(Res.drawable.icon_folder),
                    "pick file",
                    colorFilter = ColorFilter.tint(
                        ColorTheme
                    ),
                    modifier = Modifier.padding(end = 8.dp)
                        .height(26.dp)
                        .width(26.dp).padding(3.dp)
                )
            }
            Text(
                data, fontSize = 14.sp, lineHeight = 14.sp
            )
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(ColorDivider))
    }
}

fun list(path: String, callback: (List<String>) -> Unit) {
    AdbFileExplorer.getFileList(path) { files ->
//        for ((index, fileName) in files.withIndex()) {
//            println("child file:[${fileName}]")
//        }
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
