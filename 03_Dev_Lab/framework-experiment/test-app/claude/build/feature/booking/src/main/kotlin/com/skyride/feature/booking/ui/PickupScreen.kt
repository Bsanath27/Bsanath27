package com.skyrik.feature.booking.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skyrik.core.ui.components.SkyrikButton
import com.skyrik.core.ui.components.SkyrikTopAppBar
import com.skyrik.core.ui.theme.SkyrikTheme
import com.skyrik.feature.booking.BookingViewModel

// Stub location suggestions — replaced by actual search results from HelicopterRepository
private val SUGGESTED_PICKUPS = listOf(
    "Juhu Aerodrome, Mumbai",
    "Chhatrapati Shivaji Maharaj International Airport",
    "Mahalaxmi Racecourse Helipad, Mumbai",
    "Nariman Point Helipad, Mumbai",
    "Bandra-Kurla Complex Helipad, Mumbai",
)

/**
 * PickupScreen — Step 1 of the booking flow.
 * User types or selects their pickup helipad/location.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupScreen(
    viewModel: BookingViewModel,
    onPickupConfirmed: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = SkyrikTheme.spacing

    var query by rememberSaveable { mutableStateOf("") }
    var selectedPickup by rememberSaveable { mutableStateOf<String?>(null) }
    val keyboard = LocalSoftwareKeyboardController.current

    val filteredSuggestions = remember(query) {
        if (query.length < 2) SUGGESTED_PICKUPS
        else SUGGESTED_PICKUPS.filter { it.contains(query, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            SkyrikTopAppBar(
                title = "Pickup Location",
                onNavigateBack = onBack,
            )
        },
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = spacing.space_base),
        ) {
            Spacer(modifier = Modifier.height(spacing.space_base))

            // Step indicator
            BookingStepIndicator(currentStep = 1, totalSteps = 3)

            Spacer(modifier = Modifier.height(spacing.space_xl))

            Text(
                text = "Where will you depart from?",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(spacing.space_base))

            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    selectedPickup = null
                },
                placeholder = {
                    Text(
                        "Search helipad or location",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = SkyrikTheme.shapes.shapeMedium,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboard?.hide()
                        if (query.isNotBlank()) selectedPickup = query
                    },
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor   = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedBorderColor    = MaterialTheme.colorScheme.outlineVariant,
                    focusedBorderColor      = MaterialTheme.colorScheme.primary,
                ),
            )

            Spacer(modifier = Modifier.height(spacing.space_base))

            // Suggestions list
            LazyColumn {
                items(filteredSuggestions) { suggestion ->
                    LocationSuggestionRow(
                        name = suggestion,
                        isSelected = suggestion == selectedPickup,
                        onClick = {
                            selectedPickup = suggestion
                            query = suggestion
                            keyboard?.hide()
                        },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            SkyrikButton(
                text = "Confirm Pickup",
                onClick = {
                    val pickup = selectedPickup ?: query.takeIf { it.isNotBlank() } ?: return@SkyrikButton
                    viewModel.setPickup(pickup)
                    onPickupConfirmed()
                },
                enabled = selectedPickup != null || query.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(spacing.space_xl))
        }
    }
}

// ─── Shared sub-components ────────────────────────────────────────────────────

@Composable
fun LocationSuggestionRow(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = SkyrikTheme.spacing
    val bgColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                  else MaterialTheme.colorScheme.background

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(vertical = spacing.space_md, horizontal = spacing.space_xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = if (isSelected) MaterialTheme.colorScheme.primary
                   else MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.width(spacing.space_sm))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface,
        )
    }
}

/**
 * Simple 3-step progress indicator for the booking flow.
 */
@Composable
fun BookingStepIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
) {
    val spacing = SkyrikTheme.spacing

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(totalSteps) { index ->
            val stepNumber = index + 1
            val isCompleted = stepNumber < currentStep
            val isCurrent = stepNumber == currentStep

            val color = when {
                isCompleted || isCurrent -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outlineVariant
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(if (isCurrent) 12.dp else 8.dp)
                        .background(color = color, shape = SkyrikTheme.shapes.shapeCircle),
                )
            }

            if (index < totalSteps - 1) {
                Spacer(modifier = Modifier.width(spacing.space_xs))
                HorizontalDivider(
                    modifier = Modifier.width(spacing.space_2xl),
                    thickness = 2.dp,
                    color = if (currentStep > stepNumber) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outlineVariant,
                )
                Spacer(modifier = Modifier.width(spacing.space_xs))
            }
        }

        Spacer(modifier = Modifier.width(spacing.space_base))
        Text(
            text = "Step $currentStep of $totalSteps",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF050B18, device = "spec:width=411dp,height=891dp")
@Composable
private fun PickupScreenPreview() {
    SkyrikTheme(darkTheme = true) {
        Column(modifier = Modifier.padding(16.dp)) {
            BookingStepIndicator(currentStep = 1, totalSteps = 3)
            Spacer(Modifier.height(16.dp))
            LocationSuggestionRow(
                name = "Juhu Aerodrome, Mumbai",
                isSelected = true,
                onClick = {},
            )
            LocationSuggestionRow(
                name = "Nariman Point Helipad, Mumbai",
                isSelected = false,
                onClick = {},
            )
        }
    }
}
