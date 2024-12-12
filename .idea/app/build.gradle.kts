plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.cekcuaca"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cekcuaca"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.picasso:picasso:2.71828")

    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.android.volley:volley:1.2.1")

    // Library Espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Library untuk menjalankan pengujian dengan JUnit
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")

    // Library aturan testing
    androidTestImplementation ("androidx.test:rules:1.5.0")

    // Library Hamcrest untuk matchers
    androidTestImplementation ("org.hamcrest:hamcrest-library:2.2")
}