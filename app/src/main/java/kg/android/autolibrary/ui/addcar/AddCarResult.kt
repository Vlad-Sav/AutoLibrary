package kg.android.autolibrary.ui.addcar

/**
 * Hierarchy for representing result of adding car to database
 */
sealed class AddCarResult<T>(val data: T? = null) {
    class Added<T>(data: T? = null): AddCarResult<T>(data)
    class Error<T>(val message: String = "unknown error"): AddCarResult<T>()
}