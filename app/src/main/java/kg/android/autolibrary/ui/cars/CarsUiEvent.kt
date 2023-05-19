package kg.android.autolibrary.ui.cars

/**
 * Represents events that happen on AddCarFragment
 */
sealed class CarsUiEvent {
    object UpdateUserPermissions: CarsUiEvent()
}