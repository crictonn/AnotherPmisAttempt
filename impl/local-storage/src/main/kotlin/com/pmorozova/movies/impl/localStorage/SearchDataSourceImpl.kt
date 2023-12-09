package com.pmorozova.movies.impl.localStorage

import com.pmorozova.movies.domain.models.SearchHistoryItem
import com.pmorozova.movies.data.search.SearchDataSource
import com.pmorozova.movies.domain.models.Genres
import com.pmorozova.movies.domain.models.ImageCaption
import com.pmorozova.movies.domain.models.ImageData
import com.pmorozova.movies.domain.models.ReleaseDate
import com.pmorozova.movies.domain.models.ReleaseYear
import com.pmorozova.movies.domain.models.TitleInfo
import com.pmorozova.movies.domain.models.TitleText
import com.pmorozova.movies.domain.models.TitleType
import com.pmorozova.movies.domain.models.TitlesPage
import com.pmorozova.movies.impl.database.DatabaseSearchHistoryItem
import com.pmorozova.movies.impl.database.SearchHistoryDao
import com.pmorozova.movies.impl.network.ResponseGenres
import com.pmorozova.movies.impl.network.ResponseImageCaption
import com.pmorozova.movies.impl.network.ResponseImageData
import com.pmorozova.movies.impl.network.ResponseReleaseDate
import com.pmorozova.movies.impl.network.ResponseReleaseYear
import com.pmorozova.movies.impl.network.ResponseTitleInfo
import com.pmorozova.movies.impl.network.ResponseTitleText
import com.pmorozova.movies.impl.network.ResponseTitleType
import com.pmorozova.movies.impl.network.ResponseTitlesPage
import com.pmorozova.movies.impl.network.SearchClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SearchDataSourceImpl(
	private val searchHistoryDao: SearchHistoryDao,
	private val searchClient: SearchClient
) : SearchDataSource {
	// Search history methods
	override fun getAllItemsStream(): Flow<List<SearchHistoryItem>> =
		searchHistoryDao.getAllItems().map { it.map { dbSearchHistoryItem -> dbSearchHistoryItem.toCommonSearchHistoryItem() } }

	override fun getItemStream(id: String): Flow<SearchHistoryItem?> =
		searchHistoryDao.getItem(id).map { it?.toCommonSearchHistoryItem() }

	override suspend fun insertItem(item: SearchHistoryItem) =
		searchHistoryDao.insert(item.toDbSearchHistoryItem())

	override suspend fun deleteItem(item: SearchHistoryItem) =
		searchHistoryDao.delete(item.toDbSearchHistoryItem())

	override suspend fun updateItem(item: SearchHistoryItem) =
		searchHistoryDao.update(item.toDbSearchHistoryItem())

	private fun SearchHistoryItem.toDbSearchHistoryItem(): DatabaseSearchHistoryItem =
		DatabaseSearchHistoryItem(id, title, timeOfCreation)

	private fun DatabaseSearchHistoryItem.toCommonSearchHistoryItem(): SearchHistoryItem =
		SearchHistoryItem(id, title, timeOfCreation)

	private fun ResponseTitleInfo.toCommonTitleInfo(): TitleInfo =
		TitleInfo(
			id,
			primaryImage?.toCommonImageData(),
			titleType.toCommonTitleType(),
			titleText.toCommonTitleText(),
			originalTitleText.toCommonTitleText(),
			releaseYear?.toCommonReleaseYear(),
			releaseDate?.toCommonReleaseDate()
		)

	private fun ResponseImageData.toCommonImageData(): ImageData =
		ImageData(id, width, height, url, caption.toCommonImageCaption())

	private fun ResponseImageCaption.toCommonImageCaption(): ImageCaption =
		ImageCaption(plainText)

	private fun ResponseTitleType.toCommonTitleType(): TitleType =
		TitleType(text, id, isSeries, isEpisode)

	private fun ResponseTitleText.toCommonTitleText(): TitleText = TitleText(text)

	private fun ResponseReleaseYear.toCommonReleaseYear(): ReleaseYear =
		ReleaseYear(year, endYear)

	private fun ResponseReleaseDate.toCommonReleaseDate(): ReleaseDate =
		ReleaseDate(day, month, year)

	private fun ResponseGenres.toCommonGenres(): Genres = Genres(results)

	private fun ResponseTitlesPage.toCommonTitlesPage(): TitlesPage =
		TitlesPage(page, next, entries, results.map { it.toCommonTitleInfo() })

	override suspend fun getSearchPage(
		searchQuery: String, page: Int?
	): TitlesPage? = searchClient.getSearchPage(searchQuery, page)?.toCommonTitlesPage()
}