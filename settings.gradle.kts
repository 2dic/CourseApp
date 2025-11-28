pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CoursesApp"
include(":app")

include(":core")
include(":core:network")
include(":core:database")
include(":core:common")
include(":core:designsystem")

include(":data")
include(":data:repositories")
include(":data:datasources")
include(":data:models")

include(":domain")
include(":domain:usecases")
include(":domain:repositories")
include(":domain:models")

include(":features")
include(":features:auth")
include(":features:home")
include(":features:favorites")
include(":features:account")
