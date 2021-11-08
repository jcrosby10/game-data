plugins {
    id("com.android.library")
    id("com.google.gms.google-services")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31
    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdk = 21
        targetSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")

    implementation(platform("com.google.firebase:firebase-bom:28.4.2"))
    implementation("com.google.firebase:firebase-firestore-ktx")

    implementation("com.google.dagger:hilt-android:2.37")
    kapt("com.google.dagger:hilt-compiler:2.37")

    implementation("androidx.room:room-runtime:2.3.0")
    implementation("androidx.room:room-ktx:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")
}