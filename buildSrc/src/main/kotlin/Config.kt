import org.gradle.api.JavaVersion

object Config {
    val minSdk = 23
    val compileSdk = 28
    val targetSdk = 28
    val javaVersion = JavaVersion.VERSION_1_8

    const val versionCode = 1
    const val versionName = "1.0.0"
}