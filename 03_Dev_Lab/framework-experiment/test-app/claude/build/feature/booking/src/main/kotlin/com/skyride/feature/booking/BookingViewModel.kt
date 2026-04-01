package com.skyrik.feature.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyrik.core.data.model.BookingState
import com.skyrik.core.data.model.Helicopter
import com.skyrik.core.data.model.PaymentMethod
import com.skyrik.core.data.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Shared [ViewModel] scoped to the booking nested navigation graph.
 *
 * Manages the [BookingState] state machine transitions and exposes:
 *  - [bookingState]  — current state (StateFlow, UI observes this)
 *  - [uiEvents]      — one-shot side effects (navigation signals, toasts)
 *
 * [SavedStateHandle] persists partial booking state across process death.
 */
@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // ── State ─────────────────────────────────────────────────────────────────

    private val _bookingState = MutableStateFlow<BookingState>(BookingState.Idle)
    val bookingState: StateFlow<BookingState> = _bookingState.asStateFlow()

    // Loading flag for async operations
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // ── One-shot UI events ────────────────────────────────────────────────────

    sealed class UiEvent {
        data class NavigateToRideSelection(val helicopters: List<Helicopter>) : UiEvent()
        data class NavigateToConfirmation(val bookingRef: String) : UiEvent()
        data class ShowError(val message: String) : UiEvent()
        data object NavigateBack : UiEvent()
    }

    private val _uiEvents = Channel<UiEvent>(Channel.BUFFERED)
    val uiEvents = _uiEvents.receiveAsFlow()

    // ── State transitions ─────────────────────────────────────────────────────

    /** Called when the user enters/confirms a pickup location. */
    fun setPickup(pickup: String) {
        _bookingState.update { BookingState.LocationEntry(pickup = pickup) }
        savedStateHandle["pickup"] = pickup
    }

    /** Called when the user enters/confirms a destination. */
    fun setDestination(destination: String) {
        val currentPickup = when (val s = _bookingState.value) {
            is BookingState.LocationEntry -> s.pickup ?: return
            is BookingState.ScheduleEntry -> s.pickup
            else -> savedStateHandle.get<String>("pickup") ?: return
        }
        _bookingState.update {
            BookingState.ScheduleEntry(
                pickup = currentPickup,
                destination = destination,
            )
        }
        savedStateHandle["destination"] = destination
    }

    /** Called when the user confirms date/time — triggers helicopter fetch. */
    fun setSchedule(scheduledTime: LocalDateTime) {
        val state = _bookingState.value as? BookingState.ScheduleEntry ?: return
        val destination = state.destination ?: return

        _bookingState.update {
            BookingState.RideSelection(
                pickup = state.pickup,
                destination = destination,
                scheduledTime = scheduledTime,
                availableHelicopters = emptyList(),
            )
        }

        viewModelScope.launch {
            _isLoading.value = true
            bookingRepository.getAvailableHelicopters(
                pickup = state.pickup,
                destination = destination,
                scheduledTime = scheduledTime,
            ).fold(
                onSuccess = { helicopters ->
                    _bookingState.update { current ->
                        (current as? BookingState.RideSelection)?.copy(
                            availableHelicopters = helicopters
                        ) ?: current
                    }
                },
                onFailure = { error ->
                    _bookingState.update {
                        BookingState.Error(
                            message = error.message ?: "Failed to load helicopters",
                            previousState = it,
                        )
                    }
                    _uiEvents.send(UiEvent.ShowError(error.message ?: "Failed to load helicopters"))
                },
            )
            _isLoading.value = false
        }
    }

    /** Called when the user selects a helicopter from the list. */
    fun selectHelicopter(helicopter: Helicopter) {
        val state = _bookingState.value as? BookingState.RideSelection ?: return

        _bookingState.update {
            BookingState.PricingReview(
                pickup = state.pickup,
                destination = state.destination,
                scheduledTime = state.scheduledTime,
                selectedHelicopter = helicopter,
                pricingBreakdown = null,
            )
        }

        // Eagerly fetch pricing and payment methods
        viewModelScope.launch {
            _isLoading.value = true

            val pricingResult = bookingRepository.getPricingBreakdown(
                helicopterId = helicopter.id,
                pickup = state.pickup,
                destination = state.destination,
            )
            val paymentResult = bookingRepository.getPaymentMethods()

            pricingResult.fold(
                onSuccess = { pricing ->
                    _bookingState.update { current ->
                        (current as? BookingState.PricingReview)?.copy(
                            pricingBreakdown = pricing,
                            selectedPaymentMethod = paymentResult.getOrNull()
                                ?.firstOrNull { it.isDefault },
                        ) ?: current
                    }
                },
                onFailure = { error ->
                    _uiEvents.send(UiEvent.ShowError(error.message ?: "Failed to load pricing"))
                },
            )
            _isLoading.value = false
        }
    }

    /** Called when the user selects a payment method. */
    fun selectPaymentMethod(paymentMethod: PaymentMethod) {
        _bookingState.update { current ->
            (current as? BookingState.PricingReview)?.copy(
                selectedPaymentMethod = paymentMethod,
            ) ?: current
        }
    }

    /** Submits the booking — transitions through Confirming → Confirmed | Error. */
    fun confirmBooking() {
        val state = _bookingState.value as? BookingState.PricingReview ?: return
        val paymentMethod = state.selectedPaymentMethod ?: run {
            viewModelScope.launch {
                _uiEvents.send(UiEvent.ShowError("Please select a payment method"))
            }
            return
        }

        _bookingState.update { BookingState.Confirming }

        viewModelScope.launch {
            bookingRepository.confirmBooking(
                helicopterId     = state.selectedHelicopter.id,
                pickup           = state.pickup,
                destination      = state.destination,
                scheduledTime    = state.scheduledTime,
                paymentMethodId  = paymentMethod.id,
            ).fold(
                onSuccess = { bookingRef ->
                    _bookingState.update {
                        BookingState.Confirmed(
                            bookingRef    = bookingRef,
                            helicopter    = state.selectedHelicopter,
                            pickup        = state.pickup,
                            destination   = state.destination,
                            scheduledTime = state.scheduledTime,
                        )
                    }
                    _uiEvents.send(UiEvent.NavigateToConfirmation(bookingRef))
                },
                onFailure = { error ->
                    _bookingState.update {
                        BookingState.Error(
                            message = error.message ?: "Booking failed. Please try again.",
                            previousState = state,
                        )
                    }
                    _uiEvents.send(UiEvent.ShowError(error.message ?: "Booking failed"))
                },
            )
        }
    }

    /** Retry after an error — restores the previous state. */
    fun retry() {
        val errorState = _bookingState.value as? BookingState.Error ?: return
        _bookingState.update { errorState.previousState ?: BookingState.Idle }
    }

    /** Reset the entire booking flow. */
    fun reset() {
        _bookingState.update { BookingState.Idle }
        savedStateHandle.remove<String>("pickup")
        savedStateHandle.remove<String>("destination")
    }
}
