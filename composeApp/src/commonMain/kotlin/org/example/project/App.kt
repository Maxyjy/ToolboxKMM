package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import org.example.project.component.ColorDivider
import org.example.project.component.DimenDivider
import org.example.project.page.AdbControlPage
import org.example.project.page.DeviceExplorerPage
import org.example.project.page.ApkInspectPage
import org.example.project.page.Base64Page
import org.example.project.page.JsonFormatPage
import org.example.project.page.LogProcessPage
import org.example.project.page.MccChangePage
import org.example.project.page.PerformancePage
import org.example.project.page.RedTeaApkPushPage
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.example.project.component.SideBar

@Composable
@Preview
fun App() {

    MaterialTheme {
        var rightPanelIndex by remember { mutableStateOf(0) }
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
                    .background(Color(0xfff7f7f7))
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
                        MccChangePage()
                    }

                    3 -> {
                        ApkInspectPage()
                    }

                    4 -> {
                        RedTeaApkPushPage()
                    }

                    5 -> {
                        DeviceExplorerPage()
                    }

                    6 -> {
                        PerformancePage()
                    }

                    7 -> {
                        JsonFormatPage()
                    }

                    8 -> {
                        Base64Page()
                    }
                }
            }
        }
    }
}