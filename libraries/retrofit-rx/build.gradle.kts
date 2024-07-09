import com.raxdenstudios.publishing.model.Coordinates
import extension.implementationBundle

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.retrofit.rx"
}

versioning {
    filePath = "./libraries/retrofit-rx/version.properties"
}

publishLibrary {
    name = "Retrofit Commons"
    description = "Retrofit commons is a library with a set of useful classes to help to developer " +
            "to work with retrofit."
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-retrofit-rx")
}

dependencies {
    api(projects.libraries.retrofit)

    implementationBundle(libs.bundles.retrofit2.asProvider())
    implementationBundle(libs.bundles.retrofit2.rx)

    testImplementation(libs.bundles.test)
}
