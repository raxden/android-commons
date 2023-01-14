import org.gradle.api.JavaVersion

object Versions {
    const val minSdk = 21
    const val compileSdk = 31
    const val targetSdk = 31

    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    val sourceCompatibility = JavaVersion.VERSION_1_8
    val targetCompatibility = JavaVersion.VERSION_1_8
    const val jvmTarget = "1.8"
}
