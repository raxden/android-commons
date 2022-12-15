object AndroidLibraries {
    const val material = "com.google.android.material:material:${Versions.material}"
    const val playCore = "com.google.android.play:core:${Versions.playCore}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val swipeRefreshLayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"
    const val browser = "androidx.browser:browser:${Versions.browser}"
    const val kotlinCore = "androidx.core:core-ktx:${Versions.kotlinCore}"
    const val kotlinActivity = "androidx.activity:activity-ktx:${Versions.kotlinActivity}"
    const val kotlinFragment = "androidx.fragment:fragment-ktx:${Versions.kotlinFragment}"
    const val kotlinPreferences = "androidx.preference:preference-ktx:${Versions.kotlinPreferences}"

    // Lifecycle
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
}

object KotlinLibraries {
    const val coroutinesBom = "org.jetbrains.kotlinx:kotlinx-coroutines-bom:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android"
}

object Libraries {
    // RX
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"

    // Retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitRxAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val retrofitNetworkResponseAdapter =
        "com.github.haroldadmin:NetworkResponseAdapter:${Versions.retrofitNetworkResponseAdapter}"

    // OkHttp
    const val okHttpBom = "com.squareup.okhttp3:okhttp-bom:${Versions.okHttp}"
    const val okHttp = "com.squareup.okhttp3:okhttp"
    const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor"

    // Gson
    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    // Glide
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    // Threetenabp
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
