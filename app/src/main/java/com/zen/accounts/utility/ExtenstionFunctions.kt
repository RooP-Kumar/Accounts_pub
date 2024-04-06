package com.zen.accounts.utility

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.platform.inspectable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.shadowColor

@Composable
fun Modifier.customShadow(
    elevation: Dp = generalPadding,
    shape: Shape = RoundedCornerShape(generalPadding),
    clip: Boolean = elevation > 0.dp,
    ambientColor: Color = shadowColor,
    spotColor: Color = shadowColor,
) = if (elevation > 0.dp || clip) {
    inspectable(
        inspectorInfo = debugInspectorInfo {
            name = "shadow"
            properties["elevation"] = elevation
            properties["shape"] = shape
            properties["clip"] = clip
            properties["ambientColor"] = ambientColor
            properties["spotColor"] = spotColor
        }
    ) {
        graphicsLayer {
            this.shadowElevation = elevation.toPx()
            this.shape = shape
            this.clip = clip
            this.ambientShadowColor = ambientColor
            this.spotShadowColor = spotColor
        }
    }
} else {
    this
}

@Composable
fun Modifier.customShadowTwo(
    color : Color = shadowColor.copy(alpha = 0.5f),
    offsetX : Dp = (-1).dp,
    offsetY : Dp = 1.dp,
    radiusX : Dp = 0.dp,
    radiusY : Dp = 0.dp,
    blur : Dp = halfGeneralPadding
) = then(
    drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameWorkPaint = paint.asFrameworkPaint()
            if (blur != 0.dp) {
                frameWorkPaint.maskFilter = (BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL))
            }
            frameWorkPaint.color = color.toArgb()
            val leftPixel = offsetX.toPx()
            val topPixel = offsetY.toPx()
            val rightPixel = size.width + -1*leftPixel
            val bottomPixel = size.height + topPixel

            if (radiusX != 0.dp && radiusY != 0.dp) {
                canvas.drawRoundRect(
                    leftPixel,
                    -1*topPixel,
                    rightPixel,
                    bottomPixel,
                    radiusX.toPx(),
                    radiusY.toPx(),
                    paint
                )
            } else {
                canvas.drawRect(
                    leftPixel,
                    -1*topPixel,
                    rightPixel,
                    bottomPixel,
                    paint
                )
            }
        }
    }
)