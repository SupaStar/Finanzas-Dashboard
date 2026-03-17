package com.frontend.finanzasdashfront.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeRevealItem(
    modifier: Modifier = Modifier,
    maxSwipeDp: Float = 140f,
    actions: @Composable RowScope.() -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val maxSwipePx = with(density) { -maxSwipeDp.dp.toPx() }

    val offsetXAnim = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        // Background Actions
        Row(
            modifier = Modifier
                .matchParentSize()
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions()
        }

        // Foreground Content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetXAnim.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            coroutineScope.launch {
                                val target = (offsetXAnim.value + dragAmount).coerceIn(maxSwipePx, 0f)
                                offsetXAnim.snapTo(target)
                            }
                        },
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetXAnim.value < maxSwipePx / 2) {
                                    offsetXAnim.animateTo(maxSwipePx)
                                } else {
                                    offsetXAnim.animateTo(0f)
                                }
                            }
                        }
                    )
                }
        ) {
            content()
        }
    }
}
