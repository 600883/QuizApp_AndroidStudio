plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.quizapp2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.quizapp2"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    // Room components
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.test.espresso:espresso-intents:3.5.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata:2.3.1")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.3.1")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.github.bumptech.glide:glide:4.12.0")
}