package kg.android.autolibrary.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kg.android.autolibrary.data.models.Car
import kg.android.autolibrary.data.models.UserPermissions

@Dao
interface CarsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCar(car: Car)

    @Query("SELECT * FROM cars ORDER BY name ASC")
    fun readAllCars(): LiveData<List<Car>>

    @Query("SELECT * FROM cars WHERE id=:arg0")
    fun readCar(id: Int): LiveData<Car>

    @Query("SELECT * FROM userPermissions")
    fun readUserPermissions(): LiveData<UserPermissions>
}