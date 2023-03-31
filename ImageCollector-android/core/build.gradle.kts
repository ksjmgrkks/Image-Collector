apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation"(DaggerHilt.hiltAndroid)
    "kapt"(DaggerHilt.hiltCompiler)

    "implementation"(AndroidX.coreKtx)
    "implementation"(AndroidX.appCompat)
    "implementation"(AndroidX.navigationFragment)
    "implementation"(AndroidX.navigationUi)
    "implementation"(AndroidX.swiperefreshlayout)
    "implementation"(Google.material)

    "implementation"(Moshi.moshiKotlin)
    "kapt"(Moshi.moshiCodegen)

    "implementation"(Retrofit.okHttp)
    "implementation"(Retrofit.retrofit)
    "implementation"(Retrofit.okHttpLoggingInterceptor)
    "implementation"(Retrofit.moshiConverter)
    "implementation"(Retrofit.rxjavaAdapter)

    "implementation"(Glide.glideImpl)
    "annotationProcessor"(Glide.glideAnnotationProcessor)
}