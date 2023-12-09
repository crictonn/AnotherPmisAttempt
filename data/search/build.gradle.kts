plugins {
	id("com.pmorozova.movies.android-library")
}

val libs by versionCatalog

dependencies {
	implementation(project(":feature:movie"))
	implementation(project(":feature:search"))
	implementation(project(":domain:models"))
}