import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

versioning {
    filePath = "./libraries/glide/version.properties"
}

publishLibrary {
    name = "Glide"
    description = "Glide library"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-glide")
}

dependencies {
    implementation(libs.glide)
    kapt(libs.glide.compiler)
}
