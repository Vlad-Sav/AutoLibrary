package kg.android.autolibrary.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kg.android.autolibrary.data.dao.CarsDao
import kg.android.autolibrary.data.database.CarsDatabase
import kg.android.autolibrary.data.repository.CarsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideCarsDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        CarsDatabase::class.java,
        "CarsDb"
    ).createFromAsset("database/CarsDb.db").build()

    @Singleton
    @Provides
    fun provideCarsDao(database: CarsDatabase) = database.carsDao()

    @Singleton
    @Provides
    fun provideCarsRepository(carsDao: CarsDao) = CarsRepository(carsDao)
}