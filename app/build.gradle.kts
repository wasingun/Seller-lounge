import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.firebase.crashlytics")
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())
val keystoreProperties = Properties()
keystoreProperties.load(project.rootProject.file("keystore.properties").inputStream())

android {
    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"].toString())
            storePassword = keystoreProperties["storePassword"].toString()
            keyAlias = keystoreProperties["keyAlias"].toString()
            keyPassword = keystoreProperties["keyPassword"].toString()
        }
    }
    namespace = "com.wasingun.seller_lounge"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wasingun.seller_lounge"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.1.0"
        setProperty("archivesBaseName", "${applicationId}-v${versionName}")

        buildConfigField("String", "GOOGLE_CLIENT_ID", properties["google_client_id"].toString())
        buildConfigField("String", "NAVER_CLIENT_ID", properties["naver_client_id"].toString())
        buildConfigField(
            "String",
            "NAVER_CLIENT_SECRET",
            properties["naver_client_secret"].toString()
        )
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            versionNameSuffix = "-release"
        }
        getByName("debug") {
            isDebuggable = true
            versionNameSuffix = "-debug"
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

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.5.0")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-crashlytics")

    //Google play service authentication
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    //Jetpack navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    //Json, Http client
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //MpAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")

    //Coil
    implementation("io.coil-kt:coil:2.4.0")

    //Jetpack workManager
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    //Jetpack Paging
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("androidx.room:room-ktx:2.6.0")
    implementation ("androidx.room:room-runtime:2.6.0")
    kapt ("androidx.room:room-compiler:2.6.0")
}

kapt {
    correctErrorTypes = true
}