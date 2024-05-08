package com.zen.accounts.utility

import android.content.Context
import android.graphics.BlurMaskFilter
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.platform.inspectable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zen.accounts.ui.theme.generalPadding
import com.zen.accounts.ui.theme.halfGeneralPadding
import com.zen.accounts.ui.theme.normalPadding
import com.zen.accounts.ui.theme.onBackground
import com.zen.accounts.ui.theme.roundedCornerShape
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
fun Modifier.generalBorder(
    color: Color = shadowColor,
    borderRadius: Dp = generalPadding,
    width: Dp = 0.5.dp,
): Modifier = this.drawBehind {
    val shadowColor = android.graphics.Color.toArgb(color.value.toLong())
    val widthPx = width.toPx()
    this.drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = shadowColor
        it.drawRoundRect(
            -1 * widthPx,
            -1 * widthPx,
            this.size.width + widthPx,
            this.size.height + widthPx,
            borderRadius.toPx(),
            borderRadius.toPx(),
            paint
        )
    }
}.clip(shape = RoundedCornerShape(generalPadding))


fun Context.toast(msg : String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}