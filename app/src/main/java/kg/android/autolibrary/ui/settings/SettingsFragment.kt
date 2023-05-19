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
    private lateinit var viewModel: SettingsViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val pref: Preference? = findPreference("reset_settings")
        pref?.setOnPreferenceClickListener {
            viewModel.perms.observe(viewLifecycleOwner) { perms ->
                if(perms != null){
                    viewModel.resetSettings(viewModel.perms.value!!)
                }
            }
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        //Listening to result of settings reset in database
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.resetSettingsResult.collect { result ->
                when (result) {
                    is ResetSettingsResult.ResetSucceed -> {
                        val directions = SettingsFragmentDirections
                            .actionSettingsFragmentToCarsFragment()
                        findNavController().navigate(directions)
                    }
                    is ResetSettingsResult.Error -> {
                        Toast.makeText(
                            context,
                            result.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}
