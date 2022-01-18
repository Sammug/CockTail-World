package com.david.cocktailworld.data.paging

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PageData(
    val current_page: Int,
    val next_page: Int?,
    val loaded_page_items: Int,
    val total_count: Int,
    val total_pages: Int
): Parcelable
