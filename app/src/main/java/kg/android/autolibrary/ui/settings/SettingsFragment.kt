package kg.android.autolibrary.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import kg.android.autolibrary.R
import kg.android.autolibrary.ui.addcar.AddCarFragmentDirections
import kg.android.autolibrary.ui.addcar.AddCarResult
import kg.android.autolibrary.ui.cars.CarsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val pref: Preference? = findPreference("reset_settings")
        pref?.setOnPreferenceClickListener {
            val directions = SettingsFragmentDirections
                .actionSettingsFragmentToCarsFragment().setResetSettings(true)
            findNavController().navigate(directions)
            true
        }

    }

}
