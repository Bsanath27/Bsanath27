package com.skyrik.feature.confirmation.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skyrik.core.ui.components.SkyrikButton
import com.skyrik.core.ui.components.SkyrikGhostButton
import com.skyrik.core.ui.components.StatusChip
import com.skyrik.core.ui.theme.SkyrikTheme
import com.skyrik.feature.booking.ui.MapPlaceholder
import kotlinx.coroutines.delay

/**
 * TrackingScreen — live ride tracking view.
 *
 * Layout:
 *  - Full-screen map (placeholder; Google Maps SDK at runtime)
 *  - Pulsing helicopter icon overlay
 *  - Bottom sheet with:
 *    - Status chip + ETA countdown
 *    - Progress indicator (flight stages)
 *    - Pilot info row
 *    - Cancel button
 */
@Composable
fun TrackingScreen(
    bookingRef: String,
    onCancelRide: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = SkyrikTheme.spacing
    val shapes  = SkyrikTheme.shapes

    // Simulated tracking state — replaced by real Flow from BookingRepository
    var etaMinutes by remember { mutableIntStateOf(22) }
    var statusLabel by remember { mutableStateOf("En route to pickup") }
    var flightProgress by remember { mutableStateOf(0f) }

    // Countdown tick every minute (simulated)
    LaunchedEffect(Unit) {
        val stages = listOf(
            Triple("En route to pickup", 0.1f, 18),
            Triple("Pilot at pickup", 0.3f, 14),
            Triple("In flight", 0.6f, 8),
            Triple("Approaching destination", 0.85f, 3),
            Triple("Landed — welcome!", 1.0f, 0),
        )
        for ((status, progress, eta) in stages) {
            delay(4000L)
            statusLabel = status
            flightProgress = progress
            etaMinutes = eta
        }
    }

    // Helicopter rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "heliRotate")
    val rotorAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue  = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "rotorAngle",
    )

    Box(modifier = modifier.fillMaxSize()) {

        // ── Full-screen map ───────────────────────────────────────────────────
        MapPlaceholder(modifier = Modifier.fillMaxSize())

        // ── Helicopter position icon ──────────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = SkyrikTheme.shapes.shapeCircle,
                ),
            contentAlignment = Alignment.Center,
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.AirplanemodeActive,
                contentDescription = "Helicopter position",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(32.dp)
                    .rotate(rotorAngle * 0.1f), // slight wobble to simulate movement
            )
        }

        // ── Status bar (top overlay) ──────────────────────────────────────────
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(spacing.space_base)
                .align(Alignment.TopStart),
        ) {
            StatusChip(
                label = "LIVE",
                dotColor = SkyrikTheme.extendedColors.colorLiveTracking,
                isActive = true,
            )
        }

        // ── Bottom tracking sheet ─────────────────────────────────────────────
        TrackingBottomSheet(
            bookingRef   = bookingRef,
            etaMinutes   = etaMinutes,
            statusLabel  = statusLabel,
            flightProgress = flightProgress,
            onCancelRide = onCancelRide,
            modifier     = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun TrackingBottomSheet(
    bookingRef: String,
    etaMinutes: Int,
    statusLabel: String,
    flightProgress: Float,
    onCancelRide: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = SkyrikTheme.spacing
    val shapes  = SkyrikTheme.shapes

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = shapes.shapeTopRounded,
            )
            .navigationBarsPadding()
            .padding(spacing.space_xl),
    ) {
        // Drag handle
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .align(Alignment.CenterHorizontally)
                .background(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = shapes.shapeCircle,
                )
        )

        Spacer(modifier = Modifier.height(spacing.space_lg))

        // Status + ETA
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = statusLabel,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Booking · $bookingRef",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            ETABadge(etaMinutes = etaMinutes)
        }

        Spacer(modifier = Modifier.height(spacing.space_base))

        // Flight progress bar
        Text(
            text = "Flight progress",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(spacing.space_xs))
        LinearProgressIndicator(
            progress = { flightProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round,
        )

        Spacer(modifier = Modifier.height(spacing.space_xl))

        // Pilot info
        PilotInfoRow(
            pilotName = "Capt. Priya Sharma",
            rating = 4.9f,
            helicopterName = "Bell 407",
        )

        Spacer(modifier = Modifier.height(spacing.space_xl))

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.space_sm),
        ) {
            SkyrikButton(
                text = "Contact Pilot",
                onClick = { /* Deep-link to dialer */ },
                modifier = Modifier.weight(1f),
            )
            SkyrikGhostButton(
                text = "Cancel",
                onClick = onCancelRide,
                contentColor = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(0.5f),
            )
        }
    }
}

@Composable
private fun ETABadge(etaMinutes: Int) {
    val spacing = SkyrikTheme.spacing
    val shapes  = SkyrikTheme.shapes

    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = shapes.shapeMedium,
            )
            .padding(horizontal = spacing.space_md, vertical = spacing.space_sm),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = if (etaMinutes == 0) "Arrived" else "$etaMinutes min",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        if (etaMinutes > 0) {
            Text(
                text = "ETA",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
            )
        }
    }
}

@Composable
private fun PilotInfoRow(
    pilotName: String,
    rating: Float,
    helicopterName: String,
    modifier: Modifier = Modifier,
) {
    val spacing = SkyrikTheme.spacing

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = SkyrikTheme.shapes.shapeCircle,
                ),
            contentAlignment = Alignment.Center,
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(modifier = Modifier.width(spacing.space_md))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = pilotName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "$helicopterName  •  ⭐ $rating",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        StatusChip(
            label = "Verified",
            isActive = true,
        )
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF050B18, device = "spec:width=411dp,height=891dp")
@Composable
private fun TrackingBottomSheetPreview() {
    SkyrikTheme(darkTheme = true) {
        TrackingBottomSheet(
            bookingRef     = "SKY-482930",
            etaMinutes     = 14,
            statusLabel    = "In flight",
            flightProgress = 0.6f,
            onCancelRide   = {},
        )
    }
}
