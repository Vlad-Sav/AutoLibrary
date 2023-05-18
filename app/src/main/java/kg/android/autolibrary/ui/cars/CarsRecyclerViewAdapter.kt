package kg.android.autolibrary.ui.cars

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kg.android.autolibrary.R
import kg.android.autolibrary.data.models.Car

class CarsRecyclerViewAdapter(private val cars: List<Car>,
                              private val onCarItemClicked: IOnCarItemClicked
                              ): RecyclerView.Adapter<CarsRecyclerViewAdapter.CarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.car_recycler_view_item, parent, false)
        return CarViewHolder(v)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(cars[position], onCarItemClicked)
    }

    override fun getItemCount(): Int {
        return cars.count()
    }

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val carItemNameTv: TextView = itemView.findViewById(R.id.car_item_name_tv)
        private val carYearNameTv: TextView = itemView.findViewById(R.id.car_item_year_tv)

        fun bind(car: Car, onCarItemClicked: IOnCarItemClicked) {
            carItemNameTv.text = car.name
            carYearNameTv.text = car.releaseYear.toString()
            itemView.setOnClickListener {
                onCarItemClicked.onCarItemClicked(car.id)
            }
        }
    }
}