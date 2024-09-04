package org.example.project.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.icon_adb
import kotlinproject.composeapp.generated.resources.icon_apk
import kotlinproject.composeapp.generated.resources.icon_app_inspect
import kotlinproject.composeapp.generated.resources.icon_app_logo
import kotlinproject.composeapp.generated.resources.icon_base_64
import kotlinproject.composeapp.generated.resources.icon_file_explorer
import kotlinproject.composeapp.generated.resources.icon_json_format
import kotlinproject.composeapp.generated.resources.icon_log_merge
import kotlinproject.composeapp.generated.resources.icon_mcc_change
import kotlinproject.composeapp.generated.resources.icon_performance
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
@Composable
@Preview
fun SideBar(onIndexChangeListener: (Int) -> Unit) {
    var selectedIndex by remember { mutableStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(modifier = Modifier.height(70.dp).padding(vertical = 15.dp)) {
            Image(
                painter = painterResource(Res.drawable.icon_app_logo),
                "app logo",
                modifier = Modifier.height(40.dp).width(40.dp),
            )
        }

        val onItemClickListener: (Int) -> Unit = { index ->
            selectedIndex = index
            onIndexChangeListener.invoke(index)
        }

        MenuItem(0, "ADB Execute", Res.drawable.icon_adb, onItemClickListener, selectedIndex)
        MenuItem(1, "Log Merge", Res.drawable.icon_log_merge, onItemClickListener, selectedIndex)
        MenuItem(2, "Mcc Change", Res.drawable.icon_mcc_change, onItemClickListener, selectedIndex)
        MenuItem(3, "APK Inspect", Res.drawable.icon_app_inspect, onItemClickListener, selectedIndex)
        MenuItem(4, "APK Push", Res.drawable.icon_apk, onItemClickListener, selectedIndex)
        MenuItem(5, "File Explorer", Res.drawable.icon_file_explorer, onItemClickListener, selectedIndex)
        MenuItem(6, "Performance", Res.drawable.icon_performance, onItemClickListener, selectedIndex)
        MenuItem(7, "Json Format", Res.drawable.icon_json_format, onItemClickListener, selectedIndex)
        MenuItem(8, "Base64", Res.drawable.icon_base_64, onItemClickListener, selectedIndex)
    }
}

@Composable
fun MenuItem(
    index: Int,
    text: String,
    icon: DrawableResource,
    onItemClickListener: (Int) -> Unit,
    selectedMenuItemIndex: Int
) {
    val isSelect = selectedMenuItemIndex == index
    fun onMenuClick(index: Int) {
        onItemClickListener.invoke(index)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = {
                    onMenuClick(index)
                },
            ).padding(vertical = 4.dp, horizontal = 4.dp)
            .background(
                if (isSelect) {
                    Color(0xfffceaeb)
                } else {
                    Color(0x000000)
                }, RoundedCornerShape(10)
            ).padding(vertical = 6.dp, horizontal = 0.dp),
    ) {
        MenuIcon(icon, text, isSelect)
        MenuText(text, isSelect)
    }
}

@Composable
fun MenuIcon(icon: DrawableResource, contentDesc: String, isSelected: Boolean) {
    Image(
        painter = painterResource(icon),
        contentDesc,
        colorFilter = ColorFilter.tint(
            if (isSelected) {
                ColorTheme
            } else {
                ColorText
            }
        ),
        modifier = Modifier.height(20.dp).width(20.dp),
    )
}

@Composable
fun MenuText(text: String, isSelected: Boolean) {
    Text(
        text,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        lineHeight = TextUnit(15f, TextUnitType.Sp),
        fontSize = 9.sp,
        fontWeight = FontWeight(500),
        color = if (isSelected) {
            ColorTheme
        } else {
            ColorText
        }
    )
}