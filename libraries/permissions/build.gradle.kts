import com.raxdenstudios.publishing.model.Coordinates
import com.raxdenstudios.publishing.model.Developer

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.permissions"
}

versioning {
    filePath = "version.properties"
}

publishLibrary {
    name = "Permissions Commons"
    description = "Permissions commons is a library with a set of useful classes to help to " +
            "developer to work with permissions."
    url = "https://github.com/raxden/android-commons"
    developer = Developer(
        id = "raxden",
        name = "Ángel Gómez",
        email = "raxden.dev@gmail.com",
    )
    coordinates = Coordinates.default.copy(artifactId = "commons-permissions")
}


dependencies {
    api(projects.libraries.android)

    implementation(libs.android.material)

    testImplementation(libs.truth)
    testImplementation(libs.mockk.android)
}
