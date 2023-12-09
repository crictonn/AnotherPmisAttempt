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
import com.pmorozova.movies.impl.network.SearchClient
import kotlinx.coroutines.flow.Flow

internal class SearchHistoryDatabaseRepositoryImpl(
	private val searchClient: SearchClient,
	private val searchHistoryDao: SearchHistoryDao
) : SearchHistoryDatabaseRepository {
	suspend fun getSearchPage(searchQuery: String, page: Int? = null) =
		searchClient.getSearchPage(searchQuery, page)

	override fun getAllItemsStream(): Flow<List<DatabaseSearchHistoryItem>> = searchHistoryDao.getAllItems()

	override fun getItemStream(id: String): Flow<DatabaseSearchHistoryItem?> = searchHistoryDao.getItem(id)

	override suspend fun insertItem(item: DatabaseSearchHistoryItem) = searchHistoryDao.insert(item)

	override suspend fun deleteItem(item: DatabaseSearchHistoryItem) = searchHistoryDao.delete(item)

	override suspend fun updateItem(item: DatabaseSearchHistoryItem) = searchHistoryDao.update(item)
}

interface SearchHistoryDatabaseRepository {
	fun getAllItemsStream(): Flow<List<DatabaseSearchHistoryItem>>

	fun getItemStream(id: String): Flow<DatabaseSearchHistoryItem?>

	suspend fun insertItem(item: DatabaseSearchHistoryItem)

	suspend fun deleteItem(item: DatabaseSearchHistoryItem)

	suspend fun updateItem(item: DatabaseSearchHistoryItem)
}

@Database(entities = [DatabaseSearchHistoryItem::class], version = 1)
internal abstract class SearchHistoryDatabase : RoomDatabase() {
	abstract fun dao(): SearchHistoryDao

	companion object {
		@Volatile
		private var Instance: SearchHistoryDatabase? = null
		fun getDatabase(context: Context): SearchHistoryDatabase {
			return Instance ?: synchronized(this) {
				Room.databaseBuilder(
					context,
					SearchHistoryDatabase::class.java,
					"search_history"
				).build()
			}.also { Instance = it }
		}
	}
}

@Dao
interface SearchHistoryDao {
	@Insert
	suspend fun insert(item: DatabaseSearchHistoryItem)

	@Update
	suspend fun update(item: DatabaseSearchHistoryItem)

	@Delete
	suspend fun delete(item: DatabaseSearchHistoryItem)

	@Query("SELECT * from search_history WHERE id = :id")
	fun getItem(id: String): Flow<DatabaseSearchHistoryItem?>

	@Query("SELECT * from search_history ORDER BY time_of_creation DESC")
	fun getAllItems(): Flow<List<DatabaseSearchHistoryItem>>
}

@Entity(tableName = "search_history")
data class DatabaseSearchHistoryItem(
	@PrimaryKey val id: String,
	@ColumnInfo(name = "title") val title: String,
	@ColumnInfo("time_of_creation") val timeOfCreation: Long
)

