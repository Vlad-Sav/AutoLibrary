package kg.android.autolibrary.ui.addcar

sealed class AddCarUiEvent {
    data class CarNameChanged(val value: String): AddCarUiEvent()
    data class ReleaseYearChanged(val value: String): AddCarUiEvent()
    data class EngineCapacityChanged(val value: String): AddCarUiEvent()
    object AddCar: AddCarUiEvent()
}