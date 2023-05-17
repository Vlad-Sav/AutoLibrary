package kg.android.autolibrary.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kg.android.autolibrary.data.models.Car
import kg.android.autolibrary.data.models.UserPermissions

@Database(entities = [Car::class, UserPermissions::class], version = 1, exportSchema = false)
abstract class CarsDatabase: RoomDatabase() {
    abstract fun carsDao()

    companion object {
        @Volatile
        private var INSTANSE: CarsDatabase? = null

        fun getDatabase(context: Context): CarsDatabase{
            val tempInstance = INSTANSE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CarsDatabase::class.java,
                    "cars database"
                ).build()
                INSTANSE = instance
                return instance
            }
        }
    }
}