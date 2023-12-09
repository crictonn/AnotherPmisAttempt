plugins {
	id("com.pmorozova.movies.android-library")
}

val libs by versionCatalog

dependencies {
	implementation(libs.requireLib("kotlin-coroutines-core"))
	implementation(project(":domain:models"))
	implementation(project(":impl:database"))
	implementation(project(":data:favorites"))
	implementation(project(":data:movie"))
	implementation(project(":impl:network"))
	implementation(project(":data:search"))
	implementation(project(":data:recommendations"))
}