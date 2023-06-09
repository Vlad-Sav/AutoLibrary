package kg.android.autolibrary.ui.addcar

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kg.android.autolibrary.R
import kg.android.autolibrary.data.models.UserPermissions
import kg.android.autolibrary.databinding.FragmentAddCarBinding
import kg.android.autolibrary.ui.base.DialogBuilder
import kg.android.autolibrary.ui.cars.CarsFragmentDirections
import kg.android.autolibrary.ui.cars.CarsUiEvent
import kg.android.autolibrary.ui.cars.CarsViewModel
import java.io.ByteArrayOutputStream
import java.io.InputStream

@AndroidEntryPoint
class AddCarFragment : Fragment() {
    private lateinit var viewModel: AddCarViewModel
    private var _binding: FragmentAddCarBinding? = null
    private val binding get() = _binding!!
    private var userPermissions: UserPermissions? = null
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCarBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(AddCarViewModel::class.java)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                binding.photoUri.text = uri.path
                val base64 = imageToBase64(uri)
                viewModel.onAddCarEvent(AddCarUiEvent.PhotoChanged(base64))
            } else {
                Toast.makeText(requireContext(), requireContext().getText(R.string.photo_load_error), Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeEditText()
        binding.btnAddCar.setOnClickListener{ addCarOnClick() }
        viewModel.readUserPermissions()
        viewModel.perms.observe(viewLifecycleOwner) { perms ->
            if(perms != null){
                userPermissions = perms
            }
        }
        //Listening to result of adding car to database
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addCarResult.collect { result ->
                when (result) {
                    is AddCarResult.Added -> {
                        val directions =
                            AddCarFragmentDirections.actionAddCarFragmentToCarsFragment()
                        findNavController().navigate(directions)
                    }
                    is AddCarResult.Error -> {
                        Toast.makeText(
                            context,
                            result.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        binding.btnAddPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    /**
     * Click listener for Add Car Button
     */
    private fun addCarOnClick(){
        if(userPermissions?.freeAddCount ?: 0 > 0 || userPermissions?.hasBoughtSubs ?: 0 == 1){
            userPermissions!!.freeAddCount--
            viewModel.onAddCarEvent(AddCarUiEvent.UpdateUserPermissions)
            viewModel.onAddCarEvent(AddCarUiEvent.AddCar)
        }
        else  DialogBuilder().buildDialog(requireContext()){
            userPermissions!!.hasBoughtSubs = 1
            viewModel.onAddCarEvent(AddCarUiEvent.UpdateUserPermissions)
            Toast.makeText(requireContext(), requireContext().getText(R.string.subscription_purchased), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Linking edit texts from ui with object that represents state of those edit texts
     */
    private fun initializeEditText(){
        binding.etCarName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onAddCarEvent(AddCarUiEvent.CarNameChanged(s.toString()))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.etCarYear.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onAddCarEvent(AddCarUiEvent.ReleaseYearChanged(s.toString()))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.etEngineCap.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onAddCarEvent(AddCarUiEvent.EngineCapacityChanged(s.toString()))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    /**
     * Converts Image Byte Stream Into Base64 string
     */
    private fun imageToBase64(uri: Uri): String{
        val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
        val buffer = ByteArrayOutputStream()
        val bufferSize = 4048
        val data = ByteArray(bufferSize)
        var bytesRead: Int
        while (inputStream?.read(data, 0, bufferSize).also { bytesRead = it ?: -1 } != -1) {
            buffer.write(data, 0, bytesRead)
        }
        val imageBytes = buffer.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }
}