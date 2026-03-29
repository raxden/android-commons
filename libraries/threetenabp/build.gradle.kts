import com.raxdenstudios.publishing.model.Coordinates
import com.raxdenstudios.publishing.model.Developer

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.threetenabp"
}

versioning {
    filePath = "version.properties"
}

publishLibrary {
    name = "ThreeTenABP Commons"
    description = "ThreeTenABP commons is a library with a set of useful classes to help to developer " +
            "to work with ThreeTenABP."
    url = "https://github.com/raxden/android-commons"
    developer = Developer(
        id = "raxden",
        name = "Ángel Gómez",
        email = "raxden.dev@gmail.com",
    )
    coordinates = Coordinates.default.copy(artifactId = "commons-threetenabp")
}

dependencies {
    api(libs.threetenabp)

    testImplementation(projects.libraries.threetenabpTest)
    testImplementation(libs.truth)
}
