package com.example.cocktailworld.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cocktailworld.R
import com.example.cocktailworld.model.Drinks

class PopularDrinksAdapter(
	private val popularDrinks: ArrayList<Drinks>
): RecyclerView.Adapter<PopularDrinksAdapter.ViewHolder>() {

	class ViewHolder(
		itemView: View
	): RecyclerView.ViewHolder(itemView) {
		val textViewName: TextView
		val textViewCategory: TextView
		val imageViewDrink: ImageView
		init {
			textViewName = itemView.findViewById(R.id.textView_name)
			textViewCategory = itemView.findViewById(R.id.textView_category)
			imageViewDrink = itemView.findViewById(R.id.imageView_drink_image)
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
		LayoutInflater.from(parent.context).inflate(R.layout.popular_drink_item_layout,parent,false)
	)

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val drinks: Drinks = popularDrinks[position]
		holder.textViewName.text = drinks.drinks[position].strDrink
		holder.textViewCategory.text = drinks.drinks[position].strCategory

		Glide.with(holder.itemView.context)
			.load(drinks.drinks[position].strDrinkThumb)
			.error(R.drawable.cock_tail_thumbnail)
			.into(holder.imageViewDrink)

	}

	override fun getItemCount(): Int {
		return popularDrinks.size
	}

	fun addDrinks(drinks: Drinks){
		popularDrinks.addAll(listOf(drinks))
	}
}