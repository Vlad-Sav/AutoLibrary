package kg.android.autolibrary.ui.cardetails

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kg.android.autolibrary.R
import kg.android.autolibrary.data.models.Car
import kg.android.autolibrary.databinding.FragmentAddCarBinding
import kg.android.autolibrary.databinding.FragmentCarDetailsBinding
import kg.android.autolibrary.ui.cars.CarsViewModel
import java.lang.Exception

class CarDetailsFragment : Fragment() {
    private var _binding: FragmentCarDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var car: Car

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCarDetailsBinding.inflate(inflater, container, false)
        //Getting model from args
        val bundle = arguments ?: return binding.root
        val args = CarDetailsFragmentArgs.fromBundle(bundle)
        car = args.car
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.exactCarNameTv.text = car.name
        binding.exactCarYearTv.text = car.releaseYear.toString()
        binding.exactEngineCapTv.text = car.engineCapacity.toString()
        binding.exactInsertDateTv.text = car.insertDate
        if(!car.photo.isNullOrEmpty()){
            try{
                val imageAsBytes: ByteArray = Base64.decode(car.photo, Base64.DEFAULT)
                binding.carIv.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size))
            }
            catch (e: Exception){
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}