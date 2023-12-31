package com.pmorozova.movies.feature.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmorozova.movies.domain.models.SearchHistoryItem
import com.pmorozova.movies.domain.models.TitleInfo
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.UUID

internal class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {
	var searchQuery by mutableStateOf("")
	var loadedPage = 0
	val searchResults = mutableStateListOf<TitleInfo>()

	val successfulSearchQueriesHistory = searchRepository.getAllItemsStream()

	fun updateSearchQuery(newText: String) {
		searchQuery = newText
		viewModelScope.launch {
			searchRepository.getSearchPage(searchQuery)?.let {
				if (it.results.isNotEmpty()) {
					searchResults.clear()
					searchResults.addAll(it.results)

					loadedPage = 0
					searchRepository.insertItem(
						SearchHistoryItem(
							id = UUID.randomUUID().toString(),
							title = newText,
							timeOfCreation = System.currentTimeMillis()
						)
					)
				}
			}
		}
	}

	fun clearSearchQueryAndResults() {
		searchQuery = ""
		searchResults.clear()
	}

	suspend fun loadMoreMovies() {
		searchRepository.getSearchPage(searchQuery, page = loadedPage + 1)?.let {
			searchResults.addAll(it.results)
			loadedPage++
		}
	}

	fun removeASearchHistoryItem(item: SearchHistoryItem) {
		viewModelScope.launch {
			searchRepository.deleteItem(item)
		}
	}
}

val searchVMModule = module {
	viewModel { SearchViewModel(get()) }
}