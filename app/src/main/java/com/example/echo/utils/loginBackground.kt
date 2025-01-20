import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.*

@Composable
@Preview
fun WaveBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val wavePeriod = canvasWidth / 4
        val amplitude = canvasWidth / 15
        val lineSpacing = 40f

        rotate(25f) {
            translate(left = -canvasWidth / 2, top = -canvasHeight / 2) {
                repeat(((canvasWidth + canvasHeight) / lineSpacing).toInt()) { index ->
                    val path = Path().apply {
                        moveTo(-canvasWidth / 2, index * lineSpacing)
                        for (x in (-canvasWidth / 2).toInt()..(canvasWidth * 1.5).toInt()) {
                            val y = index * lineSpacing +
                                    sin(x / wavePeriod + phase + index * 0.2f) * amplitude
                            lineTo(x.toFloat(), y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = Color(0xFF424242),
                        style = Stroke(
                            width = 2.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }
            }
        }
    }
}