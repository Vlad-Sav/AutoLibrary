package kg.android.autolibrary.ui.addcar

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kg.android.autolibrary.data.models.UserPermissions
import kg.android.autolibrary.databinding.FragmentAddCarBinding
import kg.android.autolibrary.ui.cars.CarsFragmentDirections
import kg.android.autolibrary.ui.cars.CarsUiEvent
import kg.android.autolibrary.ui.cars.CarsViewModel

class AddCarFragment : Fragment() {
    private lateinit var viewModel: CarsViewModel
    private var _binding: FragmentAddCarBinding? = null
    private val binding get() = _binding!!
    private var userPermissions: UserPermissions? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCarBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(CarsViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeEditText()
        binding.btnAddCar.setOnClickListener{ addCarOnClick() }
        viewModel.readUserPermissions()
        viewModel.perms.observe(viewLifecycleOwner) { perms ->
            if(perms != null){
                userPermissions = perms[0]
            }
        }
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
    }

    private fun addCarOnClick(){
        if(userPermissions?.freeAddCount ?: 0 > 0 || userPermissions?.hasBoughtSubs ?: 0 == 1){
            userPermissions!!.freeAddCount--
            viewModel.onCarsEvent(CarsUiEvent.UpdateUserPermissions)
            viewModel.onAddCarEvent(AddCarUiEvent.AddCar)
        }
        else buildDialog()
    }

    fun buildDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Купите подписку!")
        builder.setMessage("С покупкой подписки все функции станут доступны без ограничений на период в месяц")

        builder.setPositiveButton("Купить") { dialog, which ->
            userPermissions!!.hasBoughtSubs = 1
            viewModel.onCarsEvent(CarsUiEvent.UpdateUserPermissions)
            Toast.makeText(requireContext(), "Подписка приобретена", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Отмена") { dialog, which ->
        }
        builder.show()
    }

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
}