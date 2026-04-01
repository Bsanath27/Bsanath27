package com.skyrik.feature.pricing.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skyrik.core.data.model.BookingState
import com.skyrik.core.data.model.PaymentMethod
import com.skyrik.core.data.model.PaymentType
import com.skyrik.core.data.model.PricingBreakdown
import com.skyrik.core.ui.components.LoadingShimmer
import com.skyrik.core.ui.components.SkyrikButton
import com.skyrik.core.ui.components.SkyrikTopAppBar
import com.skyrik.core.ui.components.StatusChip
import com.skyrik.core.ui.theme.LocalSkyrikExtendedColors
import com.skyrik.core.ui.theme.SkyrikTheme
import com.skyrik.feature.booking.BookingViewModel
import com.skyrik.feature.pricing.PricingViewModel
import java.time.format.DateTimeFormatter

/**
 * PricingScreen — review pricing breakdown before confirming.
 *
 * Sections:
 *  1. Trip summary card (route + helicopter + time)
 *  2. Price breakdown rows (base fare, distance, taxes, convenience fee, total)
 *  3. Payment method row (tappable → shows payment sheet)
 *  4. Terms & Conditions checkbox
 *  5. Sticky "Confirm & Pay" CTA button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PricingScreen(
    viewModel: BookingViewModel,
    onConfirmed: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    localViewModel: PricingViewModel = hiltViewModel(),
) {
    val spacing = SkyrikTheme.spacing
    val bookingState by viewModel.bookingState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val termsAccepted by localViewModel.termsAccepted.collectAsState()

    val pricingState = bookingState as? BookingState.PricingReview
    val isConfirming = bookingState is BookingState.Confirming

    // Navigate on success
    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is BookingViewModel.UiEvent.NavigateToConfirmation ->
                    onConfirmed(event.bookingRef)
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            SkyrikTopAppBar(
                title = "Review & Pay",
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
                    pricingState?.pricingBreakdown?.let { breakdown ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Total",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = breakdown.formatAmount(breakdown.totalAmount),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                        Spacer(modifier = Modifier.height(spacing.space_md))
                    }

                    SkyrikButton(
                        text = "Confirm & Pay",
                        onClick = { viewModel.confirmBooking() },
                        enabled = termsAccepted && pricingState?.pricingBreakdown != null && !isConfirming,
                        isLoading = isConfirming,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = spacing.space_base)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(spacing.space_base),
        ) {
            Spacer(modifier = Modifier.height(spacing.space_sm))

            // ── Trip summary ──────────────────────────────────────────────────
            if (pricingState != null) {
                TripSummaryCard(state = pricingState)
            }

            // ── Price breakdown ───────────────────────────────────────────────
            if (isLoading && pricingState?.pricingBreakdown == null) {
                PriceBreakdownShimmer()
            } else if (pricingState?.pricingBreakdown != null) {
                val breakdown = pricingState.pricingBreakdown!!
                PriceBreakdownCard(breakdown = breakdown)
            }

            // ── Payment method ────────────────────────────────────────────────
            PaymentMethodRow(
                selectedMethod = pricingState?.selectedPaymentMethod,
                onClick = { localViewModel.togglePaymentSheet() },
            )

            // ── Terms ─────────────────────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { localViewModel.setTermsAccepted(!termsAccepted) },
            ) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = { localViewModel.setTermsAccepted(it) },
                )
                Spacer(modifier = Modifier.width(spacing.space_xs))
                Text(
                    text = "I agree to the Terms of Service and Cancellation Policy",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(spacing.space_4xl))
        }
    }
}

@Composable
private fun TripSummaryCard(state: BookingState.PricingReview) {
    val spacing = SkyrikTheme.spacing
    val formatter = DateTimeFormatter.ofPattern("EEE, d MMM • h:mm a")

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = SkyrikTheme.shapes.shapeMedium,
        elevation = CardDefaults.cardElevation(SkyrikTheme.elevation.level2),
    ) {
        Column(modifier = Modifier.padding(spacing.space_base)) {
            // Helicopter info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AirplanemodeActive,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(spacing.space_sm))
                Column {
                    Text(
                        text = state.selectedHelicopter.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    StatusChip(
                        label = state.selectedHelicopter.category.displayName,
                        isActive = false,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "⭐ ${state.selectedHelicopter.pilotRating}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = spacing.space_md),
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            // Pickup
            IconTextRow(
                icon = { Icon(Icons.Default.LocationOn, null, tint = SkyrikTheme.extendedColors.colorStateSuccess) },
                label = "Pickup",
                value = state.pickup,
            )

            Spacer(modifier = Modifier.height(spacing.space_sm))

            // Destination
            IconTextRow(
                icon = { Icon(Icons.Default.LocationOn, null, tint = MaterialTheme.colorScheme.primary) },
                label = "Destination",
                value = state.destination,
            )

            Spacer(modifier = Modifier.height(spacing.space_sm))

            // Time
            IconTextRow(
                icon = { Icon(Icons.Default.Schedule, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                label = "Departure",
                value = state.scheduledTime.format(formatter),
            )
        }
    }
}

@Composable
private fun IconTextRow(
    icon: @Composable () -> Unit,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val spacing = SkyrikTheme.spacing
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
    ) {
        icon()
        Spacer(modifier = Modifier.width(spacing.space_sm))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun PriceBreakdownCard(breakdown: PricingBreakdown) {
    val spacing = SkyrikTheme.spacing

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = SkyrikTheme.shapes.shapeMedium,
        elevation = CardDefaults.cardElevation(SkyrikTheme.elevation.level2),
    ) {
        Column(modifier = Modifier.padding(spacing.space_base)) {
            Text(
                text = "Price Breakdown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(spacing.space_md))

            PriceRow("Base fare", breakdown.formatAmount(breakdown.baseFare))
            PriceRow("Distance charge", breakdown.formatAmount(breakdown.distanceFare))
            PriceRow("GST & taxes (18%)", breakdown.formatAmount(breakdown.taxes))
            PriceRow("Convenience fee", breakdown.formatAmount(breakdown.convenienceFee))

            if (breakdown.hasDiscount) {
                PriceRow(
                    label = "Discount",
                    value = breakdown.formatAmount(breakdown.discount),
                    valueColor = SkyrikTheme.extendedColors.colorStateSuccess,
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = spacing.space_sm),
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Total Payable",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = breakdown.formatAmount(breakdown.totalAmount),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun PriceRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
) {
    val spacing = SkyrikTheme.spacing
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacing.space_xs),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor,
        )
    }
}

@Composable
private fun PaymentMethodRow(
    selectedMethod: PaymentMethod?,
    onClick: () -> Unit,
) {
    val spacing = SkyrikTheme.spacing

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = SkyrikTheme.shapes.shapeMedium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.space_base),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(spacing.space_md))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Payment Method",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = selectedMethod?.let {
                        "${it.displayName}${it.last4?.let { l4 -> " ••••$l4" } ?: ""}"
                    } ?: "Select payment method",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selectedMethod != null) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Select",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun PriceBreakdownShimmer() {
    val spacing = SkyrikTheme.spacing
    Column(verticalArrangement = Arrangement.spacedBy(spacing.space_sm)) {
        repeat(4) {
            LoadingShimmer(modifier = Modifier.fillMaxWidth(), height = 20.dp)
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF050B18)
@Composable
private fun PriceBreakdownPreview() {
    com.skyrik.core.ui.theme.SkyrikTheme(darkTheme = true) {
        val breakdown = PricingBreakdown(
            baseFare = 8000.0,
            distanceFare = 6720.0,
            taxes = 2649.6,
            convenienceFee = 250.0,
            totalAmount = 17619.6,
        )
        PriceBreakdownCard(breakdown = breakdown)
    }
}
