package com.skyrik.core.data.repository

import com.skyrik.core.data.model.Helicopter
import com.skyrik.core.data.model.Location

/**
 * Contract for the helicopter catalogue repository.
 *
 * Separated from [BookingRepository] to keep responsibilities focused:
 *  - Catalogue browsing / filtering
 *  - Location search / autocomplete
 */
interface HelicopterRepository {

    /** Fetch all helicopters in the catalogue (unfiltered). */
    suspend fun getAllHelicopters(): Result<List<Helicopter>>

    /** Search for a helicopter by ID. */
    suspend fun getHelicopterById(id: String): Result<Helicopter>

    /**
     * Resolve a user-typed query to a list of location suggestions.
     * Used by the pickup / destination search fields.
     */
    suspend fun searchLocations(query: String): Result<List<Location>>

    /**
     * Resolve a lat/lng pair to a human-readable display name (reverse geocoding).
     */
    suspend fun reverseGeocode(latitude: Double, longitude: Double): Result<Location>
}
