package org.example.project

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.icon_app_icns_win
import org.jetbrains.compose.resources.painterResource
import java.awt.Dimension

fun main() = application {

    val width = if (getSystemName().contains("mac", true)) {
        850
    } else {
        1000
    }
    val height = if (getSystemName().contains("mac", true)) {
        800
    } else {
        850
    }
    Window(
        icon = painterResource(Res.drawable.icon_app_icns_win),
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = width.dp, height = height.dp),
        title = "Toolbox",
    ) {
        window.minimumSize = Dimension(width, height)
        App()
    }
}