package com.pmorozova.movies.impl.localStorage

import com.pmorozova.movies.data.movie.FavoriteMovieDataSource
import com.pmorozova.movies.data.favorites.FavoritesDataSource
import com.pmorozova.movies.data.movie.MovieDataSource
import com.pmorozova.movies.data.movie.MovieNotesDataSource
import com.pmorozova.movies.data.recommendations.RecommendationsDataSource
import com.pmorozova.movies.data.search.SearchDataSource
import org.koin.dsl.module

val searchDatasourceModule = module {
	single<SearchDataSource> { SearchDataSourceImpl(get(), get()) }
}

val movieDatasourceModule = module {
	single<MovieDataSource> { MovieDataSourceImpl(get()) }

	single<RecommendationsDataSource> { RecommendationsDataSourceImpl(get()) }
}

val movieNotesDatasourceModule = module {
	single<MovieNotesDataSource> { MovieNotesDataSourceImpl(get()) }
}

val favoritesDatasourceModule = module {
	single<FavoritesDataSource> { FavoritesDataSourceImpl(get()) }

	single<FavoriteMovieDataSource> { FavoritesDataSourceImpl(get()) }
}