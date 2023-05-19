package kg.android.autolibrary.ui.settings

/**
 * Represents events that happen on SettingsFragment
 */
sealed class SettingsUiEvent {
    object ResetSettings: SettingsUiEvent()
}