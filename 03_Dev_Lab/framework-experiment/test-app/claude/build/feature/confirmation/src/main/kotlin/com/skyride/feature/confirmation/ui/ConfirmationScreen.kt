package com.skyrik.feature.confirmation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skyrik.core.ui.components.SkyrikButton
import com.skyrik.core.ui.components.SkyrikGhostButton
import com.skyrik.core.ui.components.SkyrikSecondaryButton
import com.skyrik.core.ui.theme.SkyrikTheme

/**
 * ConfirmationScreen — post-booking success state.
 *
 * Features:
 *  - Bouncing check-circle success animation on enter
 *  - Prominently displayed booking reference (monospaced, copyable feel)
 *  - "Add to Calendar" and "Track Flight" CTAs
 *  - Share button in header
 */
@Composable
fun ConfirmationScreen(
    bookingRef: String,
    onTrackFlight: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = SkyrikTheme.spacing

    // Bounce-in animation for the success icon
    val iconScale = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        iconScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness    = Spring.StiffnessMedium,
            ),
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.space_sm),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { /* Share booking details */ }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // ── Scrollable content ────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = spacing.space_base),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(spacing.space_3xl))

            // Success icon with bounce animation
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Booking confirmed",
                modifier = Modifier
                    .size(96.dp)
                    .scale(iconScale.value),
                tint = SkyrikTheme.extendedColors.colorStateSuccess,
            )

            Spacer(modifier = Modifier.height(spacing.space_xl))

            Text(
                text = "Flight Booked!",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(spacing.space_sm))

            Text(
                text = "Your helicopter is confirmed and ready to take flight.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(spacing.space_2xl))

            // ── Booking reference card ────────────────────────────────────────
            BookingReferenceCard(bookingRef = bookingRef)

            Spacer(modifier = Modifier.height(spacing.space_2xl))

            // ── What's next card ──────────────────────────────────────────────
            WhatsNextCard()

            Spacer(modifier = Modifier.height(spacing.space_xl))
        }

        // ── CTA buttons ───────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.space_base)
                .padding(bottom = spacing.space_base),
            verticalArrangement = Arrangement.spacedBy(spacing.space_sm),
        ) {
            SkyrikButton(
                text = "Track My Flight",
                onClick = onTrackFlight,
                modifier = Modifier.fillMaxWidth(),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.space_sm),
            ) {
                SkyrikSecondaryButton(
                    text = "Add to Calendar",
                    onClick = { /* Intent to calendar app */ },
                    modifier = Modifier.weight(1f),
                )
                SkyrikGhostButton(
                    text = "Done",
                    onClick = onDone,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun BookingReferenceCard(bookingRef: String) {
    val spacing = SkyrikTheme.spacing
    val shapes  = SkyrikTheme.shapes

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = shapes.shapeLarge,
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                shape = shapes.shapeLarge,
            )
            .padding(spacing.space_xl),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Booking Reference",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(spacing.space_sm))
            Text(
                text = bookingRef,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp,
                    fontSize = 32.sp,
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun WhatsNextCard() {
    val spacing = SkyrikTheme.spacing

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = SkyrikTheme.shapes.shapeMedium,
    ) {
        Column(modifier = Modifier.padding(spacing.space_base)) {
            Text(
                text = "What happens next?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(spacing.space_md))

            WhatsNextStep(
                icon = Icons.Default.FlightTakeoff,
                title = "Pilot assigned",
                description = "Your pilot will be assigned 30 minutes before departure.",
            )
            Spacer(modifier = Modifier.height(spacing.space_md))
            WhatsNextStep(
                icon = Icons.Default.CalendarMonth,
                title = "Calendar reminder",
                description = "Add to your calendar so you don't miss your flight.",
            )
        }
    }
}

@Composable
private fun WhatsNextStep(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
) {
    val spacing = SkyrikTheme.spacing
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(spacing.space_sm))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF050B18, device = "spec:width=411dp,height=891dp")
@Composable
private fun ConfirmationScreenPreview() {
    SkyrikTheme(darkTheme = true) {
        ConfirmationScreen(
            bookingRef = "SKY-482930",
            onTrackFlight = {},
            onDone = {},
        )
    }
}
