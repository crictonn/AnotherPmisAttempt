package com.pmorozova.movies

import android.app.Application
import com.pmorozova.movies.data.favorites.favoritesRepositoryModule
import com.pmorozova.movies.data.movie.combinedMovieRepositoryModule
import com.pmorozova.movies.data.search.searchRepositoryModule
import com.pmorozova.movies.feature.favorites.favoriteVMModule
import com.pmorozova.movies.feature.movie.movieVMModule
import com.pmorozova.movies.feature.recommendations.recommendationsVMModule
import com.pmorozova.movies.feature.search.searchVMModule
import com.pmorozova.movies.impl.database.favoritesDatabaseModule
import com.pmorozova.movies.impl.database.movieNotesDatabaseModule
import com.pmorozova.movies.impl.database.searchDatabaseModule
import com.pmorozova.movies.impl.localStorage.favoritesDatasourceModule
import com.pmorozova.movies.impl.localStorage.movieDatasourceModule
import com.pmorozova.movies.impl.localStorage.movieNotesDatasourceModule
import com.pmorozova.movies.impl.localStorage.searchDatasourceModule
import com.pmorozova.movies.impl.network.moviesClientModule
import com.pmorozova.movies.impl.network.networkClientModule
import com.pmorozova.movies.impl.network.searchClientModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

internal class App : Application() {
	override fun onCreate() {
		super.onCreate()

		startKoin {
			androidContext(this@App)
			modules(
				favoritesDatabaseModule,
				searchDatabaseModule,
				movieNotesDatabaseModule,

				networkClientModule,
				searchClientModule,
				moviesClientModule,

				favoritesDatasourceModule,
				favoritesRepositoryModule,
				movieDatasourceModule,
				movieNotesDatasourceModule,

				combinedMovieRepositoryModule,
				searchDatasourceModule,
				searchRepositoryModule,

				recommendationsVMModule,
				movieVMModule,
				favoriteVMModule,
				searchVMModule
			)
		}
	}
}