package com.skyrik.feature.booking.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skyrik.core.ui.components.SkyrikButton
import com.skyrik.core.ui.components.SkyrikTopAppBar
import com.skyrik.core.ui.theme.SkyrikTheme
import com.skyrik.feature.booking.BookingViewModel

private val SUGGESTED_DESTINATIONS = listOf(
    "Pune Airport (PNQ)",
    "Lonavala Helipad",
    "Shirdi Airport",
    "Nashik Airport",
    "Goa International Airport",
    "Mahabaleshwar Helipad",
    "Alibag Helipad",
)

/**
 * DestinationScreen — Step 2 of the booking flow.
 * User enters their drop-off location.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationScreen(
    viewModel: BookingViewModel,
    onDestinationConfirmed: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = SkyrikTheme.spacing
    var query by rememberSaveable { mutableStateOf("") }
    var selectedDestination by rememberSaveable { mutableStateOf<String?>(null) }
    val keyboard = LocalSoftwareKeyboardController.current

    val filteredSuggestions = remember(query) {
        if (query.length < 2) SUGGESTED_DESTINATIONS
        else SUGGESTED_DESTINATIONS.filter { it.contains(query, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            SkyrikTopAppBar(
                title = "Destination",
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

            BookingStepIndicator(currentStep = 2, totalSteps = 3)

            Spacer(modifier = Modifier.height(spacing.space_xl))

            Text(
                text = "Where are you flying to?",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(spacing.space_base))

            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    selectedDestination = null
                },
                placeholder = {
                    Text(
                        "Search destination",
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
                        if (query.isNotBlank()) selectedDestination = query
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor   = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedBorderColor    = MaterialTheme.colorScheme.outlineVariant,
                    focusedBorderColor      = MaterialTheme.colorScheme.primary,
                ),
            )

            Spacer(modifier = Modifier.height(spacing.space_base))

            LazyColumn {
                items(filteredSuggestions) { suggestion ->
                    LocationSuggestionRow(
                        name = suggestion,
                        isSelected = suggestion == selectedDestination,
                        onClick = {
                            selectedDestination = suggestion
                            query = suggestion
                            keyboard?.hide()
                        },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            SkyrikButton(
                text = "Set Destination",
                onClick = {
                    val destination = selectedDestination ?: query.takeIf { it.isNotBlank() } ?: return@SkyrikButton
                    viewModel.setDestination(destination)
                    onDestinationConfirmed()
                },
                enabled = selectedDestination != null || query.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(spacing.space_xl))
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF050B18)
@Composable
private fun DestinationScreenPreview() {
    SkyrikTheme(darkTheme = true) {
        Column(modifier = Modifier.padding(16.dp)) {
            BookingStepIndicator(currentStep = 2, totalSteps = 3)
        }
    }
}
