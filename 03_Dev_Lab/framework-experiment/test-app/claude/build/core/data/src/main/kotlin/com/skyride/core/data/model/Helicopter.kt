package com.skyrik.core.data.model

/**
 * Domain model representing a helicopter available for booking.
 *
 * @param id             Unique identifier
 * @param name           Display name (e.g. "Bell 407")
 * @param category       Service tier (Standard, Executive, VIP)
 * @param seats          Maximum passenger capacity
 * @param speedKmh       Cruising speed in km/h
 * @param rangeKm        Maximum range in km
 * @param pricePerKm     Base per-km pricing in INR
 * @param baseFare       Minimum flat fare in INR
 * @param imageUrl       URL for the helicopter illustration (loaded via Coil)
 * @param amenities      List of included amenities
 * @param pilotName      Assigned pilot's display name
 * @param pilotRating    Pilot rating out of 5.0
 */
data class Helicopter(
    val id: String,
    val name: String,
    val category: HelicopterCategory,
    val seats: Int,
    val speedKmh: Int,
    val rangeKm: Int,
    val pricePerKm: Double,
    val baseFare: Double,
    val imageUrl: String,
    val amenities: List<String>,
    val pilotName: String,
    val pilotRating: Float,
)

enum class HelicopterCategory(val displayName: String) {
    STANDARD("Standard"),
    EXECUTIVE("Executive"),
    VIP("VIP"),
}
