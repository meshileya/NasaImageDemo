plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.navigation.safeargs.kotlin)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.nasa.demo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nasa.demo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("debug")
        buildConfigField(
            "String", "BASE_URL", "\"https://api.nasa.gov/mars-photos/api/v1/\""
        )
        buildConfigField(
            "int", "TIMEOUT_SECONDS", "10"
        )
        buildConfigField(
            "String", "API_KEY", "\"Up349dhURITqO3zuA4VU345doazzcsOwCxO9mbrv\""
        )
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }

    dataBinding {
        addKtx = true
    }
}

dependencies {
//UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.swiperefreshlayout)
    //UI [compose]
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //Lifecycle
    implementation(libs.lifecycle)
    //Navigation
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment)
    //DI
    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.fragment.testing)
    implementation(libs.androidx.runtime.livedata)
    kapt(libs.dagger.hilt.android.compiler)
    //circular imageview
    implementation(libs.circleimageview)
    //cache image
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    //Network
    implementation(libs.adapter.rxjava)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)
    implementation(libs.converter.scalars)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.retrofit2.kotlin.coroutines.adapter)
    // Room for local caching
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // Paging
    implementation(libs.androidx.paging.runtime)
    //Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
}