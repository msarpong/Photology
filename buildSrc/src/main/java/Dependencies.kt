object Versions {
    const val kotlin = "1.3.72"
    const val detekt = "1.4.0"
    const val koin = "2.1.3"
    const val gradle = "4.0.1"
    const val lifecycle = "2.2.0"

}

object Dependencies {
    const val kotlinLibrary = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val androidXCore = "androidx.core:core-ktx:1.3.0"
    const val androidXAppCompat = "androidx.appcompat:appcompat:1.1.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
    const val jUnit = "junit:junit:4.12"
    const val androidJUnit = "androidx.test.ext:junit:1.1.1"
    const val androidEspresso = "androidx.test.espresso:espresso-core:3.2.0"

    const val koin = "org.koin:koin-android:${Versions.koin}"
    const val koin_viewmodel = "org.koin:koin-android-viewmodel:${Versions.koin}"

    const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifeCycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.1.0"

    const val retrofit = "com.squareup.retrofit2:retrofit:2.8.1"
    const val retrofitConverter = "com.squareup.retrofit2:converter-gson:2.8.1"

    const val okttp3 = "com.squareup.okhttp3:okhttp:4.5.0"
    const val okttp3Interceptor = "com.squareup.okhttp3:logging-interceptor:4.5.0"
    const val okttp3Urlconnection = "com.squareup.okhttp3:okhttp-urlconnection:4.2.1"

    const val glide = "com.github.bumptech.glide:glide:4.11.0"
    const val glideAnnotation = "com.github.bumptech.glide:compiler:4.11.0"

    const val imageRound = "de.hdodenhof:circleimageview:3.1.0"
}

object BuildLibs {
    const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}"
    const val kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val gradle_android = "com.android.tools.build:gradle:${Versions.gradle}"
}