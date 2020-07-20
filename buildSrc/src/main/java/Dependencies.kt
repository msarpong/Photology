object Versions {
    const val kotlin = "1.3.72"
    const val detekt = "1.4.0"
    const val koin = "2.1.3"
    const val gradle = "4.0.1"
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

object BuildLibs {
    const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}"
    const val kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val gradle_android = "com.android.tools.build:gradle:${Versions.gradle}"
}