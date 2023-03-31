dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
rootProject.name = "ImageCollector"
include (":app")
include (":core")
include (":data")
include (":domain")
include (":feature:search")
include (":feature:bookmarks")
