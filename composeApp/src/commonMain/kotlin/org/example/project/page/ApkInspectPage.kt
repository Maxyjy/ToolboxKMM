package org.example.project.page

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
@Composable
@Preview
fun ApkInspectPage() {
    var bottomBoxText by remember { mutableStateOf("Click me\nusing LMB or\nRMB + Alt") }
    var bottomBoxCount by remember { mutableStateOf(0) }
    val interactionSource = remember { MutableInteractionSource() }

    Text(
        "Apk Inspect",
        fontSize = 30.sp,
        fontWeight = FontWeight(700),
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
    )
}