package com.skyrik.core.data.fake

import com.skyrik.core.data.model.Helicopter
import com.skyrik.core.data.model.HelicopterCategory
import com.skyrik.core.data.model.Location
import com.skyrik.core.data.repository.HelicopterRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Fake in-memory implementation of [HelicopterRepository].
 * Provides stub location suggestions for the search UI.
 */
@Singleton
class FakeHelicopterRepository @Inject constructor() : HelicopterRepository {

    private val locations = listOf(
        Location("Juhu Aerodrome, Mumbai", 19.0989, 72.8340),
        Location("Chhatrapati Shivaji Maharaj International Airport, Mumbai", 19.0896, 72.8656),
        Location("Mahalaxmi Racecourse Helipad, Mumbai", 18.9856, 72.8205),
        Location("Nariman Point Helipad, Mumbai", 18.9256, 72.8242),
        Location("Bandra-Kurla Complex Helipad, Mumbai", 19.0643, 72.8693),
        Location("Pune Airport (PNQ)", 18.5822, 73.9197),
        Location("Lonavala Helipad", 18.7485, 73.4082),
        Location("Shirdi Airport", 19.6888, 74.3795),
        Location("Nashik Airport", 19.9632, 73.8108),
        Location("Goa International Airport", 15.3808, 73.8314),
    )

    private val catalogue = listOf(
        Helicopter(
            id = "r44-001",
            name = "Robinson R44",
            category = HelicopterCategory.STANDARD,
            seats = 3, speedKmh = 185, rangeKm = 560,
            pricePerKm = 85.0, baseFare = 3500.0,
            imageUrl = "https://placeholder.skyrik.app/helicopters/r44.png",
            amenities = listOf("Air-conditioned", "Headsets"),
            pilotName = "Capt. Arjun Mehta", pilotRating = 4.7f,
        ),
        Helicopter(
            id = "bell407-001",
            name = "Bell 407",
            category = HelicopterCategory.EXECUTIVE,
            seats = 6, speedKmh = 245, rangeKm = 685,
            pricePerKm = 140.0, baseFare = 8000.0,
            imageUrl = "https://placeholder.skyrik.app/helicopters/bell407.png",
            amenities = listOf("Air-conditioned", "Leather seats", "Refreshments", "Headsets"),
            pilotName = "Capt. Priya Sharma", pilotRating = 4.9f,
        ),
        Helicopter(
            id = "s76-001",
            name = "Sikorsky S-76",
            category = HelicopterCategory.VIP,
            seats = 12, speedKmh = 287, rangeKm = 761,
            pricePerKm = 280.0, baseFare = 25000.0,
            imageUrl = "https://placeholder.skyrik.app/helicopters/s76.png",
            amenities = listOf("VIP cabin", "Full bar", "Executive seats", "Satellite connectivity"),
            pilotName = "Capt. Kavitha Iyer", pilotRating = 5.0f,
        ),
    )

    override suspend fun getAllHelicopters(): Result<List<Helicopter>> {
        delay(600)
        return Result.success(catalogue)
    }

    override suspend fun getHelicopterById(id: String): Result<Helicopter> {
        delay(300)
        return catalogue.find { it.id == id }
            ?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("Helicopter $id not found"))
    }

    override suspend fun searchLocations(query: String): Result<List<Location>> {
        delay(400)
        if (query.length < 2) return Result.success(emptyList())
        val filtered = locations.filter {
            it.displayName.contains(query, ignoreCase = true)
        }
        return Result.success(filtered)
    }

    override suspend fun reverseGeocode(latitude: Double, longitude: Double): Result<Location> {
        delay(300)
        // Return nearest stub location
        val nearest = locations.minByOrNull { loc ->
            val dLat = loc.latitude - latitude
            val dLng = loc.longitude - longitude
            dLat * dLat + dLng * dLng
        } ?: locations.first()
        return Result.success(nearest)
    }
}
