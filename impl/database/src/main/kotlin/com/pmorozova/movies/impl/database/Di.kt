package com.pmorozova.movies.impl.database

import org.koin.dsl.module

val movieNotesDatabaseModule = module {
	single<MovieNotesDao> { MovieNotesDatabase.getDatabase(get()).dao() }
	single<MovieNotesDatabaseRepository> {
		MovieNotesDatabaseRepositoryImpl(movieNotesDao = get())
	}
}

val favoritesDatabaseModule = module {
	single<FavoritesDao> { FavoritesDatabase.getDatabase(get()).dao() }
	single<FavoritesDatabaseRepository> {
		FavoritesDatabaseRepositoryImpl(favoritesDao = get())
	}
}

val searchDatabaseModule = module {
	single<SearchHistoryDao> { SearchHistoryDatabase.getDatabase(get()).dao() }
	single<SearchHistoryDatabaseRepository> {
		SearchHistoryDatabaseRepositoryImpl(
			searchClient = get(),
			searchHistoryDao = get()
		)
	}
}