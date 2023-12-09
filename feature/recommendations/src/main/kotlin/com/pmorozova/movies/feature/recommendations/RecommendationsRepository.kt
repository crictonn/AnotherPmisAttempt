package com.pmorozova.movies.feature.recommendations

import com.pmorozova.movies.domain.models.Genres
import com.pmorozova.movies.domain.models.MovieTypeList
import com.pmorozova.movies.domain.models.TitleInfo
import com.pmorozova.movies.domain.models.TitlesPage

interface RecommendationsRepository {
	suspend fun getTitlesPage(page: Int? = null, genre: String? = null, movieList: String? = null): Result<TitlesPage>
	suspend fun getRandomTitle(): Result<TitleInfo>
	suspend fun getGenres(): Result<Genres>
	suspend fun getMovieLists(): Result<MovieTypeList>
}