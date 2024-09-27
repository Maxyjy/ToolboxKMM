package org.example.project

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension

fun main() = application {

    val width = if (getSystemName().contains("mac", true)) {
        850
    } else {
        1000
    }
    val height = if (getSystemName().contains("mac", true)) {
        750
    } else {
        850
    }
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = width.dp, height = height.dp),
        title = "Toolbox",
    ) {
        window.minimumSize = Dimension(width, height)
        ApplicationComponent.init()
        App()
    }
}