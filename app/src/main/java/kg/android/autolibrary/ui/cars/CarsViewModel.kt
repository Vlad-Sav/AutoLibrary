package kg.android.autolibrary.ui.cars

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.android.autolibrary.data.models.Car
import kg.android.autolibrary.data.models.UserPermissions
import kg.android.autolibrary.data.repository.CarsRepository
import kg.android.autolibrary.ui.addcar.AddCarResult
import kg.android.autolibrary.ui.addcar.AddCarState
import kg.android.autolibrary.ui.addcar.AddCarUiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CarsViewModel @Inject constructor(
    private val repository: CarsRepository
): ViewModel() {
    lateinit var cars: LiveData<List<Car>>
    lateinit var perms: LiveData<List<UserPermissions>>

    /**
     * CarsFragment events processor
     */
    fun onCarsEvent(event: CarsUiEvent) {
        when(event) {
            is CarsUiEvent.UpdateUserPermissions -> {
                updateUserPermissions()
            }
        }
    }

    /**
     * Getting all cars from database
     */
    fun readAllCars() {
        cars = repository.readAllCars
    }

    /**
     * Getting user permissions model
     */
    fun readUserPermissions(){
        perms = repository.readUserPermissions
    }

    /**
     * Updating user permissions model
     */
    private fun updateUserPermissions(){
        viewModelScope.launch {
            repository.updateUserPermissions(perms.value!![0])
        }
    }
}