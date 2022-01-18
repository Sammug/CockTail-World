package com.david.cocktailworld.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.david.cocktailworld.R
import com.david.cocktailworld.data.local.db.entities.Drink

class PopularDrinksAdapter(private val onItemClick: (position: Int) -> Unit):
	PagingDataAdapter<Drink,PopularDrinksAdapter.ViewHolder>(DiffCallBack()) {

	class ViewHolder(
		itemView: View,
		private val onItemClick: (position: Int) -> Unit
	): RecyclerView.ViewHolder(itemView), View.OnClickListener {
		val textViewName: TextView = itemView.findViewById(R.id.textView_name)
		val textViewCategory: TextView = itemView.findViewById(R.id.textView_category)
		val imageViewDrink: ImageView = itemView.findViewById(R.id.imageView_drink_image)
		val textViewDrinkTags: TextView = itemView.findViewById(R.id.textView_category_tags)

		init {
			itemView.setOnClickListener(this)
		}

		override fun onClick(v: View?) {
			val position = adapterPosition
			onItemClick(position)
		}
	}

	private class DiffCallBack: DiffUtil.ItemCallback<Drink>(){
		override fun areItemsTheSame(oldItem: Drink, newItem: Drink): Boolean {
			return oldItem.idDrink == newItem.idDrink

		}

		override fun areContentsTheSame(oldItem: Drink, newItem: Drink): Boolean {
			return oldItem == newItem
		}
	}
	//val differ = AsyncPagingDataDiffer(this,differCallBack)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
		LayoutInflater
			.from(parent.context)
			.inflate(R.layout.popular_drinks_item_layout,parent,false),
		onItemClick
	)

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val drink= getItem(position)
		val data: Drink? = drink
		data?.let {
			holder.textViewName.text = it.strDrink
			holder.textViewCategory.text = it.strCategory
			holder.textViewDrinkTags.text = it.strTags

			Glide.with(holder.itemView.context)
				.load(it.strDrinkThumb)
				.error(R.drawable.cock_tail_thumbnail)
				.into(holder.imageViewDrink)
		}
	}
}