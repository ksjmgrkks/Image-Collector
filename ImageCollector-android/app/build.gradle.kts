plugins {
    id("com.android.application")
    kotlin("android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = ProjectConfig.appId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures.dataBinding = true
}

dependencies {
    implementation(DaggerHilt.hiltAndroid)
    kapt(DaggerHilt.hiltCompiler)

    implementation(project(Modules.domain))
    implementation(project(Modules.data))
    implementation(project(Modules.core))
    implementation(project(Modules.featureSearch))
    implementation(project(Modules.featureBookmarks))

    implementation(AndroidX.coreKtx)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.navigationFragment)
    implementation(AndroidX.navigationUi)

    implementation(Glide.glideImpl)
    annotationProcessor(Glide.glideAnnotationProcessor)

    implementation(Google.material)

    implementation(Moshi.moshiKotlin)
    implementation(Moshi.moshiCodegen)
    implementation(Retrofit.okHttp)
    implementation(Retrofit.retrofit)
    implementation(Retrofit.okHttpLoggingInterceptor)
    implementation(Retrofit.moshiConverter)

    implementation(Paging.pagingRuntime)
    implementation(Paging.pagingCommon)

    testImplementation(Testing.junit4)
    testImplementation(Testing.junitAndroidExt)

    androidTestImplementation(Testing.junit4)
    androidTestImplementation(Testing.junitAndroidExt)
    androidTestImplementation(Testing.testRunner)
}