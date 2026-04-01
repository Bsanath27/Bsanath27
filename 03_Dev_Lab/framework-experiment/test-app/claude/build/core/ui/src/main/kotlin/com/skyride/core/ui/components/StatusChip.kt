package com.skyrik.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skyrik.core.ui.theme.SkyrikTheme

/**
 * StatusChip — small pill label to denote category, status, or tier.
 *
 * @param label     Display text
 * @param isActive  When true, uses the primary/selected color scheme
 * @param dotColor  Optional leading status dot color (e.g. live tracking green)
 */
@Composable
fun StatusChip(
    label: String,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    dotColor: Color? = null,
) {
    val shapes   = SkyrikTheme.shapes
    val spacing  = SkyrikTheme.spacing
    val extColors = SkyrikTheme.extendedColors

    val bgColor   = if (isActive) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = modifier
            .background(color = bgColor, shape = shapes.shapeCircle)
            .padding(horizontal = spacing.space_sm, vertical = spacing.space_xxs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.space_xs),
    ) {
        if (dotColor != null) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(color = dotColor, shape = shapes.shapeCircle),
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
        )
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF050B18)
@Composable
private fun StatusChipPreview() {
    SkyrikTheme(darkTheme = true) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            StatusChip(label = "Standard")
            StatusChip(label = "Executive", isActive = true)
            StatusChip(
                label = "Live",
                dotColor = SkyrikTheme.extendedColors.colorLiveTracking,
            )
        }
    }
}
