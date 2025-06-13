plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.wearandpair"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.wearandpair"
        minSdk = 30
        targetSdk = 35
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

    implementation(libs.appcompat) {
        exclude(group = "androidx.annotation", module = "annotation")
    }
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.swiperefreshlayout)
    implementation(libs.annotation.jvm){
        exclude(group = "androidx.annotation", module = "annotation")
    }
    implementation(libs.gson)
    implementation(libs.lifecycle.viewmodel.ktx)


    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

}