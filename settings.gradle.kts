rootProject.name = "LastIncident"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        //TODO: This was commented out due to build issues previously, but would be good to put back in here and in other block below to potentially save on sync times.
        // look into adding back and making sure correct repos are hit with google repo
//        google {
//            mavenContent {
//                includeGroupAndSubgroups("androidx")
//                includeGroupAndSubgroups("com.android")
//                includeGroupAndSubgroups("com.google")
//            }
//        }
        //TODO: version from step by step guide from jetbrains at this link: https://www.youtube.com/watch?v=fmFezt-2IBo
//        google {
//            mavenContent {
//                includeGroupByRegex(".*android.*")
//                includeGroupByRegex(".*google.*")
//            }
//        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
//        google {
//            mavenContent {
//                includeGroupAndSubgroups("androidx")
//                includeGroupAndSubgroups("com.android")
//                includeGroupAndSubgroups("com.google")
//            }
//        }
        google()
        mavenCentral()
    }
}

include(":composeApp")