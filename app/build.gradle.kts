// Top-level build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.kapt")
}



android {
    namespace = "com.roni.class12thagjetnotes"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.roni.class12thagjetnotes"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Core Android libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Additional UI components from original
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.recyclerview.v132)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation(libs.google.firebase.auth.ktx)
    implementation(libs.google.firebase.firestore.ktx)
    implementation(libs.google.firebase.storage.ktx)
    implementation(libs.play.services.auth.v2070)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android.v173)
    implementation(libs.kotlinx.coroutines.play.services.v173)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v262)
    implementation(libs.androidx.lifecycle.livedata.ktx.v262)
    implementation(libs.androidx.lifecycle.runtime.ktx.v262)

    // Image Loading
    implementation(libs.glide.v4160)
    implementation(libs.androidx.media3.common.ktx)
    kapt(libs.compiler.v4160)

    // Shimmer Effect
    implementation(libs.shimmer)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.material.v1110)
    implementation(libs.androidx.swiperefreshlayout)

    implementation(libs.konfetti.xml)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.exoplayer)
    implementation(libs.exoplayer.ui)

    // PDF Viewer - AndroidPdfViewer
    implementation("com.github.mhiew:android-pdf-viewer:3.2.0-beta.3")// ExoPlayer for Video Playback
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.common)
// Lottie for Animations (optional but recommended)
    implementation(libs.lottie)

}