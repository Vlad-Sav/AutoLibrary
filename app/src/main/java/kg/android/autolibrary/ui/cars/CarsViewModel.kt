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
    var state = MutableLiveData<AddCarState>()
    lateinit var cars: LiveData<List<Car>>
    lateinit var perms: LiveData<List<UserPermissions>>

    private val resultChannel = Channel<AddCarResult<Unit>>()
    val addCarResult = resultChannel.receiveAsFlow()

    init {
        state.postValue(AddCarState())
    }

    fun onAddCarEvent(event: AddCarUiEvent) {
        when(event) {
            is AddCarUiEvent.CarNameChanged -> {
                state.value = state.value?.copy(carName = event.value)
            }
            is AddCarUiEvent.ReleaseYearChanged -> {
                state.value = state.value?.copy(releaseYear = event.value)
            }
            is AddCarUiEvent.EngineCapacityChanged -> {
                state.value = state.value?.copy(engineCap = event.value)
            }
            is AddCarUiEvent.AddCar -> {
                addCar()
            }
        }
    }

    fun onCarsEvent(event: CarsUiEvent) {
        when(event) {
            is CarsUiEvent.SearchCars -> {
                searchCars(event.value)
            }
            is CarsUiEvent.UpdateUserPermissions -> {
                updateUserPermissions()
            }
        }
    }

    fun readAllCars() {
        cars = repository.readAllCars
    }

    fun addCar() {
        viewModelScope.launch {
            val currentTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formatted = currentTime.format(formatter)
            try {
                repository.addCar(
                    Car(
                        0,
                        state.value?.carName ?: "",
                        "",
                        state.value?.releaseYear?.toInt() ?: 0,
                        state.value?.engineCap?.toDouble() ?: 0.0,
                        formatted)
                )
                resultChannel.send(AddCarResult.Added())
            }
            catch (e: Exception){
                resultChannel.send(AddCarResult.Error(e.message ?: "Unknown error"))
            }
        }
    }

    fun readUserPermissions(){
        perms = repository.readUserPermissions
    }

    fun updateUserPermissions(){
        viewModelScope.launch {
            repository.updateUserPermissions(perms.value!![0])
        }
    }

    fun searchCars(query: String):LiveData<List<Car>>{
        return repository.searchCar(query).asLiveData()
    }
}