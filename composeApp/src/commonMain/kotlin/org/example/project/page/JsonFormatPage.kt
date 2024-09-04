package org.example.project.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.util.JsonFormatUtil
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
@Composable
@Preview
fun JsonFormatPage() {
    var text by remember { mutableStateOf("") }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "JSON Data",
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth(0.8f),
            textAlign = TextAlign.Start,
        )
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            BasicTextField(
                modifier = Modifier.fillMaxWidth(0.8f)
                    .heightIn(300.dp, 600.dp)
                    .background(Color.Gray, RoundedCornerShape(2))
                    .padding(15.dp),
                value = text,
                onValueChange = {
                    text = it
                })
        }
        Button(
            onClick = {
                try {
                    text = JsonFormatUtil.format(text)
                } catch (e: Exception) {
                    text = "Something wrong with your json"
                }
            }
        ) {
            Text("Format")
        }
    }

}

