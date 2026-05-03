plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.escometer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.escometer"
        minSdk = 24
        targetSdk = 36
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

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    // 🔹 Base Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // 🔹 Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // 🔥 Material 3
    implementation(libs.androidx.compose.material3)

    // 🔥 ICONOS (SOLUCIONA TU ERROR)
    implementation("androidx.compose.material:material-icons-extended")

    // 🔥 ViewModel Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

    // 🔌 SERIAL USB
    implementation("com.github.mik3y:usb-serial-for-android:3.7.0")

    // 📊 GRÁFICAS
    implementation("com.patrykandpatrick.vico:compose:1.14.0")

    // 🔁 COROUTINES
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // 🔹 Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // 🔹 Debug
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}