package com.david.cocktailworld.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.david.cocktailworld.R
import com.david.cocktailworld.adapters.DrinksLoadingStateAdapter
import com.david.cocktailworld.adapters.PopularDrinksAdapter
import com.david.cocktailworld.databinding.FragmentPopularDrinksBinding
import com.david.cocktailworld.ui.viewmodels.MainViewModel
import com.david.cocktailworld.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CreateRecipeFragment : Fragment() {
	private var _binding: FragmentPopularDrinksBinding? = null
	private val binding: FragmentPopularDrinksBinding
	get() = _binding!!

	private lateinit var popularDrinksAdapter: PopularDrinksAdapter
	private val mainViewModel: MainViewModel by viewModels()
	private lateinit var navController: NavController

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentPopularDrinksBinding.inflate(inflater,container,false)
		return binding.root
	}

	@ExperimentalPagingApi
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.progressBar.isVisible = false
		navController = binding.root.findNavController()
		popularDrinksAdapter = PopularDrinksAdapter { position -> onPopularDrinkItemClicked(position) }
		setUpRecyclerView()
		getPagedData()
	}
	@ExperimentalPagingApi
	private fun getPagedData(){
		lifecycleScope.launch {
			mainViewModel.fetchPagedPopularDrinks().collectLatest {
				popularDrinksAdapter.submitData(it)
			}
		}
	}

	private fun setUpRecyclerView() {
		val layoutManager = LinearLayoutManager(activity)
		binding.recyclerViewPopularDrinks.layoutManager = layoutManager
		binding.recyclerViewPopularDrinks.adapter = popularDrinksAdapter.withLoadStateFooter(
			footer = DrinksLoadingStateAdapter{ retry() }
		)

		popularDrinksAdapter.addLoadStateListener {loadState ->

			if (loadState.mediator?.refresh is LoadState.Loading) {

				if (popularDrinksAdapter.snapshot().isEmpty()) {
					binding.progressBar.isVisible = true
				}
				binding.errorTxt.isVisible = false

			} else {
				binding.progressBar.isVisible = false
				binding.swipeRefreshLayout.isRefreshing = false
				val error = when {
					loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
					loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
					loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error

					else -> null
				}
				error?.let {
					if (popularDrinksAdapter.snapshot().isEmpty()) {
						binding.errorTxt.isVisible = true
						binding.errorTxt.text = it.error.localizedMessage
					}
				}
			}
		}
	}
	private fun retry(){
		popularDrinksAdapter.retry()
	}

	private fun onPopularDrinkItemClicked(position: Int) {

		val bundle = bundleOf("ID" to id)
		navController.navigate(R.id.action_createRecipeFragment_to_drinkDetailsFragment,bundle)
	}
}