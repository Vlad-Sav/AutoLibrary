package kg.android.autolibrary.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kg.android.autolibrary.data.dao.CarsDao
import kg.android.autolibrary.data.models.Car
import kg.android.autolibrary.data.models.UserPermissions

@Database(entities = [Car::class, UserPermissions::class], version = 1, exportSchema = false)
abstract class CarsDatabase: RoomDatabase() {
    abstract fun carsDao(): CarsDao
}