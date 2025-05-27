import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

// Create a variable called keyPropertiesFile, and initialize it to your
// key.properties file, in the rootProject folder.
val keyPropertiesFile = rootProject.file("key.properties")

// Initialize a new Properties() object called keyProperties.
val keyProperties = Properties()

// Load your key.properties file into the keyProperties object.
keyProperties.load(FileInputStream(keyPropertiesFile))

android {
    namespace = "com.bd.cyclists"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bd.cyclists"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "GOOGLE_MAP_API_KEY",
            "\"${keyProperties["GOOGLE_MAP_API_KEY"] as String}\""
        )

        manifestPlaceholders["GOOGLE_MAP_API_KEY"] = keyProperties["GOOGLE_MAP_API_KEY"] as String
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
        buildConfig = true
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
    testImplementation(libs.mockk) // For mocking dependencies
    testImplementation(libs.kotlinx.coroutines.test) // For testing coroutines
    // Add more for Android-specific tests if needed later (e.g., androidx.test)

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

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    // google map
    implementation(libs.play.services.maps)
    // Android Maps Compose composables for the Maps SDK for Android
    implementation(libs.google.maps.compose)
    // Optionally, you can include the Compose utils library for Clustering,
    // Street View metadata checks, etc.
    implementation(libs.google.maps.compose.utils)
    // Optionally, you can include the widgets library for ScaleBar, etc.
    implementation(libs.google.maps.compose.widgets)
    implementation(libs.play.services.location)

    implementation(libs.accompanist.permissions)
}