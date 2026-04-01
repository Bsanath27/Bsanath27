package com.skyrik.core.data.fake

import com.skyrik.core.data.model.Helicopter
import com.skyrik.core.data.model.HelicopterCategory
import com.skyrik.core.data.model.PaymentMethod
import com.skyrik.core.data.model.PaymentType
import com.skyrik.core.data.model.PricingBreakdown
import com.skyrik.core.data.repository.BookingRepository
import com.skyrik.core.data.repository.TrackingStatus
import com.skyrik.core.data.repository.TrackingUpdate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Fake in-memory implementation of [BookingRepository].
 *
 * Returns realistic stub data so the app compiles and runs end-to-end
 * without a backend. Simulates network latency with [delay].
 */
@Singleton
class FakeBookingRepository @Inject constructor() : BookingRepository {

    // ── Stub helicopter catalogue ─────────────────────────────────────────────

    private val helicopterCatalogue = listOf(
        Helicopter(
            id = "r44-001",
            name = "Robinson R44",
            category = HelicopterCategory.STANDARD,
            seats = 3,
            speedKmh = 185,
            rangeKm = 560,
            pricePerKm = 85.0,
            baseFare = 3500.0,
            imageUrl = "https://placeholder.skyrik.app/helicopters/r44.png",
            amenities = listOf("Air-conditioned", "Headsets"),
            pilotName = "Capt. Arjun Mehta",
            pilotRating = 4.7f,
        ),
        Helicopter(
            id = "bell407-001",
            name = "Bell 407",
            category = HelicopterCategory.EXECUTIVE,
            seats = 6,
            speedKmh = 245,
            rangeKm = 685,
            pricePerKm = 140.0,
            baseFare = 8000.0,
            imageUrl = "https://placeholder.skyrik.app/helicopters/bell407.png",
            amenities = listOf("Air-conditioned", "Leather seats", "Refreshments", "Headsets"),
            pilotName = "Capt. Priya Sharma",
            pilotRating = 4.9f,
        ),
        Helicopter(
            id = "as350-001",
            name = "Airbus H125",
            category = HelicopterCategory.EXECUTIVE,
            seats = 5,
            speedKmh = 230,
            rangeKm = 660,
            pricePerKm = 120.0,
            baseFare = 7000.0,
            imageUrl = "https://placeholder.skyrik.app/helicopters/as350.png",
            amenities = listOf("Air-conditioned", "Panoramic windows", "Headsets"),
            pilotName = "Capt. Rahul Nair",
            pilotRating = 4.8f,
        ),
        Helicopter(
            id = "s76-001",
            name = "Sikorsky S-76",
            category = HelicopterCategory.VIP,
            seats = 12,
            speedKmh = 287,
            rangeKm = 761,
            pricePerKm = 280.0,
            baseFare = 25000.0,
            imageUrl = "https://placeholder.skyrik.app/helicopters/s76.png",
            amenities = listOf(
                "VIP cabin", "Full bar", "Leather executive seats",
                "Noise-cancelling headsets", "Satellite connectivity"
            ),
            pilotName = "Capt. Kavitha Iyer",
            pilotRating = 5.0f,
        ),
    )

    private val paymentMethods = listOf(
        PaymentMethod(
            id = "pm-001",
            type = PaymentType.CARD,
            displayName = "Visa",
            last4 = "4242",
            isDefault = true,
        ),
        PaymentMethod(
            id = "pm-002",
            type = PaymentType.UPI,
            displayName = "UPI — arjun@okhdfcbank",
            isDefault = false,
        ),
    )

    // ── BookingRepository impl ────────────────────────────────────────────────

    override suspend fun getAvailableHelicopters(
        pickup: String,
        destination: String,
        scheduledTime: LocalDateTime,
    ): Result<List<Helicopter>> {
        delay(1200) // simulate network
        return Result.success(helicopterCatalogue)
    }

    override suspend fun getPricingBreakdown(
        helicopterId: String,
        pickup: String,
        destination: String,
    ): Result<PricingBreakdown> {
        delay(800)
        val heli = helicopterCatalogue.find { it.id == helicopterId }
            ?: return Result.failure(IllegalArgumentException("Helicopter not found"))

        val distanceKm = 48.0 // stub distance
        val distanceFare = heli.pricePerKm * distanceKm
        val baseFare     = heli.baseFare
        val taxes        = (baseFare + distanceFare) * 0.18
        val convenience  = 250.0
        val total        = baseFare + distanceFare + taxes + convenience

        return Result.success(
            PricingBreakdown(
                baseFare       = baseFare,
                distanceFare   = distanceFare,
                taxes          = taxes,
                convenienceFee = convenience,
                totalAmount    = total,
            )
        )
    }

    override suspend fun confirmBooking(
        helicopterId: String,
        pickup: String,
        destination: String,
        scheduledTime: LocalDateTime,
        paymentMethodId: String,
    ): Result<String> {
        delay(2000) // simulate payment processing
        val bookingRef = "SKY-${(100000..999999).random()}"
        return Result.success(bookingRef)
    }

    override suspend fun getPaymentMethods(): Result<List<PaymentMethod>> {
        delay(400)
        return Result.success(paymentMethods)
    }

    override fun trackBooking(bookingRef: String): Flow<TrackingUpdate> = flow {
        val statuses = listOf(
            TrackingStatus.EN_ROUTE_TO_PICKUP,
            TrackingStatus.AT_PICKUP,
            TrackingStatus.IN_FLIGHT,
            TrackingStatus.LANDED,
            TrackingStatus.COMPLETED,
        )
        var eta = 22
        for (status in statuses) {
            emit(
                TrackingUpdate(
                    bookingRef = bookingRef,
                    pilotLat   = 19.0760 + (Math.random() * 0.01),
                    pilotLng   = 72.8777 + (Math.random() * 0.01),
                    etaMinutes = eta,
                    status     = status,
                )
            )
            eta -= 4
            delay(5000)
        }
    }

    override suspend fun cancelBooking(bookingRef: String): Result<Unit> {
        delay(600)
        return Result.success(Unit)
    }
}
