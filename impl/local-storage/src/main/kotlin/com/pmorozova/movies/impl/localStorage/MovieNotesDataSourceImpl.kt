package com.pmorozova.movies.impl.localStorage

import com.pmorozova.movies.data.movie.MovieNotesDataSource
import com.pmorozova.movies.domain.models.MovieNote
import com.pmorozova.movies.impl.database.DatabaseMovieNote
import com.pmorozova.movies.impl.database.MovieNotesDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MovieNotesDataSourceImpl(private val movieNotesDao: MovieNotesDao) :
	MovieNotesDataSource {
	override fun getAllItemsStream(): Flow<List<MovieNote>> =
		movieNotesDao.getAllItems().map { it.map { dbMovieNote -> dbMovieNote.toCommonMovieNote() } }

	override fun getAllItemsStreamByMovieId(movieId: String): Flow<List<MovieNote>> =
		movieNotesDao.getAllItemsByMovieId(movieId).map { it.map { dbMovieNote -> dbMovieNote.toCommonMovieNote() } }

	override fun getItemStream(id: String): Flow<MovieNote?> =
		movieNotesDao.getItem(id).map { it.toCommonMovieNote() }

	override suspend fun insertItem(item: MovieNote) =
		movieNotesDao.insert(item.toDbMovieNote())

	override suspend fun deleteItem(item: MovieNote) =
		movieNotesDao.delete(item.toDbMovieNote())

	override suspend fun updateItem(item: MovieNote) =
		movieNotesDao.update(item.toDbMovieNote())

	private fun MovieNote.toDbMovieNote(): DatabaseMovieNote =
		DatabaseMovieNote(uuid, movieId, text, timeOfCreation)

	private fun DatabaseMovieNote.toCommonMovieNote(): MovieNote =
		MovieNote(uuid, movieId, text, timeOfCreation)
}