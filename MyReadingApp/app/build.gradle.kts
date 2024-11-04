plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
//    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.myreadingapp"
    compileSdk = 34
    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.myreadingapp"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("androidx.appcompat:appcompat:1.6.1");
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-storage:19.2.2")
    implementation ("com.github.dhaval2404:imagepicker:2.1")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}