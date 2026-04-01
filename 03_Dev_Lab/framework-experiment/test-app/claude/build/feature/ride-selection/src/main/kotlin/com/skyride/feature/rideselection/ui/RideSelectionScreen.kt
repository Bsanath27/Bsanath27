package com.skyrik.feature.rideselection.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skyrik.core.data.model.BookingState
import com.skyrik.core.data.model.Helicopter
import com.skyrik.core.data.model.HelicopterCategory
import com.skyrik.core.ui.components.HeliCard
import com.skyrik.core.ui.components.HeliCardShimmer
import com.skyrik.core.ui.components.SkyrikButton
import com.skyrik.core.ui.components.SkyrikTopAppBar
import com.skyrik.core.ui.theme.SkyrikTheme
import com.skyrik.feature.booking.BookingViewModel
import com.skyrik.feature.rideselection.HelicopterFilter
import com.skyrik.feature.rideselection.RideSelectionViewModel
import java.time.LocalDateTime

/**
 * RideSelectionScreen — scrollable list of available helicopters.
 *
 * Features:
 *  - Filter chips (All / Standard / Executive / VIP)
 *  - Shimmer placeholders while helicopters are loading
 *  - Animated selection border on [HeliCard]
 *  - Sticky bottom bar with "Choose This Flight" CTA
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RideSelectionScreen(
    viewModel: BookingViewModel,
    onSelectionConfirmed: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    localViewModel: RideSelectionViewModel = hiltViewModel(),
) {
    val spacing = SkyrikTheme.spacing
    val bookingState by viewModel.bookingState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedId by localViewModel.selectedHelicopterId.collectAsState()
    val activeFilter by localViewModel.activeFilter.collectAsState()

    val rideState = bookingState as? BookingState.RideSelection
    val allHelicopters = rideState?.availableHelicopters ?: emptyList()
    val displayedHelicopters = localViewModel.filteredHelicopters(allHelicopters)

    Scaffold(
        topBar = {
            SkyrikTopAppBar(
                title = "Choose Your Flight",
                onNavigateBack = onBack,
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = SkyrikTheme.elevation.level3,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(spacing.space_base),
                ) {
                    SkyrikButton(
                        text = if (selectedId != null) "Choose This Flight" else "Select a Helicopter",
                        onClick = {
                            val selected = allHelicopters.find { it.id == selectedId } ?: return@SkyrikButton
                            viewModel.selectHelicopter(selected)
                            onSelectionConfirmed()
                        },
                        enabled = selectedId != null,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {

            // ── Filter chips ──────────────────────────────────────────────────
            LazyRow(
                contentPadding = PaddingValues(horizontal = spacing.space_base),
                horizontalArrangement = Arrangement.spacedBy(spacing.space_sm),
                modifier = Modifier.padding(vertical = spacing.space_sm),
            ) {
                items(HelicopterFilter.entries) { filter ->
                    FilterChip(
                        selected = activeFilter == filter,
                        onClick = { localViewModel.setFilter(filter) },
                        label = { Text(filter.label) },
                        leadingIcon = if (activeFilter == filter) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                )
                            }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        ),
                    )
                }
            }

            // ── List ──────────────────────────────────────────────────────────
            if (isLoading && allHelicopters.isEmpty()) {
                // Shimmer loading state
                LazyColumn(
                    contentPadding = PaddingValues(spacing.space_base),
                    verticalArrangement = Arrangement.spacedBy(spacing.space_md),
                ) {
                    items(4) { HeliCardShimmer() }
                }
            } else if (displayedHelicopters.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "No helicopters available for this filter",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(spacing.space_base),
                    verticalArrangement = Arrangement.spacedBy(spacing.space_md),
                ) {
                    items(
                        items = displayedHelicopters,
                        key = { it.id },
                    ) { helicopter ->
                        HeliCard(
                            name = helicopter.name,
                            category = helicopter.category.displayName,
                            seats = helicopter.seats,
                            speedKmh = helicopter.speedKmh,
                            priceFormatted = "₹ ${"%.0f".format(helicopter.baseFare)}+",
                            eta = "~${(helicopter.rangeKm / helicopter.speedKmh * 60).toInt()} min",
                            isSelected = helicopter.id == selectedId,
                            onClick = { localViewModel.selectHelicopter(helicopter.id) },
                        )
                    }
                    // Bottom spacing so last card doesn't hide behind sticky bar
                    item { Spacer(modifier = Modifier.height(spacing.space_2xl)) }
                }
            }
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF050B18)
@Composable
private fun RideSelectionScreenPreview() {
    val sampleHelicopter = Helicopter(
        id = "bell407-001",
        name = "Bell 407",
        category = HelicopterCategory.EXECUTIVE,
        seats = 6,
        speedKmh = 245,
        rangeKm = 685,
        pricePerKm = 140.0,
        baseFare = 8000.0,
        imageUrl = "",
        amenities = listOf("Air-conditioned", "Leather seats"),
        pilotName = "Capt. Priya Sharma",
        pilotRating = 4.9f,
    )

    SkyrikTheme(darkTheme = true) {
        Column(modifier = Modifier.padding(16.dp)) {
            HeliCard(
                name = sampleHelicopter.name,
                category = sampleHelicopter.category.displayName,
                seats = sampleHelicopter.seats,
                speedKmh = sampleHelicopter.speedKmh,
                priceFormatted = "₹ 8,000+",
                eta = "~18 min",
                isSelected = true,
                onClick = {},
            )
        }
    }
}
