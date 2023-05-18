package kg.android.autolibrary.ui.cars

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kg.android.autolibrary.R
import kg.android.autolibrary.databinding.FragmentCarsBinding

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
        viewModel.cars.observe(viewLifecycleOwner) { cars ->
            if(cars != null && cars.isNotEmpty()){
                binding.carsRecyclerView.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = CarsRecyclerViewAdapter(cars, this@CarsFragment)

                }
            }
        }
        viewModel.addCar()
        viewModel.readAllCars()
    }

    override fun onCarItemClicked(id: Int) {

    }
}