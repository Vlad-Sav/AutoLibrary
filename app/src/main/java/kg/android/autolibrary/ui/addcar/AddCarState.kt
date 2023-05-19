package kg.android.autolibrary.ui.addcar

/**
 * Represents state of edit text in adding car form
 */
data class AddCarState(
    val carName: String = "",
    val releaseYear: String = "",
    val engineCap: String = ""
)