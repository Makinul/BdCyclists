plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.bd.cyclist"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bd.cyclist"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.animation)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Koin core
    implementation(libs.koin.core) // Check for the latest stable version
    // Koin for Android
    implementation(libs.koin.android)
    // Koin for Jetpack Compose (if you need Compose-specific integration like viewModel, rememberKoinInject)
    implementation(libs.koin.androidx.compose)


    // Ktor Client Core
    implementation(libs.ktor.client.core) // Check for the latest stable version
    // Ktor Client for Android (engine)
    implementation(libs.ktor.client.android)
    // Ktor Client Content Negotiation (for JSON parsing)
    implementation(libs.ktor.client.content.negotiation)
    // Ktor Client JSON Serializer (Kotlinx Serialization)
    implementation(libs.ktor.serialization.kotlinx.json)
    // Kotlinx Serialization runtime
    implementation(libs.kotlinx.serialization.json) // Check for the latest stable version
}