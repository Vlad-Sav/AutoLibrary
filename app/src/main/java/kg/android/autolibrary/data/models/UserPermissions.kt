package kg.android.autolibrary.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userPermissions")
data class UserPermissions(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val freeAddCount: Int,
    val freeViewCount: Int,
    val hasBoughtSubs: Int
    )