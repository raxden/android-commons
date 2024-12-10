import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.permissions"
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
    api(projects.libraries.android)

    implementation(libs.android.material)
}
