plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.ksp)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.craftsilicon.shumul.agency"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.craftsilicon.shumul.agency"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            keyAlias = "elmaandroidkeystore"
            keyPassword = "priya143"
            storeFile = file("C:/Users/danvan.mandela/Documents/keys/craft/ElmaAndroidKeyStore")
            storePassword = "priya143"
            enableV1Signing = false
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }

        externalNativeBuild {
            cmake {
                path("CMakeLists.txt")

            }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    hilt {
        enableAggregatingTask = true
    }


//    androidComponents {
//        onVariants {variant->
//         variant.buildConfigFields.put()
//        }
//    }


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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // https://mvnrepository.com/artifact/androidx.compose.material/material-icons-extended
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.navigation)
    implementation(libs.androidx.navigation.common.ktx)

    implementation(libs.androidx.constraintlayout.compose)

    implementation(libs.androidx.multidex)


    /*
    * Hilt
    */
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.androidx.compiler)
    implementation(libs.hilt.androidx.work)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)

    /*
    * Retrofit2
   */

    implementation(libs.retrofit)
    implementation(libs.retrofit.adapter.rxjava2)
    implementation(libs.retrofit.converter.gson)

    /*
    * Okhttp3
   */

    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.okhttp3.okhttp.urlconnection)

    /*
    * Gson
   */

    implementation(libs.gson)

    /*
    * Rx-Java
   */
    implementation(libs.rx.java)
    implementation(libs.rx.android)

    /*
    * Accompanist
   */
    implementation(libs.accompanist.flowlayout)
    implementation(libs.accompanist.pager.indicators)

    /*
    * Viewmodel compose
    */
    implementation(libs.androidx.lifecycle.viewmodel.compose)


    /*
     * Lottie
     */
    implementation(libs.lottie.compose)

    implementation(libs.androidx.biometric)

    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.rxjava2)
    implementation(libs.androidx.datastore.rxjava3)

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.work.rxjava2)
    implementation(libs.androidx.work.gcm)
    androidTestImplementation(libs.androidx.work.testing)
    implementation(libs.androidx.work.multiprocess)

    implementation(libs.coil.compose)


    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.room.compiler)
    ksp(libs.androidx.room.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.rxjava2)
    implementation(libs.androidx.room.rxjava3)
    implementation(libs.androidx.room.guava)
    testImplementation(libs.androidx.room.testing)
    implementation(libs.androidx.room.paging)

    implementation(libs.compose.shimmer)
    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.android.image.cropper)

    implementation(libs.dexter)


    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

}