package com.david.cocktailworld.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.david.cocktailworld.R
import com.google.android.material.button.MaterialButton

class DrinksLoadingStateAdapter(private val retry: () -> Unit): LoadStateAdapter<DrinksLoadingStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(itemView: View, private val retry: () -> Unit) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        val retryButton: MaterialButton = itemView.findViewById(R.id.button_retry)
        val errorText: TextView = itemView.findViewById(R.id.text_view_error_msg)

        init {
            retryButton.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            retry.invoke()
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        if (loadState is LoadState.Loading){
            holder.progressBar.isVisible = true
            holder.errorText.isVisible = false
            holder.retryButton.isVisible = false
        }else {
            holder.progressBar.isVisible = false
        }

        if (loadState is LoadState.Error){
            holder.progressBar.isVisible = false
            holder.errorText.isVisible = true
            holder.errorText.text = loadState.error.localizedMessage
            holder.retryButton.isVisible = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =  LoadStateViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.loading_state_item_layout,parent,false),
        retry
    )
}