import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.android.publish.library)
}

versioning {
    filePath = "./libraries/permissions/version.properties"
}

publishLibrary {
    name = "Permissions Commons"
    description = "Permissions commons is a library with a set of useful classes to help to " +
            "developer to work with permissions."
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-permissions")
}


dependencies {
    implementation(project(":libraries:android"))

    implementation(libs.bundles.material)
}
