import com.raxdenstudios.publishing.model.Coordinates
import com.raxdenstudios.publishing.model.Developer

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.time"
}

versioning {
    filePath = "version.properties"
}

publishLibrary {
    name = "Time Commons"
    description = "Time commons is a library with a set of useful extensions to help the developer " +
            "to work with java.time."
    url = "https://github.com/raxden/android-commons"
    developer = Developer(
        id = "raxden",
        name = "Ángel Gómez",
        email = "raxden.dev@gmail.com",
    )
    coordinates = Coordinates.default.copy(artifactId = "commons-time")
}

dependencies {
    testImplementation(libs.truth)
}
