package com.pmorozova.movies.impl.database

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface MovieNotesDatabaseRepository {
	fun getAllItemsStream(): Flow<List<DatabaseMovieNote>>

	fun getAllItemsStreamByMovieId(movieId: String): Flow<List<DatabaseMovieNote>>

	fun getItemStream(id: String): Flow<DatabaseMovieNote?>

	suspend fun insertItem(item: DatabaseMovieNote)

	suspend fun deleteItem(item: DatabaseMovieNote)

	suspend fun updateItem(item: DatabaseMovieNote)
}

internal class MovieNotesDatabaseRepositoryImpl(private val movieNotesDao: MovieNotesDao) :
	MovieNotesDatabaseRepository {
	override fun getAllItemsStream(): Flow<List<DatabaseMovieNote>> = movieNotesDao.getAllItems()

	override fun getAllItemsStreamByMovieId(
		movieId: String
	): Flow<List<DatabaseMovieNote>> = movieNotesDao.getAllItemsByMovieId(movieId)

	override fun getItemStream(id: String): Flow<DatabaseMovieNote?> = movieNotesDao.getItem(id)

	override suspend fun insertItem(item: DatabaseMovieNote) = movieNotesDao.insert(item)

	override suspend fun deleteItem(item: DatabaseMovieNote) = movieNotesDao.delete(item)

	override suspend fun updateItem(item: DatabaseMovieNote) = movieNotesDao.update(item)
}

@Database(entities = [DatabaseMovieNote::class], version = 1)
internal abstract class MovieNotesDatabase : RoomDatabase() {
	abstract fun dao(): MovieNotesDao

	companion object {
		@Volatile
		private var Instance: MovieNotesDatabase? = null
		fun getDatabase(context: Context): MovieNotesDatabase {
			return Instance ?: synchronized(this) {
				Room.databaseBuilder(
					context,
					MovieNotesDatabase::class.java,
					"movie_notes"
				).build()
			}.also { Instance = it }
		}
	}
}

@Dao
interface MovieNotesDao {
	@Insert
	suspend fun insert(item: DatabaseMovieNote)

	@Update
	suspend fun update(item: DatabaseMovieNote)

	@Delete
	suspend fun delete(item: DatabaseMovieNote)

	@Query("SELECT * from movie_notes WHERE movie_id = :id")
	fun getItem(id: String): Flow<DatabaseMovieNote>

	@Query("SELECT * from movie_notes ORDER BY time_of_creation DESC")
	fun getAllItems(): Flow<List<DatabaseMovieNote>>

	@Query("SELECT * FROM movie_notes WHERE movie_id = :movieId ORDER BY time_of_creation DESC")
	fun getAllItemsByMovieId(movieId: String): Flow<List<DatabaseMovieNote>>
}

@Entity(tableName = "movie_notes")
data class DatabaseMovieNote(
	@PrimaryKey val uuid: String,
	@ColumnInfo(name = "movie_id") val movieId: String,
	@ColumnInfo(name = "text") val text: String,
	@ColumnInfo(name = "time_of_creation") val timeOfCreation: Long
)