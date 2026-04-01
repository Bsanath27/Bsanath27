package com.skyrik.feature.booking.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skyrik.core.ui.components.SkyrikButton
import com.skyrik.core.ui.components.SkyrikTopAppBar
import com.skyrik.core.ui.theme.SkyrikTheme
import com.skyrik.feature.booking.BookingViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

/**
 * ScheduleScreen — Step 3 of the booking flow.
 *
 * Uses Material 3 [SegmentedButton] to toggle between:
 *  - "Depart Now" — immediate booking with the current time
 *  - "Schedule"   — shows inline date + time pickers
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: BookingViewModel,
    onScheduleConfirmed: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = SkyrikTheme.spacing

    // 0 = Depart Now, 1 = Schedule
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(0) }
    val isScheduled = selectedOptionIndex == 1

    val datePickerState  = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
    )
    val timePickerState  = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute,
        is24Hour = false,
    )

    Scaffold(
        topBar = {
            SkyrikTopAppBar(
                title = "When are you flying?",
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

            BookingStepIndicator(currentStep = 3, totalSteps = 3)

            Spacer(modifier = Modifier.height(spacing.space_xl))

            Text(
                text = "Choose departure time",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(spacing.space_xl))

            // ── Segmented button ──────────────────────────────────────────────
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = selectedOptionIndex == 0,
                    onClick = { selectedOptionIndex = 0 },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.FlightTakeoff,
                            contentDescription = null,
                        )
                    },
                ) {
                    Text("Depart Now")
                }
                SegmentedButton(
                    selected = selectedOptionIndex == 1,
                    onClick = { selectedOptionIndex = 1 },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                        )
                    },
                ) {
                    Text("Schedule")
                }
            }

            Spacer(modifier = Modifier.height(spacing.space_xl))

            // ── Inline date + time pickers (only when "Schedule" selected) ────
            AnimatedVisibility(
                visible = isScheduled,
                enter = expandVertically(),
                exit  = shrinkVertically(),
            ) {
                Column {
                    // Date picker section header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing.space_sm),
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = "Select Date",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    Spacer(modifier = Modifier.height(spacing.space_sm))

                    // Material 3 DatePicker (embedded — no dialog)
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = SkyrikTheme.shapes.shapeMedium,
                            ),
                        showModeToggle = false,
                    )

                    Spacer(modifier = Modifier.height(spacing.space_xl))

                    // Time picker section header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing.space_sm),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = "Select Time",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    Spacer(modifier = Modifier.height(spacing.space_sm))

                    TimePicker(
                        state = timePickerState,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            SkyrikButton(
                text = if (isScheduled) "Confirm Schedule" else "Find Helicopters Now",
                onClick = {
                    val scheduledTime = if (isScheduled) {
                        val epochMillis = datePickerState.selectedDateMillis
                            ?: System.currentTimeMillis()
                        val date = Instant.ofEpochMilli(epochMillis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        LocalDateTime.of(
                            date,
                            LocalTime.of(timePickerState.hour, timePickerState.minute),
                        )
                    } else {
                        LocalDateTime.now()
                    }
                    viewModel.setSchedule(scheduledTime)
                    onScheduleConfirmed()
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(spacing.space_xl))
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF050B18)
@Composable
private fun ScheduleScreenSegmentPreview() {
    SkyrikTheme(darkTheme = true) {
        Column(modifier = Modifier.padding(16.dp)) {
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = true,
                    onClick = {},
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                ) { Text("Depart Now") }
                SegmentedButton(
                    selected = false,
                    onClick = {},
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                ) { Text("Schedule") }
            }
        }
    }
}
