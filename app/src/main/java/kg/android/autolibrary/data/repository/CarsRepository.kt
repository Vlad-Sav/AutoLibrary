package kg.android.autolibrary.data.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import kg.android.autolibrary.data.dao.CarsDao
import kg.android.autolibrary.data.models.Car
import kg.android.autolibrary.data.models.UserPermissions
import kotlinx.coroutines.flow.Flow

class CarsRepository(private val carsDao: CarsDao) {
    val readAllCars: LiveData<List<Car>> = carsDao.readAllCars()

    val readUserPermissions: LiveData<UserPermissions> = carsDao.readUserPermissions()

    suspend fun addCar(car: Car) {
        carsDao.addCar(car)
    }

    suspend fun updateUserPermissions(userPermissions: UserPermissions) {
        carsDao.updateUserPermissions(userPermissions)
    }

    suspend fun resetSettings(userPermissions: UserPermissions) {
        val res = userPermissions.copy(
            freeAddCount = 2,
            freeViewCount = 3,
            hasBoughtSubs = 0)
        carsDao.updateUserPermissions(res)
    }
}