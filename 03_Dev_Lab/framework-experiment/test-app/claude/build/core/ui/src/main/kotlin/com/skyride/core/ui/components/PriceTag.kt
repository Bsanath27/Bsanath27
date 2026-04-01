package com.skyrik.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skyrik.core.ui.theme.SkyrikTheme

/**
 * PriceTag — renders a formatted price with optional label.
 *
 * @param amount      Pre-formatted price string (e.g. "₹ 18,000")
 * @param label       Optional sub-label (e.g. "per flight")
 * @param emphasized  When true, uses the primary/gold color for emphasis
 */
@Composable
fun PriceTag(
    amount: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    emphasized: Boolean = false,
) {
    val extColors = SkyrikTheme.extendedColors
    val amountColor = if (emphasized) MaterialTheme.colorScheme.primary
                      else MaterialTheme.colorScheme.onSurface

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
    ) {
        Text(
            text = amount,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = amountColor,
        )
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF050B18)
@Composable
private fun PriceTagPreview() {
    SkyrikTheme(darkTheme = true) {
        Column(modifier = Modifier.padding(16.dp)) {
            PriceTag(amount = "₹ 8,500", label = "per flight")
            PriceTag(amount = "₹ 18,000", label = "per flight", emphasized = true)
        }
    }
}
