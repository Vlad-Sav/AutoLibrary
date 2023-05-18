package kg.android.autolibrary.data.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import kg.android.autolibrary.data.dao.CarsDao
import kg.android.autolibrary.data.models.Car
import kg.android.autolibrary.data.models.UserPermissions

class CarsRepository(private val carsDao: CarsDao) {
    val readAllCars: LiveData<List<Car>> = carsDao.readAllCars()

    fun readCar(id: Int): LiveData<Car> = carsDao.readCar(id)

    fun readUserPermissions(): LiveData<UserPermissions> = carsDao.readUserPermissions()

    suspend fun addCar(car: Car) {
        carsDao.addCar(car)
    }
}