package kg.android.autolibrary.ui.cars

sealed class CarsUiEvent {
    object UpdateUserPermissions: CarsUiEvent()
    data class SearchCars(val value: String):CarsUiEvent()
}