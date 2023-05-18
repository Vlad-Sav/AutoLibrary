package kg.android.autolibrary.ui.cars

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.android.autolibrary.data.models.Car
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
}