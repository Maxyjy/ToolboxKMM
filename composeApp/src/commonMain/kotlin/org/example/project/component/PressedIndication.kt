package org.example.project.component

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope

/**
 *
 *
 * @author YangJianyu
 * @date 2024/9/23
 */
class PressedIndication(
    val radius: Float = 14f,
    val pressColor: Color = ColorTheme,
    val cornerRadius: CornerRadius = CornerRadius(radius, radius),
    val alpha: Float = 0.1f,
) : Indication {

    private inner class DefaultIndicationInstance(
        private val isPressed: State<Boolean>,
    ) : IndicationInstance {
        override fun ContentDrawScope.drawIndication() {
            drawContent()
            when {
                isPressed.value -> {
                    drawRoundRect(
                        cornerRadius = cornerRadius,
                        color = pressColor.copy(
                            alpha = alpha
                        ), size = size
                    )
                }
            }
        }
    }

    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        val isPressed = interactionSource.collectIsPressedAsState()
        return remember(interactionSource) {
            DefaultIndicationInstance(isPressed)
        }
    }
}