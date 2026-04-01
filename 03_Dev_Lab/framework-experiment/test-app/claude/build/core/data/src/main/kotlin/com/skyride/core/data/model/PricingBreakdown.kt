package com.skyrik.core.data.model

/**
 * Full pricing breakdown for a booking, displayed on the PricingScreen.
 *
 * @param baseFare         Flat booking fee
 * @param distanceFare     Per-km charge × distance
 * @param taxes            GST and other applicable taxes
 * @param convenienceFee   Platform service fee
 * @param discount         Applied discount (negative value)
 * @param totalAmount      Final payable amount
 * @param currency         ISO 4217 currency code (default: INR)
 */
data class PricingBreakdown(
    val baseFare: Double,
    val distanceFare: Double,
    val taxes: Double,
    val convenienceFee: Double,
    val discount: Double = 0.0,
    val totalAmount: Double,
    val currency: String = "INR",
) {
    /** Returns true if a discount is applied. */
    val hasDiscount: Boolean get() = discount < 0.0

    /** Formats a [Double] to an INR string like "₹ 12,500". */
    fun formatAmount(amount: Double): String {
        val absAmount = Math.abs(amount)
        val prefix = if (amount < 0) "-₹ " else "₹ "
        return "$prefix${"%,.0f".format(absAmount)}"
    }
}

/**
 * Represents a payment method option.
 */
data class PaymentMethod(
    val id: String,
    val type: PaymentType,
    val displayName: String,
    val last4: String? = null,
    val isDefault: Boolean = false,
)

enum class PaymentType {
    CARD,
    UPI,
    NET_BANKING,
    WALLET,
}
