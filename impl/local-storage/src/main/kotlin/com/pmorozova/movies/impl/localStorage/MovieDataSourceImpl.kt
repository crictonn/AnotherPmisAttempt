package com.pmorozova.movies.impl.localStorage

import com.pmorozova.movies.data.movie.MovieDataSource
import com.pmorozova.movies.data.recommendations.RecommendationsDataSource
import com.pmorozova.movies.domain.models.Genres
import com.pmorozova.movies.domain.models.ImageCaption
import com.pmorozova.movies.domain.models.ImageData
import com.pmorozova.movies.domain.models.MovieTypeList
import com.pmorozova.movies.domain.models.ReleaseDate
import com.pmorozova.movies.domain.models.ReleaseYear
import com.pmorozova.movies.domain.models.TitleInfo
import com.pmorozova.movies.domain.models.TitleText
import com.pmorozova.movies.domain.models.TitleType
import com.pmorozova.movies.domain.models.TitlesPage
import com.pmorozova.movies.impl.network.MoviesClient
import com.pmorozova.movies.impl.network.ResponseGenres
import com.pmorozova.movies.impl.network.ResponseImageCaption
import com.pmorozova.movies.impl.network.ResponseImageData
import com.pmorozova.movies.impl.network.ResponseMovieTypeList
import com.pmorozova.movies.impl.network.ResponseReleaseDate
import com.pmorozova.movies.impl.network.ResponseReleaseYear
import com.pmorozova.movies.impl.network.ResponseTitleInfo
import com.pmorozova.movies.impl.network.ResponseTitleText
import com.pmorozova.movies.impl.network.ResponseTitleType
import com.pmorozova.movies.impl.network.ResponseTitlesPage

internal class MovieDataSourceImpl(private val moviesClient: MoviesClient) : MovieDataSource {
	override suspend fun getTitlesPage(page: Int?, genre: String?, movieList: String?): Result<TitlesPage> =
		moviesClient.getTitlesPage(page, genre, movieList).map { it.toCommonTitlesPage() }

	override suspend fun getRandomTitle(): Result<TitleInfo> =
		moviesClient.getRandomTitle().map { it.toCommonTitleInfo() }

	override suspend fun getGenres(): Result<Genres> =
		moviesClient.getGenres().map { it.toCommonGenres() }

	override suspend fun getMovieLists(): Result<MovieTypeList> =
		moviesClient.getMovieLists().map { it.toCommonMovieTypeList() }

	override suspend fun getMovieDataById(id: String): Result<TitleInfo> =
		moviesClient.getMovieDataById(id).map { it.toCommonTitleInfo() }

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

	private fun ResponseTitleText.toCommonTitleText(): TitleText =
		TitleText(text)

	private fun ResponseReleaseYear.toCommonReleaseYear(): ReleaseYear =
		ReleaseYear(year, endYear)

	private fun ResponseReleaseDate.toCommonReleaseDate(): ReleaseDate =
		ReleaseDate(day, month, year)

	private fun ResponseGenres.toCommonGenres(): Genres =
		Genres(results)

	private fun ResponseMovieTypeList.toCommonMovieTypeList(): MovieTypeList =
		MovieTypeList(entries, results)

	private fun ResponseTitlesPage.toCommonTitlesPage(): TitlesPage =
		TitlesPage(page, next, entries, results.map { it.toCommonTitleInfo() })
}

internal class RecommendationsDataSourceImpl(private val moviesClient: MoviesClient) :
	RecommendationsDataSource {
	override suspend fun getTitlesPage(page: Int?, genre: String?, movieList: String?): Result<TitlesPage> =
		moviesClient.getTitlesPage(page, genre, movieList).map { it.toCommonTitlesPage() }

	override suspend fun getRandomTitle(): Result<TitleInfo> =
		moviesClient.getRandomTitle().map { it.toCommonTitleInfo() }

	override suspend fun getGenres(): Result<Genres> =
		moviesClient.getGenres().map { it.toCommonGenres() }

	override suspend fun getMovieLists(): Result<MovieTypeList> =
		moviesClient.getMovieLists().map { it.toCommonMovieTypeList() }

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

	private fun ResponseTitleText.toCommonTitleText(): TitleText =
		TitleText(text)

	private fun ResponseReleaseYear.toCommonReleaseYear(): ReleaseYear =
		ReleaseYear(year, endYear)

	private fun ResponseReleaseDate.toCommonReleaseDate(): ReleaseDate =
		ReleaseDate(day, month, year)

	private fun ResponseGenres.toCommonGenres(): Genres =
		Genres(results)

	private fun ResponseMovieTypeList.toCommonMovieTypeList(): MovieTypeList =
		MovieTypeList(entries, results)

	private fun ResponseTitlesPage.toCommonTitlesPage(): TitlesPage =
		TitlesPage(page, next, entries, results.map { it.toCommonTitleInfo() })
}