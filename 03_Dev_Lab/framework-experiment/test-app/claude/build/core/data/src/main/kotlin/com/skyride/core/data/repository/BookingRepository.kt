package com.skyrik.core.data.repository

import com.skyrik.core.data.model.BookingState
import com.skyrik.core.data.model.Helicopter
import com.skyrik.core.data.model.PaymentMethod
import com.skyrik.core.data.model.PricingBreakdown
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Contract for the booking repository.
 *
 * All functions are suspend / Flow-based to allow async IO on any dispatcher.
 * Concrete implementations (real API or [FakeBookingRepository]) are bound
 * via Hilt in the corresponding data module.
 */
interface BookingRepository {

    /**
     * Fetch available helicopters for the given route and time.
     * Returns an empty list if no helicopters are available.
     */
    suspend fun getAvailableHelicopters(
        pickup: String,
        destination: String,
        scheduledTime: LocalDateTime,
    ): Result<List<Helicopter>>

    /**
     * Calculate the pricing breakdown for the selected helicopter and route.
     */
    suspend fun getPricingBreakdown(
        helicopterId: String,
        pickup: String,
        destination: String,
    ): Result<PricingBreakdown>

    /**
     * Submit the booking and return a booking reference string on success.
     */
    suspend fun confirmBooking(
        helicopterId: String,
        pickup: String,
        destination: String,
        scheduledTime: LocalDateTime,
        paymentMethodId: String,
    ): Result<String>

    /**
     * Fetch saved payment methods for the current user.
     */
    suspend fun getPaymentMethods(): Result<List<PaymentMethod>>

    /**
     * Stream live tracking updates for an active booking.
     * Emits [TrackingUpdate] at ~5 second intervals.
     */
    fun trackBooking(bookingRef: String): Flow<TrackingUpdate>

    /**
     * Cancel an active booking.
     */
    suspend fun cancelBooking(bookingRef: String): Result<Unit>
}

/** Snapshot of live tracking data for an active ride. */
data class TrackingUpdate(
    val bookingRef: String,
    val pilotLat: Double,
    val pilotLng: Double,
    val etaMinutes: Int,
    val status: TrackingStatus,
)

enum class TrackingStatus {
    EN_ROUTE_TO_PICKUP,
    AT_PICKUP,
    IN_FLIGHT,
    LANDED,
    COMPLETED,
}
