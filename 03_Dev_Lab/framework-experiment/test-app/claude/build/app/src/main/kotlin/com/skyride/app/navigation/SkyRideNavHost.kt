package com.skyrik.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.skyrik.feature.booking.BookingViewModel
import com.skyrik.feature.booking.ui.DestinationScreen
import com.skyrik.feature.booking.ui.HomeScreen
import com.skyrik.feature.booking.ui.PickupScreen
import com.skyrik.feature.booking.ui.ScheduleScreen
import com.skyrik.feature.confirmation.ui.ConfirmationScreen
import com.skyrik.feature.confirmation.ui.TrackingScreen
import com.skyrik.feature.pricing.ui.PricingScreen
import com.skyrik.feature.rideselection.ui.RideSelectionScreen

/**
 * Root NavHost for Skyrik.
 *
 * Architecture:
 *  - Booking nested graph: shares a single [BookingViewModel] scoped to the graph's
 *    back-stack entry lifetime. All booking steps read/write the same state machine
 *    without re-creation on navigation.
 *  - ActiveRide nested graph: separated so Tracking/Confirmation can be reached
 *    independently (e.g. via deep-link from a push notification).
 */
@Composable
fun SkyrikNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = BookingGraph,
        modifier = modifier,
    ) {
        // ── Booking nested graph ──────────────────────────────────────────────
        navigation<BookingGraph>(startDestination = HomeRoute) {

            composable<HomeRoute> { backStackEntry ->
                val parentEntry = rememberParentEntry(navController, backStackEntry, BookingGraph)
                val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)

                HomeScreen(
                    viewModel = bookingViewModel,
                    onStartBooking = { navController.navigate(PickupRoute) },
                )
            }

            composable<PickupRoute> { backStackEntry ->
                val parentEntry = rememberParentEntry(navController, backStackEntry, BookingGraph)
                val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)

                PickupScreen(
                    viewModel = bookingViewModel,
                    onPickupConfirmed = { navController.navigate(DestinationRoute) },
                    onBack = { navController.popBackStack() },
                )
            }

            composable<DestinationRoute> { backStackEntry ->
                val parentEntry = rememberParentEntry(navController, backStackEntry, BookingGraph)
                val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)

                DestinationScreen(
                    viewModel = bookingViewModel,
                    onDestinationConfirmed = { navController.navigate(ScheduleRoute) },
                    onBack = { navController.popBackStack() },
                )
            }

            composable<ScheduleRoute> { backStackEntry ->
                val parentEntry = rememberParentEntry(navController, backStackEntry, BookingGraph)
                val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)

                ScheduleScreen(
                    viewModel = bookingViewModel,
                    onScheduleConfirmed = { navController.navigate(RideSelectionRoute) },
                    onBack = { navController.popBackStack() },
                )
            }

            composable<RideSelectionRoute> { backStackEntry ->
                val parentEntry = rememberParentEntry(navController, backStackEntry, BookingGraph)
                val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)

                RideSelectionScreen(
                    viewModel = bookingViewModel,
                    onSelectionConfirmed = { navController.navigate(PricingRoute) },
                    onBack = { navController.popBackStack() },
                )
            }

            composable<PricingRoute> { backStackEntry ->
                val parentEntry = rememberParentEntry(navController, backStackEntry, BookingGraph)
                val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)

                PricingScreen(
                    viewModel = bookingViewModel,
                    onConfirmed = { bookingRef ->
                        navController.navigate(ConfirmationRoute(bookingRef)) {
                            // Clear booking graph from back stack after confirming
                            popUpTo(BookingGraph) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() },
                )
            }
        }

        // ── Active-ride nested graph ──────────────────────────────────────────
        navigation<ActiveRideGraph>(startDestination = ConfirmationRoute("")) {

            composable<ConfirmationRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<ConfirmationRoute>()
                ConfirmationScreen(
                    bookingRef = route.bookingRef,
                    onTrackFlight = {
                        navController.navigate(TrackingRoute(route.bookingRef))
                    },
                    onDone = {
                        navController.navigate(BookingGraph) {
                            popUpTo(ActiveRideGraph) { inclusive = true }
                        }
                    },
                )
            }

            composable<TrackingRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<TrackingRoute>()
                TrackingScreen(
                    bookingRef = route.bookingRef,
                    onCancelRide = {
                        navController.navigate(BookingGraph) {
                            popUpTo(ActiveRideGraph) { inclusive = true }
                        }
                    },
                )
            }
        }
    }
}

/**
 * Helper to retrieve the parent (graph-level) back-stack entry in a composable context,
 * enabling graph-scoped ViewModels (i.e., [BookingViewModel] shared across booking steps).
 */
@Composable
private fun rememberParentEntry(
    navController: NavHostController,
    currentEntry: NavBackStackEntry,
    parentRoute: Any,
): NavBackStackEntry = remember(currentEntry) {
    navController.getBackStackEntry(parentRoute)
}
