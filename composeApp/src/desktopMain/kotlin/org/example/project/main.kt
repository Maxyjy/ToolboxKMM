package org.example.project

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.icon_adb
import org.jetbrains.compose.resources.painterResource
import java.awt.Dimension

fun main() = application {
    Window(
        icon = painterResource(Res.drawable.icon_adb),
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = 1000.dp, height = 700.dp),
        title = "Tool",
    ) {
        window.minimumSize = Dimension(1000, 700)
        App()
    }
}