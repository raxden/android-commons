import com.raxdenstudios.publishing.model.Coordinates
import com.raxdenstudios.publishing.model.Developer

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.pagination"
}

versioning {
    filePath = "version.properties"
}

publishLibrary {
    name = "Pagination"
    description = "Pagination library"
    url = "https://github.com/raxden/android-commons"
    developer = Developer(
        id = "raxden",
        name = "Ángel Gómez",
        email = "raxden.dev@gmail.com",
    )
    coordinates = Coordinates.default.copy(artifactId = "commons-pagination")
}

dependencies {
    implementation(libs.android.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material)
}
