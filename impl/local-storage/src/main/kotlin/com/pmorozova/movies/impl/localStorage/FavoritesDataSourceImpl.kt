package com.pmorozova.movies.impl.localStorage

import com.pmorozova.movies.data.favorites.FavoritesDataSource
import com.pmorozova.movies.data.movie.FavoriteMovieDataSource
import com.pmorozova.movies.domain.models.Favorite
import com.pmorozova.movies.impl.database.DatabaseFavorite
import com.pmorozova.movies.impl.database.FavoritesDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class FavoritesDataSourceImpl(private val favoritesDao: FavoritesDao) :
	FavoritesDataSource, FavoriteMovieDataSource {
	override fun getAllItemsStream(): Flow<List<Favorite>> =
		favoritesDao.getAllItems().map { it.map { dbFavorite -> dbFavorite.toCommonFavorite() } }

	override fun getItemStream(uuid: String): Flow<Favorite?> = favoritesDao.getItem(uuid).map { it?.toCommonFavorite() }

	override suspend fun insertItem(item: Favorite) = favoritesDao.insert(item.toDbFavorite())

	override suspend fun deleteItem(item: Favorite) = favoritesDao.delete(item.toDbFavorite())

	override suspend fun updateItem(item: Favorite) = favoritesDao.update(item.toDbFavorite())

	private fun DatabaseFavorite.toCommonFavorite(): Favorite = Favorite(
		movieId, title, imageUrl
	)

	private fun Favorite.toDbFavorite(): DatabaseFavorite =
		DatabaseFavorite(movieId, title, imageUrl)
}