package com.zen.accounts.utility

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.platform.inspectable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zen.accounts.ui.navigation.Screen
import com.zen.accounts.ui.theme.background
import com.zen.accounts.ui.theme.generalPadding
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
fun Modifier.customClickable(
    showMenu : MutableState<Boolean>,
    onClick : () -> Unit
) : Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return this.then(Modifier
        .clickable(
            interactionSource = interactionSource,
            indication = if (showMenu.value) null else LocalIndication.current
        ) {
            if(!showMenu.value) {
                onClick()
            } else {
                showMenu.value = !showMenu.value
            }
        }
    )
}

@Composable
fun Modifier.generalBorder(
    color: Color = shadowColor,
    borderRadiusX: Dp = generalPadding,
    borderRadiusY: Dp = generalPadding,
    width: Dp = 0.5.dp,
    backgroundColor: Color = background
): Modifier = this
    .drawBehind {

        /**
         * Please use it before background, otherwise it will not work properly.
         * */

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
                borderRadiusX.toPx(),
                borderRadiusY.toPx(),
                paint
            )
        }
    }
    .clip(shape = RoundedCornerShape(generalPadding))
    .background(backgroundColor)

@Composable
fun Modifier.generalCircleBorder(
    size: Dp = 0.5.dp,
    color: Color = shadowColor,
    backgroundColor : Color = background
): Modifier = this
    .drawBehind {

        /**
         * Please use it before background, otherwise it will not work properly.
         * */

        val shadowColor = android.graphics.Color.toArgb(color.value.toLong())
        val radius = size.toPx() / 2
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = shadowColor
            it.drawCircle(
                Offset(radius, radius),
                radius + 2,
                paint
            )
        }
    }
    .clip(CircleShape)
    .background(backgroundColor)

fun Context.toast(msg : String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}
