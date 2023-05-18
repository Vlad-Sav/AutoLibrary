package kg.android.autolibrary.data.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "cars")
data class Car(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val photo: String?,
    val releaseYear: Int?,
    val engineCapacity: Double?,
    val insertDate: String?
    )
