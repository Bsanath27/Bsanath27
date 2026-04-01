package com.skyrik.feature.booking.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skyrik.core.ui.components.SkyrikButton
import com.skyrik.core.ui.theme.SkyrikTheme
import com.skyrik.feature.booking.BookingViewModel

/**
 * HomeScreen — the root screen of the Skyrik experience.
 *
 * Layout:
 *  - Full-screen map placeholder (replaced by Google Maps SDK at runtime)
 *  - Floating brand header (top-left)
 *  - My-location FAB (center-right)
 *  - Persistent bottom sheet with a "Where to?" search bar and "Book a Flight" CTA
 */
@Composable
fun HomeScreen(
    viewModel: BookingViewModel,
    onStartBooking: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {

        // ── Map layer ─────────────────────────────────────────────────────────
        MapPlaceholder(modifier = Modifier.fillMaxSize())

        // ── Brand header ──────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(SkyrikTheme.spacing.space_base)
                .align(Alignment.TopStart),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.FlightTakeoff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp),
            )
            Spacer(modifier = Modifier.width(SkyrikTheme.spacing.space_sm))
            Text(
                text = "Skyrik",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
            )
        }

        // ── My-location FAB ───────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = SkyrikTheme.spacing.space_base)
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = SkyrikTheme.shapes.shapeCircle,
                )
                .clickable { /* trigger location permission and center map */ },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "My location",
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        // ── Bottom sheet ──────────────────────────────────────────────────────
        HomeBottomSheetContent(
            onStartBooking = onStartBooking,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun HomeBottomSheetContent(
    onStartBooking: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = SkyrikTheme.spacing
    val shapes  = SkyrikTheme.shapes

    Surface(
        modifier = modifier,
        shape = shapes.shapeTopRounded,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = SkyrikTheme.elevation.level3,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(spacing.space_xl),
        ) {
            // Drag handle indicator
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = shapes.shapeCircle,
                    ),
            )

            Spacer(modifier = Modifier.height(spacing.space_lg))

            Text(
                text = "Where would you like to fly?",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(spacing.space_base))

            // Tappable search bar row — opens booking flow on tap
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onStartBooking),
                shape = shapes.shapeMedium,
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = SkyrikTheme.elevation.level1,
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = spacing.space_base,
                        vertical = spacing.space_md,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.width(spacing.space_sm))
                    Text(
                        text = "Enter pickup or destination",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.space_lg))

            SkyrikButton(
                text = "Book a Flight",
                onClick = onStartBooking,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(spacing.space_md))
        }
    }
}

/**
 * Map placeholder composable.
 *
 * Replace this with the Google Maps Compose SDK [GoogleMap] composable at runtime.
 * The Maps SDK requires a valid API key set in AndroidManifest.xml via the
 * `com.google.android.geo.API_KEY` meta-data entry.
 */
@Composable
fun MapPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Map,
                contentDescription = "Map",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outlineVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Map loads at runtime",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "(Requires Google Maps API key)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF050B18, device = "spec:width=411dp,height=891dp")
@Composable
private fun HomeBottomSheetPreview() {
    SkyrikTheme(darkTheme = true) {
        HomeBottomSheetContent(onStartBooking = {})
    }
}
