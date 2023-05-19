package kg.android.autolibrary.ui.cars

import kg.android.autolibrary.data.models.Car

/**
 * CarsRecyclerView On Item Click Listener
 */
interface OnCarItemClicked {
    fun onCarItemClicked(car: Car)
}