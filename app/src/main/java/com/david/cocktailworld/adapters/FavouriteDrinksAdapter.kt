package com.david.cocktailworld.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.david.cocktailworld.R
import com.david.cocktailworld.data.local.db.entities.Recipe

class FavouriteDrinksAdapter: RecyclerView.Adapter<FavouriteDrinksAdapter.ViewHolder>() {

	inner class ViewHolder(
		itemView: View
	): RecyclerView.ViewHolder(itemView){
		val textViewName: TextView = itemView.findViewById(R.id.textView_name)
		val textViewCategory: TextView = itemView.findViewById(R.id.textView_category)
		val imageViewDrink: ImageView = itemView.findViewById(R.id.imageView_drink_image)
	}

	private val differCallBack = object : DiffUtil.ItemCallback<Recipe>(){
		override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
			return oldItem.idDrink == newItem.idDrink
		}

		override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
			return oldItem == newItem
		}
	}
	val differ = AsyncListDiffer(this,differCallBack)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
		LayoutInflater
			.from(parent.context)
			.inflate(R.layout.favourite_recipes_item_layout,parent,false)
	)

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val drink = differ.currentList[position]
		holder.textViewName.text = drink.strDrink
		holder.textViewCategory.text = drink.strCategory

		Glide.with(holder.itemView.context)
			.load(drink.strDrinkThumb)
			.error(R.drawable.cock_tail_thumbnail)
			.into(holder.imageViewDrink)

	}

	override fun getItemCount(): Int {
		return differ.currentList.size
	}
}