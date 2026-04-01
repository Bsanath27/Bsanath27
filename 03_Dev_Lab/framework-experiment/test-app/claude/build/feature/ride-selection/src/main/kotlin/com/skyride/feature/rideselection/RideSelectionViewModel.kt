package com.skyrik.feature.rideselection

import androidx.lifecycle.ViewModel
import com.skyrik.core.data.model.Helicopter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Local ViewModel for RideSelectionScreen.
 *
 * Manages transient UI state (selected helicopter highlight, filter chip state)
 * that doesn't need to survive navigation. The actual helicopter list and
 * booking actions are delegated to the graph-scoped [BookingViewModel].
 */
@HiltViewModel
class RideSelectionViewModel @Inject constructor() : ViewModel() {

    private val _selectedHelicopterId = MutableStateFlow<String?>(null)
    val selectedHelicopterId: StateFlow<String?> = _selectedHelicopterId.asStateFlow()

    private val _activeFilter = MutableStateFlow<HelicopterFilter>(HelicopterFilter.ALL)
    val activeFilter: StateFlow<HelicopterFilter> = _activeFilter.asStateFlow()

    fun selectHelicopter(id: String) {
        _selectedHelicopterId.value = id
    }

    fun setFilter(filter: HelicopterFilter) {
        _activeFilter.value = filter
    }

    fun filteredHelicopters(helicopters: List<Helicopter>): List<Helicopter> {
        return when (_activeFilter.value) {
            HelicopterFilter.ALL -> helicopters
            HelicopterFilter.STANDARD -> helicopters.filter {
                it.category == com.skyrik.core.data.model.HelicopterCategory.STANDARD
            }
            HelicopterFilter.EXECUTIVE -> helicopters.filter {
                it.category == com.skyrik.core.data.model.HelicopterCategory.EXECUTIVE
            }
            HelicopterFilter.VIP -> helicopters.filter {
                it.category == com.skyrik.core.data.model.HelicopterCategory.VIP
            }
        }
    }
}

enum class HelicopterFilter(val label: String) {
    ALL("All"),
    STANDARD("Standard"),
    EXECUTIVE("Executive"),
    VIP("VIP"),
}
