package kg.android.autolibrary.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.android.autolibrary.data.models.Car
import kg.android.autolibrary.data.models.UserPermissions
import kg.android.autolibrary.data.repository.CarsRepository
import kg.android.autolibrary.ui.addcar.AddCarResult
import kg.android.autolibrary.ui.addcar.AddCarState
import kg.android.autolibrary.ui.addcar.AddCarUiEvent
import kg.android.autolibrary.ui.cars.CarsUiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: CarsRepository
): ViewModel() {

    lateinit var perms: LiveData<List<UserPermissions>>

    private val resultChannel = Channel<ResetSettingsResult<Unit>>()
    val resetSettingsResult = resultChannel.receiveAsFlow()

    init {
        readUserPermissions()
    }

    fun onSettingsEvent(event: SettingsUiEvent) {
        when(event) {
            is SettingsUiEvent.ResetSettings -> {
                resetSettings(perms.value!![0])
            }
        }
    }

    fun readUserPermissions(){
        perms = repository.readUserPermissions
    }

    fun resetSettings(userPermissions: UserPermissions){
        viewModelScope.launch {
            try {
                repository.resetSettings(userPermissions)
                resultChannel.send(ResetSettingsResult.ResetSucceed())
            }
            catch (e: Exception){
                resultChannel.send(ResetSettingsResult.Error(e.message ?: "Unknown error"))
            }
        }
    }
}