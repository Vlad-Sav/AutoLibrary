package kg.android.autolibrary.ui.cars

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kg.android.autolibrary.R
import kg.android.autolibrary.data.models.Car
import kg.android.autolibrary.databinding.FragmentCarsBinding
import kg.android.autolibrary.ui.addcar.AddCarFragmentDirections
import kg.android.autolibrary.ui.cardetails.CarDetailsFragmentArgs

@AndroidEntryPoint
class CarsFragment : Fragment(), OnCarItemClicked {
    private lateinit var viewModel: CarsViewModel
    private var _binding: FragmentCarsBinding? = null
    private val binding get() = _binding!!
    private var resetSettings = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCarsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(CarsViewModel::class.java)
        val bundle = arguments ?: return binding.root
        val args = CarsFragmentArgs.fromBundle(bundle)
        resetSettings = args.resetSettings
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
        setUpMenu()
        viewModel.readUserPermissions()
        viewModel.perms.observe(viewLifecycleOwner) { perms ->
            if(perms != null){

            }
        }
        if(resetSettings && viewModel.perms.value != null) viewModel.resetSettings(viewModel.perms.value!![0])


    }

    override fun onCarItemClicked(car: Car) {
        val directions =
            CarsFragmentDirections.actionCarsFragmentToCarDetailsFragment(car)
        findNavController().navigate(directions)
    }

    private fun addCarOnClick(){
        val directions =
            CarsFragmentDirections.actionCarsFragmentToAddCarFragment()
        findNavController().navigate(directions)
    }

    private fun setUpMenu(){
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.nav_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.settings_menu -> {
                        val directions =
                            CarsFragmentDirections.actionCarsFragmentToSettingsFragment()
                        findNavController().navigate(directions)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


}