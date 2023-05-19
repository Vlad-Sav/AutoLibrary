package kg.android.autolibrary.ui.addcar

/**
 * Represents events that happen on AddCarFragment
 */
sealed class AddCarUiEvent {
    data class CarNameChanged(val value: String): AddCarUiEvent()
    data class ReleaseYearChanged(val value: String): AddCarUiEvent()
    data class EngineCapacityChanged(val value: String): AddCarUiEvent()
    object AddCar: AddCarUiEvent()
    object UpdateUserPermissions: AddCarUiEvent()
}