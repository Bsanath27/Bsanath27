package com.skyrik.core.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skyrik.core.ui.theme.SkyrikTheme

/**
 * Shimmer effect modifier — animates a horizontal gradient sweep to indicate loading.
 *
 * Usage: `Modifier.shimmer()`
 */
fun Modifier.shimmer(
    baseColor: Color? = null,
    highlightColor: Color? = null,
): Modifier = composed {
    val base      = baseColor      ?: MaterialTheme.colorScheme.surfaceVariant
    val highlight = highlightColor ?: MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateX by transition.animateFloat(
        initialValue   = -300f,
        targetValue    = 1200f,
        animationSpec  = infiniteRepeatable(
            animation  = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerTranslate",
    )

    background(
        Brush.linearGradient(
            colors = listOf(base, highlight, base),
            start  = Offset(translateX, 0f),
            end    = Offset(translateX + 600f, 0f),
        )
    )
}

/**
 * Convenience composable for a single shimmer placeholder box.
 */
@Composable
fun LoadingShimmer(
    modifier: Modifier = Modifier,
    height: Dp = 16.dp,
) {
    Box(
        modifier = modifier
            .height(height)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = SkyrikTheme.shapes.shapeSmall,
            )
            .shimmer(),
    )
}

/**
 * A full HeliCard-shaped shimmer skeleton shown while ride options are loading.
 */
@Composable
fun HeliCardShimmer(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = SkyrikTheme.shapes.shapeMedium,
            )
            .shimmer(),
    )
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF050B18)
@Composable
private fun LoadingShimmerPreview() {
    SkyrikTheme(darkTheme = true) {
        Column(modifier = Modifier.padding(16.dp)) {
            LoadingShimmer(modifier = Modifier.fillMaxWidth(), height = 24.dp)
            Spacer(modifier = Modifier.height(8.dp))
            LoadingShimmer(modifier = Modifier.fillMaxWidth(0.6f), height = 16.dp)
            Spacer(modifier = Modifier.height(16.dp))
            HeliCardShimmer()
            Spacer(modifier = Modifier.height(12.dp))
            HeliCardShimmer()
        }
    }
}
