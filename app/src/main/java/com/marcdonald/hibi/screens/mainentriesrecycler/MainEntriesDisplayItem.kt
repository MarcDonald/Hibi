package com.marcdonald.hibi.screens.mainentriesrecycler

import com.marcdonald.hibi.data.entity.Entry

data class MainEntriesDisplayItem(var entry: Entry, var tags: List<String>, var books: List<String>) {
	var isSelected: Boolean = false
}