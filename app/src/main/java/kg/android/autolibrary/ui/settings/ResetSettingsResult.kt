package kg.android.autolibrary.ui.settings

/**
 * Hierarchy for representing result of settings reset in database
 */
sealed class ResetSettingsResult<T>(val data: T? = null) {
    class ResetSucceed<T>(data: T? = null): ResetSettingsResult<T>(data)
    class Error<T>(val message: String = "unknown error"): ResetSettingsResult<T>()
}