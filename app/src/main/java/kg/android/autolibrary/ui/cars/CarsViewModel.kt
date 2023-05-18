package kg.android.autolibrary.ui.cars

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.android.autolibrary.data.models.Car
import kg.android.autolibrary.data.repository.CarsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarsViewModel @Inject constructor(
    private val repository: CarsRepository
): ViewModel() {
    lateinit var cars: LiveData<List<Car>>

    init {
        readAllCars()
    }

    fun readAllCars() {
            cars = repository.readAllCars

    }

    fun addCar() {
        viewModelScope.launch {
            val result = repository.addCar(Car(7,"car","",2005,10.0,""))
        }
    }
}