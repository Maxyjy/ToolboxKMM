package org.example.project

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.icon_adb
import kotlinproject.composeapp.generated.resources.icon_app_logo
import kotlinproject.composeapp.generated.resources.icon_app_logo_with_background
import org.jetbrains.compose.resources.painterResource
import java.awt.Dimension

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = 850.dp, height = 700.dp),
        title = "Toolbox",
    ) {
        window.minimumSize = Dimension(850, 700)
        ApplicationComponent.init()
        App()
    }
}