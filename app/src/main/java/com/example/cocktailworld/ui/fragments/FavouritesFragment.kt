package com.example.cocktailworld.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktailworld.adapters.FavouriteDrinksAdapter
import com.example.cocktailworld.databinding.FragmentFavouritesBinding
import com.example.cocktailworld.ui.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment() {
	private var _binding: FragmentFavouritesBinding? = null
	val binding: FragmentFavouritesBinding
	get() = _binding!!

	private lateinit var favouriteDrinksAdapter: FavouriteDrinksAdapter

	private val mainViewModel: MainViewModel by viewModels()


	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentFavouritesBinding.inflate(inflater,container,false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		favouriteDrinksAdapter = FavouriteDrinksAdapter()
		setUpRecyclerView()

		mainViewModel.getAllFavDrinks().observe(viewLifecycleOwner,{favouriteDrinks ->
				favouriteDrinksAdapter.differ.submitList(favouriteDrinks)
		})

		val onItemTouchHelperCallBack  = object : ItemTouchHelper.SimpleCallback(
			ItemTouchHelper.UP or ItemTouchHelper.DOWN,
			ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
		) {
			override fun onMove(
				recyclerView: RecyclerView,
				viewHolder: RecyclerView.ViewHolder,
				target: RecyclerView.ViewHolder
			): Boolean {
				return true
			}

			override fun onSwiped(
				viewHolder: RecyclerView.ViewHolder,
				direction: Int) {
				val position = viewHolder.adapterPosition
				val recipe = favouriteDrinksAdapter.differ.currentList[position]
				mainViewModel.deleteFavRecipe(recipe)

				Snackbar.make(binding.root,"1 Item deleted",Snackbar.LENGTH_LONG).apply {
					setAction("UNDO"){
						mainViewModel.addToFavourites(recipe)
					}
					show()
				}
			}
		}
		swipeItemToDelete(onItemTouchHelperCallBack)
	}

	private fun swipeItemToDelete(onItemTouchHelperCallBack: ItemTouchHelper.SimpleCallback) {
		ItemTouchHelper(onItemTouchHelperCallBack).apply {
			attachToRecyclerView(binding.recyclerViewFavorites)

		}
	}

	private fun setUpRecyclerView() {
		binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(activity)
		binding.recyclerViewFavorites.hasFixedSize()
		binding.recyclerViewFavorites.adapter = favouriteDrinksAdapter
	}
}
