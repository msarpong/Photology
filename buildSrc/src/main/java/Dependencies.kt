object Versions {
    const val kotlin = "1.3.72"
    const val koin = "2.1.3"
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
}