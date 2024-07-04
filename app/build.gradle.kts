plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.feedforward_courier"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.feedforward_courier"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    //gson
    implementation(libs.gson)
    //Retrofit
    implementation(libs.retrofit)
    //converter-gson
    implementation(libs.converter.gson)
    //OkHttp
    implementation(libs.okhttp)
    //google-play-services
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(platform(libs.kotlin.bom))
    implementation(libs.places)


    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}