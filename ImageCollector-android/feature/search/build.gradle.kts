apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.domain))

    "implementation"(DaggerHilt.hiltAndroid)
    "kapt"(DaggerHilt.hiltCompiler)

    "implementation"(AndroidX.coreKtx)
    "implementation"(AndroidX.appCompat)
    "implementation"(AndroidX.navigationFragment)
    "implementation"(AndroidX.navigationUi)
    "implementation"(AndroidX.swiperefreshlayout)

    "implementation"(Google.material)

    "implementation"(Paging.pagingRuntime)
    "implementation"(Paging.pagingCommon)

    "implementation"(Glide.glideImpl)
    "annotationProcessor"(Glide.glideAnnotationProcessor)

    "implementation"(Lottie.lottieAnimation)
}
