package com.example.cocktailworld.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cocktailworld.R
import com.example.cocktailworld.model.Drink

class PopularDrinksAdapter(private val onItemClick: (position: Int) -> Unit): RecyclerView.Adapter<PopularDrinksAdapter.ViewHolder>() {

	class ViewHolder(
		itemView: View,
		private val onItemClick: (position: Int) -> Unit
	): RecyclerView.ViewHolder(itemView), View.OnClickListener {
		val textViewName: TextView
		val textViewCategory: TextView
		val imageViewDrink: ImageView
		init {
			textViewName = itemView.findViewById(R.id.textView_name)
			textViewCategory = itemView.findViewById(R.id.textView_category)
			imageViewDrink = itemView.findViewById(R.id.imageView_drink_image)
			itemView.setOnClickListener(this)
		}

		override fun onClick(v: View?) {
			val position = adapterPosition
			onItemClick(position)
		}
	}

	private val differCallBack = object : DiffUtil.ItemCallback<Drink>(){
		override fun areItemsTheSame(oldItem: Drink, newItem: Drink): Boolean {
			return oldItem.idDrink == newItem.idDrink

		}

		override fun areContentsTheSame(oldItem: Drink, newItem: Drink): Boolean {
			return oldItem == newItem
		}
	}
	val differ = AsyncListDiffer(this,differCallBack)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
		LayoutInflater
			.from(parent.context)
			.inflate(R.layout.popular_drink_item_layout,parent,false),
		onItemClick
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