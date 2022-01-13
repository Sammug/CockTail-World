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
import com.david.cocktailworld.model.Drink

class TopFivePopularDrinksAdapter(private val onItemClick: (position: Int) -> Unit): RecyclerView.Adapter<TopFivePopularDrinksAdapter.ViewHolder>() {

	class ViewHolder(
		itemView: View,
		private val onItemClick: (position: Int) -> Unit
	): RecyclerView.ViewHolder(itemView), View.OnClickListener {
		val textViewName: TextView = itemView.findViewById(R.id.textView_name)
		val textViewCategory: TextView = itemView.findViewById(R.id.textView_category)
		val imageViewDrink: ImageView = itemView.findViewById(R.id.imageView_drink_image)

		init {
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
			.inflate(R.layout.top_five_popular_drinks_item_layout,parent,false),
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