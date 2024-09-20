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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import org.example.project.component.ColorDivider
import org.example.project.component.DimenDivider
import org.example.project.component.RButton
import org.example.project.component.RoundedCorner
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/29
 */
@Composable
@Preview
fun ApkPushPage() {
    var hyperCommPath by remember { mutableStateOf("") }
    var redTeaSignedApkPath by remember { mutableStateOf("") }

    val hyperCommApkPickLauncher = rememberFilePickerLauncher(
        title = "Pick Redtea Signed HyperComm Apk",
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
                redTeaSignedApkPath = path
            }
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
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(0.85f)
                .height(60.dp)
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
            text = "Pick HyperComm APK File:",
            fontSize = 14.sp,
            modifier = Modifier.padding(2.dp, 0.dp, 0.dp, 10.dp),
            textAlign = TextAlign.Start,
        )
        BasicTextField(
            value = if (redTeaSignedApkPath == "null") {
                ""
            } else {
                redTeaSignedApkPath
            },
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(0.85f)
                .height(60.dp)
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

        Row(modifier = Modifier.fillMaxWidth(0.85f), horizontalArrangement = Arrangement.End) {
            RButton(
                onClick = {
                    redTeaSignedApkPickLauncher.launch()
                }, "Pick Redtea Roaming APK"
            )
            Spacer(modifier = Modifier.width(20.dp))
            RButton(
                onClick = {
                    hyperCommApkPickLauncher.launch()
                }, "Pick HyperComm APK"
            )
        }

    }
}