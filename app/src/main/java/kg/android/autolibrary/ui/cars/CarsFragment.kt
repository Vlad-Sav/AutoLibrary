package kg.android.autolibrary.ui.cars

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kg.android.autolibrary.R
import kg.android.autolibrary.databinding.FragmentCarsBinding
import kg.android.autolibrary.ui.addcar.AddCarUiEvent

@AndroidEntryPoint
class CarsFragment : Fragment(), IOnCarItemClicked {
    private lateinit var viewModel: CarsViewModel
    private var _binding: FragmentCarsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCarsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(CarsViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.readAllCars()
        viewModel.cars.observe(viewLifecycleOwner) { cars ->
            if(cars != null && cars.isNotEmpty()){
                binding.carsRecyclerView.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = CarsRecyclerViewAdapter(cars, this@CarsFragment)

                }
            }
        }
        binding.addBtn.setOnClickListener{ addCarOnClick() }
    }

    private fun addCarOnClick(){
        val directions =
            CarsFragmentDirections.actionCarsFragmentToAddCarFragment()
        findNavController().navigate(directions)
    }

    override fun onCarItemClicked(id: Int) {

    }


}