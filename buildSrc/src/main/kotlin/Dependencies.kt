object AndroidLibraries {
    const val material = "com.google.android.material:material:${Versions.material}"
    const val playCore = "com.google.android.play:core:${Versions.playCore}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"
    const val browser = "androidx.browser:browser:${Versions.browser}"
    const val kotlinCore = "androidx.core:core-ktx:${Versions.kotlinCore}"
    const val kotlinActivity = "androidx.activity:activity-ktx:${Versions.kotlinActivity}"
    const val kotlinFragment = "androidx.fragment:fragment-ktx:${Versions.kotlinFragment}"
    const val kotlinPreferences = "androidx.preference:preference-ktx:${Versions.kotlinPreferences}"
    const val navFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navUI = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
}

object AndroidLifecycleLibraries {
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycleCommon = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
}

object ComposeLibraries {
    // https://developer.android.com/jetpack/compose/setup#bom-version-mapping
    const val bom = "androidx.compose:compose-bom:2022.12.00"

    // Choose one of the following:
    // Material Design 3
    const val material3 = "androidx.compose.material3:material3"

    // or Material Design 2
    const val material = "androidx.compose.material:material"

    // or skip Material Design and build directly on top of foundational components
    const val foundation = "androidx.compose.foundation:foundation"

    // or only import the main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    const val ui = "androidx.compose.ui:ui"

    // Android Studio Preview support
    const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
    const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"

    // UI Tests
    const val uiTestjunit4 = "androidx.compose.ui:ui-test-junit4"
    const val uiTestManifest = "androidx.compose.ui:ui-test-manifest"

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
    const val materialIconsCore = "androidx.compose.material:material-icons-core"

    // Optional - Add full set of material icons
    const val materialIconsExtended = "androidx.compose.material:material-icons-extended"

    // Optional - Add window size utils
    const val material3WindowSizeClass = "androidx.compose.material3:material3-window-size-class"

    // Optional - Integration with activities
    const val activityCompose = "androidx.activity:activity-compose:1.5.1"

    // Optional - Integration with ViewModels
    const val viewmodelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1"

    // Optional - Integration with LiveData
    const val runtimeLiveData = "androidx.compose.runtime:runtime-livedata"

    // Optional - Integration with RxJava
    const val rxJava = "androidx.compose.runtime:runtime-rxjava2"
}

object KotlinLibraries {
    const val coroutinesBom = "org.jetbrains.kotlinx:kotlinx-coroutines-bom:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android"
}

object RXLibraries {
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
}

object RetrofitLibraries {
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitRxAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val retrofitNetworkResponseAdapter =
        "com.github.haroldadmin:NetworkResponseAdapter:${Versions.retrofitNetworkResponseAdapter}"
}

object OKHttpLibraries {
    const val okHttpBom = "com.squareup.okhttp3:okhttp-bom:${Versions.okHttp}"
    const val okHttp = "com.squareup.okhttp3:okhttp"
    const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor"
}

object GlideLibraries {
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
}

object Libraries {
    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val threetenabp = "com.jakewharton.threetenabp:threetenabp:${Versions.threetenabp}"
}

object TestLibraries {
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test"

    // ANDROID TEST
    const val archCoreTest = "androidx.arch.core:core-testing:${Versions.archCoreTest}"
    const val atslJunit = "androidx.test.ext:junit-ktx:${Versions.atslJunit}"
    const val atslRunner = "androidx.test:runner:${Versions.atsl}"
    const val atslRules = "androidx.test:rules:${Versions.atsl}"

    // Robolectric
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"

    // MOCKK
    const val mockkCore = "io.mockk:mockk:${Versions.mockk}"
    const val mockkAndroid = "io.mockk:mockk-android:${Versions.mockk}"

    // MOCK WEBSERVER
    const val restMock = "com.github.andrzejchm.RESTMock:android:${Versions.restMock}"

    // Threetenabp
    const val threetenabp = "org.threeten:threetenbp:1.3.3"
}

object TestAndroidLibraries {

    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
    const val espressoIntents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
}
