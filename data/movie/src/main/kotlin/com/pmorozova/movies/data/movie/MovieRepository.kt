package com.pmorozova.movies.data.movie

import com.pmorozova.movies.domain.models.Favorite
import com.pmorozova.movies.domain.models.Genres
import com.pmorozova.movies.domain.models.MovieNote
import com.pmorozova.movies.domain.models.MovieTypeList
import com.pmorozova.movies.domain.models.TitleInfo
import com.pmorozova.movies.domain.models.TitlesPage
import com.pmorozova.movies.feature.movie.FavoriteMovieRepository
import com.pmorozova.movies.feature.movie.MovieNotesRepository
import com.pmorozova.movies.feature.movie.MovieRepository
import com.pmorozova.movies.feature.recommendations.RecommendationsRepository
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.module

internal class MovieRepositoryImpl(private val movieDataSource: MovieDataSource) : MovieRepository {
	override suspend fun getTitlesPage(page: Int?, genre: String?, movieList: String?): Result<TitlesPage> =
		movieDataSource.getTitlesPage(page, genre, movieList)

	override suspend fun getRandomTitle(): Result<TitleInfo> =
		movieDataSource.getRandomTitle()

	override suspend fun getGenres(): Result<Genres> =
		movieDataSource.getGenres()

	override suspend fun getMovieLists(): Result<MovieTypeList> =
		movieDataSource.getMovieLists()

	override suspend fun getMovieDataById(id: String): Result<TitleInfo> =
		movieDataSource.getMovieDataById(id)
}

internal class RecommendationsRepositoryImpl(private val movieDataSource: MovieDataSource) :
	RecommendationsRepository {
	override suspend fun getTitlesPage(page: Int?, genre: String?, movieList: String?): Result<TitlesPage> =
		movieDataSource.getTitlesPage(page, genre, movieList)

	override suspend fun getRandomTitle(): Result<TitleInfo> =
		movieDataSource.getRandomTitle()

	override suspend fun getGenres(): Result<Genres> =
		movieDataSource.getGenres()

	override suspend fun getMovieLists(): Result<MovieTypeList> =
		movieDataSource.getMovieLists()
}

internal class MovieNotesRepositoryImpl(private val movieNotesDataSource: MovieNotesDataSource) :
	MovieNotesRepository {
	override fun getAllItemsStream(): Flow<List<MovieNote>> =
		movieNotesDataSource.getAllItemsStream()

	override fun getAllItemsStreamByMovieId(movieId: String): Flow<List<MovieNote>> =
		movieNotesDataSource.getAllItemsStreamByMovieId(movieId)

	override fun getItemStream(id: String): Flow<MovieNote?> =
		movieNotesDataSource.getItemStream(id)

	override suspend fun insertItem(item: MovieNote) =
		movieNotesDataSource.insertItem(item)

	override suspend fun deleteItem(item: MovieNote) =
		movieNotesDataSource.deleteItem(item)

	override suspend fun updateItem(item: MovieNote) =
		movieNotesDataSource.updateItem(item)
}

internal class FavoriteMovieRepositoryImpl(private val favoritesDataSource: FavoriteMovieDataSource) :
	FavoriteMovieRepository {
	override fun getAllItemsStream(): Flow<List<Favorite>> =
		favoritesDataSource.getAllItemsStream()

	override fun getItemStream(id: String): Flow<Favorite?> =
		favoritesDataSource.getItemStream(id)

	override suspend fun insertItem(item: Favorite) =
		favoritesDataSource.insertItem(item)

	override suspend fun deleteItem(item: Favorite) =
		favoritesDataSource.deleteItem(item)

	override suspend fun updateItem(item: Favorite) =
		favoritesDataSource.updateItem(item)
}

val combinedMovieRepositoryModule = module {
	single<MovieRepository> { MovieRepositoryImpl(get()) }
	single<RecommendationsRepository> { RecommendationsRepositoryImpl(get()) }
	single<FavoriteMovieRepository> { FavoriteMovieRepositoryImpl(get()) }
	single<MovieNotesRepository> { MovieNotesRepositoryImpl(get()) }
}

