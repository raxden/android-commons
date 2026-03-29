import com.raxdenstudios.publishing.model.Coordinates
import com.raxdenstudios.publishing.model.Developer

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.threetenabp.test"
}

versioning {
    filePath = "version.properties"
}

publishLibrary {
    name = "ThreeTenABP test Commons"
    description = "ThreeTenABP test commons is a library with a set of useful classes to help to " +
            "developer to work with ThreeTenABP."
    url = "https://github.com/raxden/android-commons"
    developer = Developer(
        id = "raxden",
        name = "Ángel Gómez",
        email = "raxden.dev@gmail.com",
    )
    coordinates = Coordinates.default.copy(artifactId = "commons-threetenabp-test")
}

dependencies {
    api(libs.threetenabp)
    api(libs.androidx.arch.core.testing)
}
