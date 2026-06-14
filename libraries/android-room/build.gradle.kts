import com.raxdenstudios.publishing.model.Coordinates
import com.raxdenstudios.publishing.model.Developer
import extension.implementationBundle

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.android.room"
}

versioning {
    filePath = "version.properties"
}

publishLibrary {
    name = "Android Room Commons"
    description = "Android Room commons is a library with a set of useful classes to help " +
            "developers to work with Room persistence library."
    url = "https://github.com/raxden/android-commons"
    developer = Developer(
        id = "raxden",
        name = "Ángel Gómez",
        email = "raxden.dev@gmail.com",
    )
    coordinates = Coordinates.default.copy(artifactId = "commons-android-room")
}

dependencies {
    implementationBundle(libs.bundles.room)
}
