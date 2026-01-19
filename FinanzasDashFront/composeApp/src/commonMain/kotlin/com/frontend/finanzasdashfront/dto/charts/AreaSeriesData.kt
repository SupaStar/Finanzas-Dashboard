package com.frontend.finanzasdashfront.dto.charts

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.koalaplot.core.xygraph.Point

data class AreaSeriesData(
    val name: String,
    val points: List<Point<Float, Float>>,
    val color: Color,
    val alpha: Float = 0.5f,
    val strokeWidth: androidx.compose.ui.unit.Dp = 2.dp
){
    val minX: Float = points.minOf { it.x }
    val maxX: Float = points.maxOf { it.x }
    val maxY: Float = points.maxOf { it.y }
}