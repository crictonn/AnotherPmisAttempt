package com.pmorozova.movies.domain.models

data class SearchHistoryItem(
	val id: String,
	val title: String,
	val timeOfCreation: Long
)