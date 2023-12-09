package com.pmorozova.movies.data.search

import com.pmorozova.movies.domain.models.SearchHistoryItem
import com.pmorozova.movies.domain.models.TitlesPage
import com.pmorozova.movies.feature.search.SearchRepository
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.module

internal class SearchRepositoryImpl(private val searchDataSource: SearchDataSource) :
	SearchRepository {
	override suspend fun getSearchPage(searchQuery: String, page: Int?): TitlesPage? =
		searchDataSource.getSearchPage(searchQuery, page)

	override fun getAllItemsStream(): Flow<List<SearchHistoryItem>> =
		searchDataSource.getAllItemsStream()

	override fun getItemStream(id: String): Flow<SearchHistoryItem?> =
		searchDataSource.getItemStream(id)

	override suspend fun insertItem(item: SearchHistoryItem) =
		searchDataSource.insertItem(item)

	override suspend fun deleteItem(item: SearchHistoryItem) =
		searchDataSource.deleteItem(item)

	override suspend fun updateItem(item: SearchHistoryItem) =
		searchDataSource.updateItem(item)
}

val searchRepositoryModule = module {
	single<SearchRepository> { SearchRepositoryImpl(get())}
}