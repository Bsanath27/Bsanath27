package com.skyrik.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skyrik.core.ui.theme.SkyrikTheme

/**
 * Skyrik button variants:
 *  - [SkyrikButton]         — Primary filled CTA
 *  - [SkyrikSecondaryButton]— Secondary outlined
 *  - [SkyrikGhostButton]    — Ghost / text-only
 */

@Composable
fun SkyrikButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    val spacing = SkyrikTheme.spacing
    val shapes = SkyrikTheme.shapes

    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        enabled = enabled && !isLoading,
        shape = shapes.shapeMedium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor   = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = SkyrikTheme.extendedColors.colorStateDisabled,
            disabledContentColor   = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        ),
        contentPadding = PaddingValues(horizontal = spacing.space_xl, vertical = spacing.space_sm),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation  = SkyrikTheme.elevation.level2,
            pressedElevation  = SkyrikTheme.elevation.level1,
            disabledElevation = SkyrikTheme.elevation.level0,
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun SkyrikSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val shapes = SkyrikTheme.shapes

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        enabled = enabled,
        shape = shapes.shapeMedium,
        border = BorderStroke(
            width = 1.5.dp,
            color = if (enabled) MaterialTheme.colorScheme.primary
                    else SkyrikTheme.extendedColors.colorStateDisabled,
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = SkyrikTheme.extendedColors.colorStateDisabled,
        ),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun SkyrikGhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = contentColor,
            disabledContentColor = SkyrikTheme.extendedColors.colorStateDisabled,
        ),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

// ─── Previews ────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF050B18)
@Composable
private fun SkyrikButtonPreview() {
    SkyrikTheme(darkTheme = true) {
        SkyrikButton(
            text = "Book a Flight",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050B18)
@Composable
private fun SkyrikButtonLoadingPreview() {
    SkyrikTheme(darkTheme = true) {
        SkyrikButton(
            text = "Confirming…",
            onClick = {},
            isLoading = true,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050B18)
@Composable
private fun SkyrikSecondaryButtonPreview() {
    SkyrikTheme(darkTheme = true) {
        SkyrikSecondaryButton(
            text = "View Details",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050B18)
@Composable
private fun SkyrikGhostButtonPreview() {
    SkyrikTheme(darkTheme = true) {
        SkyrikGhostButton(
            text = "Cancel",
            onClick = {},
        )
    }
}
