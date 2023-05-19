package kg.android.autolibrary.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kg.android.autolibrary.data.models.Car
import kg.android.autolibrary.data.models.UserPermissions
import kotlinx.coroutines.flow.Flow

@Dao
interface CarsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCar(car: Car)

    @Update
    suspend fun updateUserPermissions(userPermissions: UserPermissions)

    @Query("SELECT * FROM cars ORDER BY id ASC")
    fun readAllCars(): LiveData<List<Car>>

    @Query("SELECT * FROM cars WHERE id=:id")
    fun readCar(id: Int): LiveData<Car>

    @Query("SELECT * FROM userPermissions")
    fun readUserPermissions(): LiveData<List<UserPermissions>>

    @Query("SELECT * FROM cars WHERE name LIKE :searchQuery")
    fun searchCar(searchQuery: String): Flow<List<Car>>
}