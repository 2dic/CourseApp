plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "com.example.domain"
    compileSdk = 33

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation("javax.inject:javax.inject:1")
    implementation("com.google.android.material:material:1.10.0")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}