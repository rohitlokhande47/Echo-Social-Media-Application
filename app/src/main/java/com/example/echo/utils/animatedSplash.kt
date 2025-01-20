package com.example.echo.utils

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun ThreadsIconSplash(
    modifier: Modifier = Modifier,
    color: Color = Color.Black
) {
    // Main rotation animation
    val infiniteTransition = rememberInfiniteTransition()
    val mainRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Secondary pulse animation for the dots
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .size(200.dp)
            .background(Color.Black)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 3

            // Draw the main circular path
            drawCircle(
                color = color,
                radius = radius,
                style = Stroke(
                    width = 16f,  // Increased stroke width
                    cap = StrokeCap.Round
                )
            )

            rotate(degrees = mainRotation) {
                // Draw the rotating dots
                val dotRadius = 12f * scale  // Increased dot radius
                val dotCount = 8
                val angleStep = 360f / dotCount

                for (i in 0 until dotCount) {
                    val angle = i * angleStep
                    val x = center.x + radius * kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat()
                    val y = center.y + radius * kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat()

                    drawCircle(
                        color = color,
                        radius = dotRadius,
                        center = Offset(x, y)
                    )
                }

                // Draw inner spiral pattern
                val spiralSegments = 3
                val spiralRadius = radius * 0.6f

                for (i in 0 until spiralSegments) {
                    val startAngle = i * 120f
                    val sweepAngle = 90f

                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(
                            width = 16f,  // Increased stroke width
                            cap = StrokeCap.Round
                        ),
                        size = size.copy(
                            width = spiralRadius * 2,
                            height = spiralRadius * 2
                        ),
                        topLeft = Offset(
                            center.x - spiralRadius,
                            center.y - spiralRadius
                        )
                    )
                }
            }
        }
    }
}