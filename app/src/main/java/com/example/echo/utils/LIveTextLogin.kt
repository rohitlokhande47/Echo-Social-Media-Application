import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echo.ui.theme.staatiches
import kotlinx.coroutines.delay

@Composable
@Preview
fun EnhancedTypingAnimation() {
    val sentences = listOf(
        "Express freely, connect deeply.",
        "Share stories, spark connections.",
        "Speak boldly, inspire others.",
        "Echo"
    )

    var displayedText by remember { mutableStateOf("") }
    var currentSentenceIndex by remember { mutableStateOf(0) }
    var isAddingText by remember { mutableStateOf(true) }
    var isAnimationComplete by remember { mutableStateOf(false) }

    // Enhanced animations
    val infiniteTransition = rememberInfiniteTransition()

    // Cursor animation with subtle fade only
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Text scale animation with subtle fade in
    val textAlpha = remember { Animatable(1f) }

    LaunchedEffect(currentSentenceIndex, isAddingText) {
        if (isAnimationComplete) return@LaunchedEffect

        val sentence = sentences[currentSentenceIndex]
        if (isAddingText) {
            for (i in 1..sentence.length) {
                displayedText = sentence.substring(0, i)
                delay(100)
            }
            delay(1000)

            // Check if we're at the last word
            if (currentSentenceIndex == sentences.lastIndex) {
                isAnimationComplete = true
                return@LaunchedEffect
            }

            isAddingText = false
        } else {
            for (i in sentence.length downTo 0) {
                displayedText = sentence.substring(0, i)
                delay(50)
            }
            delay(500)
            currentSentenceIndex += 1
            isAddingText = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            ,
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(2.dp)
            ) {
                Text(
                    text = displayedText,
                    fontSize = 60.sp,
                    fontFamily = staatiches,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.alpha(textAlpha.value),
                    lineHeight = 56.sp
                )

                // Only show cursor if animation isn't complete
                if (!isAnimationComplete || displayedText == "Echo") {
                    Box(
                        modifier = Modifier
                            .height(52.dp)  // Match the text height
                            .width(3.dp)
                            .alpha(cursorAlpha)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}