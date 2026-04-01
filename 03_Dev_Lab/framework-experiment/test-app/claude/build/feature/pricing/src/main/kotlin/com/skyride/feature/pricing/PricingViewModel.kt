package com.skyrik.feature.pricing

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Local ViewModel for PricingScreen.
 *
 * Manages transient UI state specific to the pricing review screen:
 *  - Payment method sheet visibility
 *  - Terms acceptance toggle
 *
 * All booking data and the confirm action are delegated to the shared [BookingViewModel].
 */
@HiltViewModel
class PricingViewModel @Inject constructor() : ViewModel() {

    private val _showPaymentSheet = MutableStateFlow(false)
    val showPaymentSheet: StateFlow<Boolean> = _showPaymentSheet.asStateFlow()

    private val _termsAccepted = MutableStateFlow(false)
    val termsAccepted: StateFlow<Boolean> = _termsAccepted.asStateFlow()

    fun togglePaymentSheet() {
        _showPaymentSheet.value = !_showPaymentSheet.value
    }

    fun dismissPaymentSheet() {
        _showPaymentSheet.value = false
    }

    fun setTermsAccepted(accepted: Boolean) {
        _termsAccepted.value = accepted
    }
}
