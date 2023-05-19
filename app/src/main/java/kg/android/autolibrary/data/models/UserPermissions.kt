package kg.android.autolibrary.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userPermissions")
data class UserPermissions(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var freeAddCount: Int,
    var freeViewCount: Int,
    var hasBoughtSubs: Int
    )