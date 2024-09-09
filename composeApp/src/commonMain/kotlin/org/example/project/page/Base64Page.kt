package org.example.project.page

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
fun Base64Page() {
    Text(
        "Base 64",
        fontSize = 30.sp,
        fontWeight = FontWeight(700),
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
    )
}