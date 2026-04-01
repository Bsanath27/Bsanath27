package com.skyrik.core.data.model

/**
 * Represents a geographic location resolved from user input.
 *
 * @param displayName Human-readable place name (e.g. "Juhu Aerodrome, Mumbai")
 * @param latitude    WGS84 latitude
 * @param longitude   WGS84 longitude
 * @param placeId     Optional provider place ID (Google Places, etc.)
 */
data class Location(
    val displayName: String,
    val latitude: Double,
    val longitude: Double,
    val placeId: String? = null,
)

/**
 * A resolved route between two locations.
 *
 * @param origin          Pickup location
 * @param destination     Drop-off location
 * @param distanceKm      Great-circle or road distance in km
 * @param estimatedMinutes Estimated flight time in minutes
 */
data class Route(
    val origin: Location,
    val destination: Location,
    val distanceKm: Double,
    val estimatedMinutes: Int,
)
