package com.skyrik.core.data.model

import java.time.LocalDateTime

/**
 * Sealed class state machine for the Skyrik booking flow.
 *
 * State transitions:
 *  Idle
 *   └─► LocationEntry (pickup entered)
 *        └─► ScheduleEntry (destination entered)
 *             └─► RideSelection (schedule set, show available helicopters)
 *                  └─► PricingReview (helicopter selected, show breakdown)
 *                       └─► Confirming (user tapped "Confirm & Pay", awaiting API)
 *                            ├─► Confirmed (API returned booking ref)
 *                            └─► Error (API failure or validation error)
 *
 * Any state can transition to [Error]; Error can transition back to the
 * appropriate recovery state via the ViewModel.
 */
sealed class BookingState {

    /** Initial state — no data entered yet. */
    data object Idle : BookingState()

    /** User has entered (or is entering) the pickup location. */
    data class LocationEntry(
        val pickup: String? = null,
    ) : BookingState()

    /** Pickup confirmed; user is now entering the destination. */
    data class ScheduleEntry(
        val pickup: String,
        val destination: String? = null,
    ) : BookingState()

    /** Both locations set; user is selecting date/time and the system is fetching helicopters. */
    data class RideSelection(
        val pickup: String,
        val destination: String,
        val scheduledTime: LocalDateTime,
        val availableHelicopters: List<Helicopter> = emptyList(),
    ) : BookingState()

    /** A helicopter has been selected; showing the pricing breakdown. */
    data class PricingReview(
        val pickup: String,
        val destination: String,
        val scheduledTime: LocalDateTime,
        val selectedHelicopter: Helicopter,
        val pricingBreakdown: PricingBreakdown? = null,
        val selectedPaymentMethod: PaymentMethod? = null,
    ) : BookingState()

    /** Booking confirmation in progress (awaiting API response). */
    data object Confirming : BookingState()

    /** Booking successfully created. */
    data class Confirmed(
        val bookingRef: String,
        val helicopter: Helicopter,
        val pickup: String,
        val destination: String,
        val scheduledTime: LocalDateTime,
    ) : BookingState()

    /** An error occurred at any stage. */
    data class Error(
        val message: String,
        val previousState: BookingState? = null,
    ) : BookingState()
}
