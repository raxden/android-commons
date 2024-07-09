import com.raxdenstudios.publishing.model.Coordinates
import extension.implementationBundle

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.rx.test"
}

versioning {
    filePath = "./libraries/rx-test/version.properties"
}

publishLibrary {
    name = "Rx Test Commons"
    description = "Rx Test commons is a library with a set of useful classes to help to developer to work with Rx."
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-rx-test")
}

dependencies {
    implementationBundle(libs.bundles.rx)
    implementationBundle(libs.bundles.test.asProvider())
}
