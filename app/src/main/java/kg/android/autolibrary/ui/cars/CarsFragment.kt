package kg.android.autolibrary.ui.cars

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
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
import kg.android.autolibrary.data.models.UserPermissions
import kg.android.autolibrary.databinding.FragmentCarsBinding
import kg.android.autolibrary.ui.base.DialogBuilder

@AndroidEntryPoint
class CarsFragment : Fragment(), OnCarItemClicked, SearchView.OnQueryTextListener {
    private lateinit var viewModel: CarsViewModel
    private var _binding: FragmentCarsBinding? = null
    private val binding get() = _binding!!
    private var userPermissions: UserPermissions? = null
    private lateinit var carsAdapter: CarsRecyclerViewAdapter

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
                carsAdapter = CarsRecyclerViewAdapter(cars, this@CarsFragment)
                binding.carsRecyclerView.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = carsAdapter
                }
            }
        }
        binding.addBtn.setOnClickListener{ addCarOnClick() }
        setUpMenu()
        viewModel.readUserPermissions()
        viewModel.perms.observe(viewLifecycleOwner) { perms ->
            if(perms != null){
                userPermissions = perms[0]
            }
        }
    }

    /**
     * On item click listener for CarsRecyclerView
     */
    override fun onCarItemClicked(car: Car) {
        if(userPermissions?.freeViewCount ?: 0 > 0 || userPermissions?.hasBoughtSubs ?: 0 == 1){
            userPermissions!!.freeViewCount--
            viewModel.onCarsEvent(CarsUiEvent.UpdateUserPermissions)
            val directions =
                CarsFragmentDirections.actionCarsFragmentToCarDetailsFragment(car)
            findNavController().navigate(directions)
        }
        else DialogBuilder().buildDialog(requireContext()){
            userPermissions!!.hasBoughtSubs = 1
            viewModel.onCarsEvent(CarsUiEvent.UpdateUserPermissions)
            Toast.makeText(requireContext(), "Подписка приобретена", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Click listener for Add Car Button
     */
    private fun addCarOnClick(){
        val directions =
            CarsFragmentDirections.actionCarsFragmentToAddCarFragment()
        findNavController().navigate(directions)
    }

    /**
     * App bar menu setting up
     */
    private fun setUpMenu(){
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.nav_menu, menu)
                val search = menu?.findItem(R.id.menu_search)
                val searchView = search?.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@CarsFragment)
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

    /**
     * Search view method implementation
     */
    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()){
            searchCars(query)
        }
        return true
    }

    /**
     * Search view method implementation
     */
    override fun onQueryTextChange(newText: String?): Boolean {
        if(!newText.isNullOrEmpty()){
            searchCars(newText)
        }
        return true
    }

    /**
     * Logic for searching car by name
     *
     * @param query - pattern for searching
     */
    private fun searchCars(query: String) {
        if(!viewModel.cars.value.isNullOrEmpty()){
            val searchQuery = ".*$query.*"
            val filteredCars = viewModel.cars.value!!.filter { car ->
                car.name.matches(Regex(searchQuery, RegexOption.IGNORE_CASE))
            }
            carsAdapter.filteredList(filteredCars)
        }
    }
}