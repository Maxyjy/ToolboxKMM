package org.example.project.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.icon_folder
import kotlinproject.composeapp.generated.resources.icon_kotlin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.project.component.ColorDisable
import org.example.project.component.ColorDivider
import org.example.project.component.ColorText
import org.example.project.component.ColorTheme
import org.example.project.component.DimenDivider
import org.example.project.component.PressedIndication
import org.example.project.component.RButton
import org.example.project.component.RoundedCorner
import org.example.project.util.AppPreferencesKey
import org.example.project.util.SettingsDelegate
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun SettingsPage(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {

    var androidHomePath by remember { mutableStateOf("") }
    var scrcpyPath by remember { mutableStateOf("") }

    val androidHomePathPickLauncher = rememberDirectoryPickerLauncher(
        title = "Pick Android Home Path",
    ) { directory ->
        CoroutineScope(Dispatchers.Default).launch {
            println(directory?.path)
            val path = directory?.path
            if (path?.isNotEmpty() == true && path != "null") {
                androidHomePath = path
            }
        }
    }

    val scrcpyPathPickLauncher = rememberDirectoryPickerLauncher(
        title = "Pick Scrcpy Path",
    ) { directory ->
        CoroutineScope(Dispatchers.Default).launch {
            println(directory?.path)
            val path = directory?.path
            if (path?.isNotEmpty() == true && path != "null") {
                scrcpyPath = path
            }
        }
    }


    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                CoroutineScope(Dispatchers.Default).launch {
                    androidHomePath =
                        SettingsDelegate.getString(
                            AppPreferencesKey.ANDROID_HOME_PATH
                        ).toString()
                    scrcpyPath =
                        SettingsDelegate.getString(
                            AppPreferencesKey.SCRCPY_HOME_PATH
                        ).toString()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Settings",
                fontSize = 30.sp,
                fontWeight = FontWeight(700),
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
            )

            Text(
                text = "Android Home Path :",
                fontSize = 14.sp,
                modifier = Modifier.padding(2.dp, 0.dp, 0.dp, 10.dp),
                textAlign = TextAlign.Start,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(1f)
                    .height(55.dp)
                    .padding(0.dp, 0.dp, 0.dp, 10.dp)
                    .border(
                        DimenDivider,
                        color = ColorDivider,
                        shape = RoundedCornerShape(RoundedCorner)
                    ).background(
                        Color.White,
                        RoundedCornerShape(RoundedCorner)
                    )
            ) {
                BasicTextField(
                    value = if (androidHomePath == "null") {
                        ""
                    } else {
                        androidHomePath
                    },
                    onValueChange = {
                        androidHomePath = it
                        CoroutineScope(Dispatchers.Default).launch {
                            SettingsDelegate.putString(
                                AppPreferencesKey.ANDROID_HOME_PATH, androidHomePath
                            )
                        }
                    },
                    modifier = Modifier.weight(1f)
                        .padding(start = 15.dp, top = 10.dp, end = 15.dp, bottom = 10.dp)
                )
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.icon_folder),
                        "pick file",
                        colorFilter = ColorFilter.tint(
                            ColorTheme
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                            .height(26.dp)
                            .width(26.dp).clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = PressedIndication(8f)
                            ) {
                                androidHomePathPickLauncher.launch()
                            }.padding(3.dp)
                    )
                }
            }
        }
//        Text(
//            text = "Scrcpy Path :",
//            fontSize = 14.sp,
//            modifier = Modifier.padding(2.dp, 0.dp, 0.dp, 10.dp),
//            textAlign = TextAlign.Start,
//        )
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.fillMaxWidth(1f)
//                .height(55.dp)
//                .padding(0.dp, 0.dp, 0.dp, 10.dp)
//                .border(
//                    DimenDivider,
//                    color = ColorDivider,
//                    shape = RoundedCornerShape(RoundedCorner)
//                ).background(
//                    Color.White,
//                    RoundedCornerShape(RoundedCorner)
//                )
//        ) {
//            BasicTextField(
//                value = if (scrcpyPath == "null") {
//                    ""
//                } else {
//                    scrcpyPath
//                },
//                onValueChange = {
//                    scrcpyPath = it
//                    CoroutineScope(Dispatchers.Default).launch {
//                        SettingsDelegate.putString(
//                            AppPreferencesKey.SCRCPY_HOME_PATH, scrcpyPath
//                        )
//                    }
//                },
//                modifier = Modifier.weight(1f)
//                    .padding(start = 15.dp, top = 10.dp, end = 15.dp, bottom = 10.dp)
//            )
//            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
//                Image(
//                    painter = painterResource(Res.drawable.icon_folder),
//                    "pick file",
//                    colorFilter = ColorFilter.tint(
//                        ColorTheme
//                    ),
//                    modifier = Modifier.padding(end = 8.dp)
//                        .height(26.dp)
//                        .width(26.dp).clickable(
//                            interactionSource = MutableInteractionSource(),
//                            indication = PressedIndication(8f)
//                        ) {
//                            scrcpyPathPickLauncher.launch()
//                        }.padding(3.dp)
//                )
//            }
//        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
            Column {
                Text(
                    text = "Efficient ADB 1.2.1",
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier,
                    color = ColorText,
                    textAlign = TextAlign.Start,
                )
                Text(
                    text = "Powered by",
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    modifier = Modifier.padding(top = 10.dp),
                    color = ColorDisable,
                    textAlign = TextAlign.Start,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 5.dp)
            ) {
                Image(
                    modifier = Modifier.width(14.dp).padding(bottom = 0.dp),
                    contentDescription = "kotlin icon",
                    painter = painterResource(Res.drawable.icon_kotlin)
                )
                Text(
                    text = "Kotlin MultiPlatform",
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    color = ColorText,
                    modifier = Modifier.padding(start = 5.dp),
                    textAlign = TextAlign.Start,
                )
            }
        }
    }

}