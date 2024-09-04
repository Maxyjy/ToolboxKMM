package org.example.project.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/30
 */
val ColorText = Color(0xe6000000)
val ColorTheme = Color(0xffe6373d)
val ColorDivider = Color(0xffdcdcdc)

val DimenDivider = 0.5.dp

@Composable
fun RButton(onClick: () -> Unit, text: String, enable: Boolean = true) {
    Button(
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp,
        ),
        enabled = enable,
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(
            ColorTheme,
            contentColor = Color.White
        ),
        onClick = {
            onClick.invoke()
        }) {
        RText(text)
    }
}

@Composable
fun RText(text: String) {
    Text(
        fontFamily = FontFamily.Default,
        fontStyle = FontStyle.Normal,
        text = text,
    )
}