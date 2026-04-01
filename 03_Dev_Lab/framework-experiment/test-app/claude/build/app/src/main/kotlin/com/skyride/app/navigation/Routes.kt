package com.skyrik.app.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe route definitions for Navigation 2.8+.
 * Each object/data class represents a distinct destination.
 * Serializable is required for type-safe navigation argument passing.
 */

// ─── Root destinations ───────────────────────────────────────────────────────

/** Nested graph wrapping the entire booking flow. */
@Serializable
object BookingGraph

/** Nested graph for active ride screens. */
@Serializable
object ActiveRideGraph

// ─── Booking flow ────────────────────────────────────────────────────────────

/** Home / Map screen — the default start destination. */
@Serializable
object HomeRoute

/** Step 1: Enter pickup location. */
@Serializable
object PickupRoute

/** Step 2: Enter destination location. */
@Serializable
object DestinationRoute

/** Step 3: Choose date / time. */
@Serializable
object ScheduleRoute

/** Helicopter selection list. */
@Serializable
object RideSelectionRoute

/** Pricing review before payment. */
@Serializable
object PricingRoute

/** Booking confirmed success screen. */
@Serializable
data class ConfirmationRoute(val bookingRef: String)

// ─── Active ride ─────────────────────────────────────────────────────────────

/** Live tracking screen for an in-progress ride. */
@Serializable
data class TrackingRoute(val bookingRef: String)
