package com.skyrik.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skyrik.core.ui.theme.SkyrikTheme

/**
 * HeliCard — displays a helicopter option in the ride-selection list.
 *
 * @param name           Helicopter model name
 * @param category       Category label (e.g. "Standard", "Executive", "VIP")
 * @param seats          Passenger capacity
 * @param speedKmh       Cruising speed
 * @param priceFormatted Formatted price string (e.g. "₹ 12,500")
 * @param eta            ETA string (e.g. "~18 min")
 * @param isSelected     Whether this card is currently selected
 * @param onClick        Selection callback
 */
@Composable
fun HeliCard(
    name: String,
    category: String,
    seats: Int,
    speedKmh: Int,
    priceFormatted: String,
    eta: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = SkyrikTheme.spacing
    val shapes = SkyrikTheme.shapes
    val extColors = SkyrikTheme.extendedColors

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) extColors.colorStateSelected
                      else MaterialTheme.colorScheme.outlineVariant,
        animationSpec = tween(durationMillis = 250),
        label = "HeliCardBorder",
    )

    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                      else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(durationMillis = 250),
        label = "HeliCardContainer",
    )

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = shapes.shapeMedium,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = borderColor,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) SkyrikTheme.elevation.level3
                               else SkyrikTheme.elevation.level1,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.space_base),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Helicopter icon placeholder
            Icon(
                imageVector = Icons.Default.AirplanemodeActive,
                contentDescription = name,
                modifier = Modifier.size(48.dp),
                tint = if (isSelected) extColors.colorStateSelected
                       else MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.width(spacing.space_base))

            // Name + specs
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(spacing.space_xs))
                StatusChip(label = category, isActive = isSelected)
                Spacer(modifier = Modifier.height(spacing.space_sm))

                // Specs row
                Row(horizontalArrangement = Arrangement.spacedBy(spacing.space_md)) {
                    SpecBadge(
                        icon = Icons.Default.Group,
                        value = "$seats",
                        contentDescription = "$seats seats",
                    )
                    SpecBadge(
                        icon = Icons.Default.Speed,
                        value = "${speedKmh}km/h",
                        contentDescription = "$speedKmh km/h",
                    )
                }
            }

            Spacer(modifier = Modifier.width(spacing.space_sm))

            // Price + ETA
            Column(horizontalAlignment = Alignment.End) {
                PriceTag(amount = priceFormatted, emphasized = isSelected)
                Spacer(modifier = Modifier.height(spacing.space_xs))
                Text(
                    text = eta,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF050B18)
@Composable
private fun HeliCardPreview() {
    SkyrikTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HeliCard(
                name = "Robinson R44",
                category = "Standard",
                seats = 3,
                speedKmh = 185,
                priceFormatted = "₹ 8,500",
                eta = "~22 min",
                isSelected = false,
                onClick = {},
            )
            HeliCard(
                name = "Bell 407",
                category = "Executive",
                seats = 6,
                speedKmh = 245,
                priceFormatted = "₹ 18,000",
                eta = "~15 min",
                isSelected = true,
                onClick = {},
            )
        }
    }
}
