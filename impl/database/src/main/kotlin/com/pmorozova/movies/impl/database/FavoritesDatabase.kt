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

@Database(entities = [DatabaseFavorite::class], version = 1)
internal abstract class FavoritesDatabase : RoomDatabase() {
	abstract fun dao(): FavoritesDao

	companion object {
		@Volatile
		private var Instance: FavoritesDatabase? = null
		fun getDatabase(context: Context): FavoritesDatabase {
			return Instance ?: synchronized(this) {
				Room.databaseBuilder(
					context,
					FavoritesDatabase::class.java,
					"favorites"
				).build()
			}.also { Instance = it }
		}
	}
}

@Dao
interface FavoritesDao {
	@Insert
	suspend fun insert(item: DatabaseFavorite)

	@Update
	suspend fun update(item: DatabaseFavorite)

	@Delete
	suspend fun delete(item: DatabaseFavorite)

	@Query("SELECT * from favorites WHERE movieId = :uuid")
	fun getItem(uuid: String): Flow<DatabaseFavorite?>

	@Query("SELECT * from favorites ORDER BY movieId ASC")
	fun getAllItems(): Flow<List<DatabaseFavorite>>
}

interface FavoritesDatabaseRepository {
	fun getAllItemsStream(): Flow<List<DatabaseFavorite>>

	fun getItemStream(uuid: String): Flow<DatabaseFavorite?>

	suspend fun insertItem(item: DatabaseFavorite)

	suspend fun deleteItem(item: DatabaseFavorite)

	suspend fun updateItem(item: DatabaseFavorite)
}

internal class FavoritesDatabaseRepositoryImpl(private val favoritesDao: FavoritesDao) :
	FavoritesDatabaseRepository {
	override fun getAllItemsStream(): Flow<List<DatabaseFavorite>> = favoritesDao.getAllItems()

	override fun getItemStream(uuid: String): Flow<DatabaseFavorite?> = favoritesDao.getItem(uuid)

	override suspend fun insertItem(item: DatabaseFavorite) = favoritesDao.insert(item)

	override suspend fun deleteItem(item: DatabaseFavorite) = favoritesDao.delete(item)

	override suspend fun updateItem(item: DatabaseFavorite) = favoritesDao.update(item)
}

@Entity(tableName = "favorites")
data class DatabaseFavorite(
	@PrimaryKey val movieId: String,
	@ColumnInfo(name = "title") val title: String,
	@ColumnInfo(name = "imageUrl") val imageUrl: String
)